package com.tecfit.gym_android_adm.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.switchmaterial.SwitchMaterial
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.activities.utilities.ForMessages
import com.tecfit.gym_android_adm.activities.utilities.ForValidations
import com.tecfit.gym_android_adm.databinding.FragmentProductsBinding
import com.tecfit.gym_android_adm.fragments.adapter.ProductAdapter
import com.tecfit.gym_android_adm.models.File
import com.tecfit.gym_android_adm.models.Product
import com.tecfit.gym_android_adm.models.custom.ArraysForClass
import com.tecfit.gym_android_adm.models.custom.SelectedClass
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileOutputStream

class ListProductFragment: Fragment() {

    private lateinit var root: View
    lateinit var binding: FragmentProductsBinding
    private val REQUEST_UPDATE_GALERY = 1001
    private val REQUEST_POST_GALERY = 2001


    private lateinit var bottomSheetDialogRegister: BottomSheetDialog
    private lateinit var bottomSheetViewRegister: View
    private var uriImagePost: Uri? = null

    private lateinit var bottomSheetDialogUpdate: BottomSheetDialog
    private lateinit var bottomSheetViewUpdate: View
    private var uriImageUpdate: Uri? = null

    private lateinit var bottomSheetDialogDelete:BottomSheetDialog
    private lateinit var bottomSheetViewDelete:View

    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_products, container, false)
        binding = FragmentProductsBinding.inflate(inflater)
        createUpdateDialog()
        createDeleteDialog()
        val gridLayoutManager = GridLayoutManager(root.context, 2)
        gridLayoutManager.widthMode
        binding.recyclerviewProducts.layoutManager = gridLayoutManager

        if (ArraysForClass.arrayProducts.isEmpty()) {
            apiGetProducts()
        } else {
            setArrayForRecycler()
        }
        fragment = this

        bottomSheetDialogDelete.findViewById<View>(R.id.delete_product_delete)?.setOnClickListener {
            bottomSheetDialogDelete.findViewById<View>(R.id.delete_product_delete)!!.background.alpha=60
            bottomSheetDialogDelete.findViewById<View>(R.id.delete_product_delete)!!.isEnabled=false
            bottomSheetDialogDelete.findViewById<View>(R.id.delete_product_cancel)!!.isEnabled=false
            deleteTrainer(SelectedClass.productSelected.id_product)
        }

        binding.btnAddProduct.setOnClickListener {
            createRegisterDialog()
        }


        return binding.root

    }

    private fun deleteTrainer(id:Int) {
        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val resultProducts: Call<Void> =apiService.deleteProduct(id)

        resultProducts.enqueue(object :Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                bottomSheetDialogDelete.findViewById<View>(R.id.delete_product_delete)!!.background.alpha=255
                bottomSheetDialogDelete.findViewById<View>(R.id.delete_product_delete)!!.isEnabled=true
                bottomSheetDialogDelete.findViewById<View>(R.id.delete_product_cancel)!!.isEnabled=true
                apiGetProducts()
                bottomSheetDialogDelete.dismiss()
                ForMessages.showSuccessMotionToast(fragment,"Producto Eliminado","Se eliminó correctamente")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error: deleteProduct() failure")
            }
        })

    }

    class FilterProducts {
        companion object {
            var nameProduct:String = ""
            var availableProduct:Boolean = false

            fun applyFilters(products: List<Product>): List<Product> {

                val filteredProducts = products.filter { product ->
                    val checkName = if(nameProduct == "") true else product.name.lowercase().startsWith(nameProduct.lowercase())
                    val checkAvailable = if (availableProduct) product.status else true
                    checkName && checkAvailable
                }
                return filteredProducts
            }

        }
    }

    private fun createDeleteDialog(){
        bottomSheetDialogDelete = BottomSheetDialog(requireActivity(), R.style.BottonSheetDialog)
        bottomSheetViewDelete = layoutInflater.inflate(R.layout.bottom_sheet_dialog_delete_product, null)
        bottomSheetDialogDelete.setContentView(bottomSheetViewDelete)
        bottomSheetViewDelete.findViewById<TextView>(R.id.delete_product_cancel).setOnClickListener {
            bottomSheetDialogDelete.dismiss()
        }
    }

    private fun apiGetProducts() {

        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val resultProducts: Call<List<Product>> = apiService.getProducts()

        resultProducts.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                val listProducts = response.body()

                if (listProducts != null) {
                    ArraysForClass.arrayProducts = listProducts.toMutableList()
                    setArrayForRecycler()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                println("Error: getProducts() failure")
            }
        })
    }

    private fun createRegisterDialog() {
        bottomSheetDialogRegister = BottomSheetDialog(requireActivity(), R.style.BottonSheetDialog)

        bottomSheetViewRegister = layoutInflater.inflate(R.layout.bottom_sheet_dialog_register_product, null)

        bottomSheetViewRegister.findViewById<TextView>(R.id.register_product_button).setOnClickListener {
            bottomSheetDialogRegister.findViewById<View>(R.id.register_product_button)!!.background.alpha = 60
            bottomSheetDialogRegister.findViewById<View>(R.id.register_product_button)!!.isEnabled = false
            bottomSheetDialogRegister.findViewById<View>(R.id.register_product_cancel)!!.isEnabled = false
            if (validateRegister()) {
                println("Pasamos las validaciones")
                val product = Product(
                    ForValidations.removeBlanks(bottomSheetViewRegister.findViewById<EditText>(R.id.register_product_name).text.toString()),
                    bottomSheetViewRegister.findViewById<SwitchMaterial>(R.id.register_product_active).isChecked,
                    bottomSheetViewRegister.findViewById<EditText>(R.id.register_product_discount).text.toString().toDouble(),
                    0,
                    bottomSheetViewRegister.findViewById<EditText>(R.id.register_product_price).text.toString().toDouble(),
                    File("", 0)
                )
                println(product.toString())
                apiPostProductWithFile(product)
            }
        }

        bottomSheetViewRegister.findViewById<TextView>(R.id.register_product_image_button).setOnClickListener {
            checkPermissionsForGalery(2)
        }

        bottomSheetViewRegister.findViewById<View>(R.id.image_tooltip_product).setOnClickListener{
            ForMessages.showInfoMotionToast(this, "¿Cómo debe ser la imagen?", "Se registró correctamente")
        }

        bottomSheetDialogRegister.setContentView(bottomSheetViewRegister)
        bottomSheetDialogRegister.show()
    }

    private fun validateImage(uri:Uri?, textViewError: TextView):Boolean{
        val existError = uri == null
        textViewError.visibility = if (existError) View.VISIBLE else View.INVISIBLE
        return existError
    }

    private fun validateRegister(): Boolean {
        val checks = arrayOf(
            ForValidations.valInput(bottomSheetViewRegister.findViewById(R.id.register_product_name),
                bottomSheetViewRegister.findViewById(R.id.register_product_name_error), ForValidations::valAllTypeText),
            ForValidations.valInput(bottomSheetViewRegister.findViewById(R.id.register_product_discount),
                bottomSheetViewRegister.findViewById(R.id.register_product_discount_error),ForValidations::valNumber),
            validateImage(uriImagePost,bottomSheetViewRegister.findViewById(R.id.register_product_image_error))

            )
        return !checks.contains(true)
    }

    private fun validateUpdate():Boolean{
        val checks = arrayOf(
            ForValidations.valInput(bottomSheetViewUpdate.findViewById(R.id.update_product_name),
                bottomSheetViewUpdate.findViewById(R.id.update_product_name_error), ForValidations::valAllTypeText),
            ForValidations.valInput(bottomSheetViewUpdate.findViewById(R.id.update_product_discount),
                bottomSheetViewUpdate.findViewById(R.id.update_product_discount_error), ForValidations::valNumber)
        )
        return !checks.contains(true)
    }

    private fun createUpdateDialog() {
        bottomSheetDialogUpdate = BottomSheetDialog(
            requireActivity(), R.style.BottonSheetDialog
        )
        bottomSheetViewUpdate =
            layoutInflater.inflate(R.layout.bottom_sheet_dialog_update_product, null)

        bottomSheetDialogUpdate.setContentView(bottomSheetViewUpdate)

        bottomSheetDialogUpdate.setOnShowListener{
            bottomSheetViewUpdate.findViewById<TextView>(R.id.update_product_name).text = SelectedClass.productSelected.name
            bottomSheetViewUpdate.findViewById<TextView>(R.id.update_product_price).text = SelectedClass.productSelected.price.toString()
            bottomSheetViewUpdate.findViewById<TextView>(R.id.update_product_discount).text = SelectedClass.productSelected.discount.toInt().toString()
            bottomSheetViewUpdate.findViewById<SwitchMaterial>(R.id.update_product_active).isChecked = SelectedClass.productSelected.status
            Glide.with(this).load(SelectedClass.productSelected.image.url).into(bottomSheetViewUpdate.findViewById(R.id.update_product_image))
        }

        bottomSheetViewUpdate.findViewById<TextView>(R.id.update_product_cancel).setOnClickListener{
            bottomSheetDialogUpdate.dismiss()
        }

        bottomSheetViewUpdate.findViewById<TextView>(R.id.update_product_button).setOnClickListener {
            bottomSheetDialogUpdate.findViewById<View>(R.id.update_product_button)!!.background.alpha = 60
            bottomSheetDialogUpdate.findViewById<View>(R.id.update_product_button)!!.isEnabled = false
            bottomSheetDialogUpdate.findViewById<View>(R.id.update_product_cancel)!!.isEnabled = false
            if (validateUpdate()) {
            val product = Product(
                ForValidations.removeBlanks(bottomSheetViewUpdate.findViewById<EditText>(R.id.update_product_name).text.toString()),
                bottomSheetViewUpdate.findViewById<SwitchMaterial>(R.id.update_product_active).isChecked,
                ForValidations.removeBlanks(bottomSheetViewUpdate.findViewById<EditText>(R.id.update_product_discount).text.toString())
                    .toDouble(),
                0,
                bottomSheetViewUpdate.findViewById<EditText>(R.id.update_product_price).text.toString()
                    .toDouble(),
                File("", 0)
            )
            if (uriImageUpdate == null) {
                product.image = SelectedClass.productSelected.image
                apiPutProduct(product)
            } else {
                apiPutFileWithProduct(product)
            }
            }
        }

        bottomSheetViewUpdate.findViewById<TextView>(R.id.update_product_image_button).setOnClickListener {
            checkPermissionsForGalery(1)
        }
    }

    private fun setArrayForRecycler(filter: Boolean = false) {
        var products = if (!filter) ArraysForClass.arrayProducts else FilterProducts.applyFilters(
            ArraysForClass.arrayProducts
        )

        binding.recyclerviewProducts.adapter = ProductAdapter(products, bottomSheetDialogUpdate, bottomSheetDialogDelete)

        binding.productsListLinear.isVisible = products.isNotEmpty()
        binding.productsListVoidLinear.isVisible = products.isEmpty()
    }


    private fun checkPermissionsForGalery(type: Int) {
        //Verificación de la versión de android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                // Si no tiene permisos, lo pedimos
                val filePermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requireActivity().requestPermissions(filePermission, REQUEST_UPDATE_GALERY)
            } else {
                // Sí tiene permisos
                openGalery(type)
            }
        } else {
            // Versión de lollipop hacia abajo tienen permisos por defecto
            openGalery(type)
        }
    }

    private fun openGalery(type: Int) {
        val intentGalery = Intent(Intent.ACTION_PICK)
        intentGalery.type = "image/*"
        if (type == 1) {
            startActivityForResult(intentGalery, REQUEST_UPDATE_GALERY)
        } else if (type == 2) {
            startActivityForResult(intentGalery, REQUEST_POST_GALERY)
        }
        Toast.makeText(context, "Open galery...", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_UPDATE_GALERY) {
            uriImageUpdate = data?.data!!
            bottomSheetViewUpdate.findViewById<ImageView>(R.id.update_product_image).setImageURI(uriImageUpdate)
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_POST_GALERY) {

            uriImagePost = data?.data!!
            bottomSheetViewRegister.findViewById<ImageView>(R.id.register_product_image).setImageURI(uriImagePost)

        }
    }

    private fun processImage(uri: Uri?): MultipartBody.Part {
        val filesDir = context?.filesDir
        val file = java.io.File(filesDir, "image.png")
        val inputStream = context?.contentResolver?.openInputStream(uri!!)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("multipartFile", file.name, requestBody)
    }

    private fun apiPostProductWithFile(product: Product){
        if (uriImagePost== null)return
        val apiService:ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultFile: Call<File> = apiService.postFile(processImage(uriImagePost))
        resultFile.enqueue(object : Callback<File> {
            override fun onResponse(call: Call<File>, response: Response<File>) {
                if (response.isSuccessful) {
                    uriImagePost = null
                    product.image = response.body()!!
                    apiPostProduct(product)
                } else {
                    ForMessages.showErrorMotionToast(
                        fragment,
                        "Producto No Registrado",
                        "La imagen es muy pesada o no es del formato correcto (png/jpg)"
                    )
                }
            }

            override fun onFailure(call: Call<File>, t: Throwable) {
                println("Error: postFile() failure")
                println(t.message)
            }
        })
    }

    private fun apiPostProduct(product: Product) {
        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resulProduct:Call<Product> = apiService.postProduct(product)
        resulProduct.enqueue(object :Callback<Product>{
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if(response.isSuccessful && response.body() != null ){
                    ArraysForClass.arrayProducts.add(response.body()!!)
                    apiGetProducts()
                    bottomSheetDialogRegister.findViewById<View>(R.id.register_product_button)!!.background.alpha = 255
                    bottomSheetDialogRegister.findViewById<View>(R.id.register_product_button)!!.isEnabled = true
                    bottomSheetDialogRegister.findViewById<View>(R.id.register_product_cancel)!!.isEnabled = true
                    ForMessages.showSuccessMotionToast(fragment,"Producto Registrado","Se registró correctamente")
                    bottomSheetDialogRegister.dismiss()
                }else{
                    ForMessages.showDeleteMotionToast(fragment,"Producto No Registrado", "Hubo un error al registrar el producto")
                }
            }
            override fun onFailure(call:Call<Product>, t:Throwable){
                println("Error: postProduct() failure")
                println(t.message)
            }
        })

    }

    private fun apiPutProduct(product: Product) {
        val apiService:ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultProduct: Call<Product> = apiService.putProduct(product, SelectedClass.productSelected.id_product)
        resultProduct.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful && response.body() != null){

                    val position = ArraysForClass.arrayProducts.indexOf(
                        ArraysForClass.arrayProducts.find { pr -> pr.id_product == response.body()!!.id_product }
                    )
                    ArraysForClass.arrayProducts.removeAt(position)
                    ArraysForClass.arrayProducts.add(position, response.body()!!)
                    bottomSheetDialogUpdate.findViewById<View>(R.id.update_product_button)!!.background.alpha = 255
                    bottomSheetDialogUpdate.findViewById<View>(R.id.update_product_button)!!.isEnabled = true
                    bottomSheetDialogUpdate.findViewById<View>(R.id.update_product_cancel)!!.isEnabled = true
                    ForMessages.showSuccessMotionToast(fragment, "Producto Actualizado", "Se actualizó correctamente")
                    apiGetProducts()
                    bottomSheetDialogUpdate.dismiss()

                } else{
                    ForMessages.showErrorMotionToast(fragment, "Producto No Actualizado", "Hubo un error al actualizar el producto")
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                println("Error: putTrainer() failure")
                println(t.message)
            }
        })
    }

    private fun apiPutFileWithProduct(product: Product){
        if(uriImageUpdate == null) return

        val apiService:ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val requestIdFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), SelectedClass.productSelected.image.id_file.toString())
        val resultFile: Call<File> = apiService.updateFile(processImage(uriImageUpdate), requestIdFile)

        resultFile.enqueue(object : Callback<File> {
            override fun onResponse(call: Call<File>, response: Response<File>){
                if (response.isSuccessful){
                    product.image = response.body()!!
                    apiPutProduct(product)
                    uriImageUpdate = null
                }
            }
            override fun onFailure(call:Call<File>, t:Throwable){
                println("Error: updateFile() failure")
                println(t.message)
            }
        })
    }


}

