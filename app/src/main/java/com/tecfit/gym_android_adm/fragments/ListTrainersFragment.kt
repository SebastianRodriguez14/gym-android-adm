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
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.activities.utilities.ForFragments
import com.tecfit.gym_android_adm.activities.utilities.ForMessages
import com.tecfit.gym_android_adm.activities.utilities.ForValidations
import com.tecfit.gym_android_adm.fragments.adapter.TrainerAdapter
import com.tecfit.gym_android_adm.models.File
import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.models.custom.ArrayForClass
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
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.FileOutputStream


class ListTrainersFragment: Fragment() {

    private lateinit var root:View
    private lateinit var addButton: LinearLayout
    private val REQUEST_UPDATE_GALERY = 1001
    private val REQUEST_POST_GALERY = 2001

    // For update
    private lateinit var bottomSheetDialogUpdate:BottomSheetDialog
    private lateinit var bottomSheetViewUpdate:View
    private var uriImagePost:Uri? = null

    //For register
    private lateinit var bottomSheetDialogRegister:BottomSheetDialog
    private lateinit var bottomSheetViewRegister:View
    private var uriImageUpdate: Uri? = null

    //For delete
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
        root=inflater.inflate(R.layout.fragment_trainers,container,false)
        createDeleteDialog()
        createUpdateDialog()
        if (ArrayForClass.arrayTrainer.isEmpty()){
            apiGetTrainers()
        } else {
            initRecyclerView(R.id.recyclerview_trainers)
        }
        fragment = this
        addButton = root.findViewById(R.id.btn_add_trainer)

        bottomSheetDialogDelete.findViewById<View>(R.id.delete_trainer_delete)
            ?.setOnClickListener{
                bottomSheetDialogDelete.findViewById<View>(R.id.delete_trainer_delete)!!.background.alpha = 60
                bottomSheetDialogDelete.findViewById<View>(R.id.delete_trainer_delete)!!.isEnabled = false
                bottomSheetDialogDelete.findViewById<View>(R.id.delete_trainer_cancel)!!.isEnabled = false
                println(SelectedClass.trainerSelected.id_trainer)
                deleteTrainer(SelectedClass.trainerSelected.id_trainer)
            }
        addButton.setOnClickListener{
            createRegisterDialog()
        }
        return root
    }

    fun deleteTrainer(id: Int){
        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val resultTrainers: Call<Void> = apiService.deleteTrainer(id)

        resultTrainers.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                print("se pudo")
                bottomSheetDialogDelete.findViewById<View>(R.id.delete_trainer_delete)!!.background.alpha = 255
                bottomSheetDialogDelete.findViewById<View>(R.id.delete_trainer_delete)!!.isEnabled = true
                bottomSheetDialogDelete.findViewById<View>(R.id.delete_trainer_cancel)!!.isEnabled = true
                apiGetTrainers()
                bottomSheetDialogDelete.dismiss()
                ForMessages.showDeleteMotionToast(fragment, "Entrenador Eliminado", "Se eliminó correctamente")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("no se pudo")
            }

        })
    }

    private fun initRecyclerView(id:Int){
        val recyclerView=root.findViewById<RecyclerView>(id)
        recyclerView.layoutManager=LinearLayoutManager(root.context)
        recyclerView.adapter= TrainerAdapter(ArrayForClass.arrayTrainer, bottomSheetDialogUpdate, bottomSheetDialogDelete)
    }

    private fun apiGetTrainers(){
        val apiService:ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val resultTrainers: Call<List<Trainer>> = apiService.getTrainers()

        resultTrainers.enqueue(object : Callback<List<Trainer>> {
            override fun onResponse(call: Call<List<Trainer>>, response: Response<List<Trainer>>){
                val listTrainers = response.body()
                if (listTrainers != null){
                    ArrayForClass.arrayTrainer = listTrainers.toMutableList()
                    initRecyclerView(R.id.recyclerview_trainers)
                }
            }
            override fun onFailure(call:Call<List<Trainer>>, t:Throwable){
                println("Error: getTrainers() failure")
                println(t.message)
            }
        })

    }

    private fun createDeleteDialog() {
        bottomSheetDialogDelete = BottomSheetDialog(requireActivity(), R.style.BottonSheetDialog)
        bottomSheetViewDelete = layoutInflater.inflate(R.layout.bottom_sheet_dialog_delete_trainer, null)
        bottomSheetDialogDelete.setContentView(bottomSheetViewDelete)
        bottomSheetViewDelete.findViewById<TextView>(R.id.delete_trainer_cancel).setOnClickListener {
            bottomSheetDialogDelete.dismiss()
        }
    }

    private fun createRegisterDialog(){

        bottomSheetDialogRegister = BottomSheetDialog(
            requireActivity(), R.style.BottonSheetDialog
        )

        bottomSheetViewRegister  = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

        bottomSheetViewRegister.findViewById<View>(R.id.register_trainer_button).setOnClickListener{
            if (validateRegister()){
                println("Pasamos las validaciones")
                val trainer = Trainer(
                    ForValidations.removeBlanks(bottomSheetViewRegister.findViewById<EditText>(R.id.register_trainer_name).text.toString()),
                    ForValidations.removeBlanks(bottomSheetViewRegister.findViewById<EditText>(R.id.register_trainer_lastname).text.toString()),
                    0,
                    ForValidations.removeBlanks(bottomSheetViewRegister.findViewById<EditText>(R.id.register_trainer_description).text.toString()),
                    File("", 0)
                    )
                apiPostTrainerWithFile(trainer)
                bottomSheetDialogRegister.dismiss()
            }

        }

        bottomSheetViewRegister.findViewById<TextView>(R.id.register_trainer_image_button).setOnClickListener {
            checkPermissionsForGalery(2)
        }

        bottomSheetViewRegister.findViewById<View>(R.id.image_tooltip).setOnClickListener{
            ForMessages.showInfoMotionToast(this, "¿Cómo debe ser la imagen?", "Se registró correctamente")
        }

        bottomSheetDialogRegister.setContentView(bottomSheetViewRegister)
        bottomSheetDialogRegister.show()
    }

    private fun createUpdateDialog() {
        bottomSheetDialogUpdate = BottomSheetDialog(requireActivity(), R.style.BottonSheetDialog)

        bottomSheetViewUpdate = layoutInflater.inflate(R.layout.bottom_sheet_dialog_trainer_update, null)

        bottomSheetDialogUpdate.setContentView(bottomSheetViewUpdate)

        bottomSheetDialogUpdate.setOnShowListener {
            bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_name).text = SelectedClass.trainerSelected.name
            bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_lastname).text = SelectedClass.trainerSelected.lastname
            bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_description).text = SelectedClass.trainerSelected.description
            Glide.with(this).load(SelectedClass.trainerSelected.file.url).into(bottomSheetViewUpdate.findViewById(R.id.update_trainer_image))

        }

        bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_cancel).setOnClickListener {
            bottomSheetDialogUpdate.dismiss()
        }

        bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_save).setOnClickListener {
            if (validateUpdate()){
                println("Pasamos las validaciones")
                val trainer = Trainer(
                    ForValidations.removeBlanks(bottomSheetViewUpdate.findViewById<EditText>(R.id.update_trainer_name).text.toString()),
                    ForValidations.removeBlanks(bottomSheetViewUpdate.findViewById<EditText>(R.id.update_trainer_lastname).text.toString()),
                    0,
                    ForValidations.removeBlanks(bottomSheetViewUpdate.findViewById<EditText>(R.id.update_trainer_description).text.toString()),
                    File("", 0)
                )
                if (uriImageUpdate == null){
                    trainer.file = SelectedClass.trainerSelected.file
                    apiPutTrainer(trainer)
                } else {
                    apiPutFileWithTrainer(trainer)

                }
                bottomSheetDialogUpdate.dismiss()
            }


            bottomSheetDialogUpdate.dismiss()
        }

        bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_image_button).setOnClickListener {
            checkPermissionsForGalery(1)
        }


    }

    private fun validateImage(uri:Uri?, textViewError: TextView):Boolean{
        val existError = uri == null
        textViewError.visibility = if (existError) View.VISIBLE else View.INVISIBLE
        return existError
    }

    private fun validateRegister():Boolean{
        val checks = arrayOf(
            ForValidations.valInput(bottomSheetViewRegister.findViewById(R.id.register_trainer_name),
                bottomSheetViewRegister.findViewById(R.id.register_trainer_name_error), ForValidations::valOnlyText),
            ForValidations.valInput(bottomSheetViewRegister.findViewById(R.id.register_trainer_lastname),
                bottomSheetViewRegister.findViewById(R.id.register_trainer_lastname_error), ForValidations::valOnlyText),
            ForValidations.valInput(bottomSheetViewRegister.findViewById(R.id.register_trainer_description),
                bottomSheetViewRegister.findViewById(R.id.register_trainer_description_error), null),
            validateImage(uriImagePost, bottomSheetViewRegister.findViewById(R.id.register_trainer_image_error))
        )
        return !checks.contains(true)
    }

    private fun validateUpdate():Boolean{
        val checks = arrayOf(
            ForValidations.valInput(bottomSheetViewUpdate.findViewById(R.id.update_trainer_name),
                bottomSheetViewUpdate.findViewById(R.id.update_trainer_name_error), ForValidations::valOnlyText),
            ForValidations.valInput(bottomSheetViewUpdate.findViewById(R.id.update_trainer_lastname),
                bottomSheetViewUpdate.findViewById(R.id.update_trainer_lastname_error), ForValidations::valOnlyText),
            ForValidations.valInput(bottomSheetViewUpdate.findViewById(R.id.update_trainer_description),
                bottomSheetViewUpdate.findViewById(R.id.update_trainer_description_error), null)
        )
        return !checks.contains(true)
    }


    private fun checkPermissionsForGalery(type:Int) {
        //Verificación de la versión de android
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)  ==
                PackageManager.PERMISSION_DENIED){
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
    // El type es para saber si se abrirá para actualizar o registrar
    private fun openGalery(type:Int){
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

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_UPDATE_GALERY){

            uriImageUpdate = data?.data!!
            bottomSheetViewUpdate.findViewById<ImageView>(R.id.update_trainer_image).setImageURI(uriImageUpdate)

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_POST_GALERY) {

            uriImagePost = data?.data!!
            bottomSheetViewRegister.findViewById<ImageView>(R.id.register_trainer_image).setImageURI(uriImagePost)

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

    private fun apiPutFileWithTrainer(trainer:Trainer){
        if(uriImageUpdate == null) return

        val apiService:ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val requestIdFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), SelectedClass.trainerSelected.file.id_file.toString())
        val resultFile: Call<File> = apiService.updateFile(processImage(uriImageUpdate), requestIdFile)

        resultFile.enqueue(object : Callback<File> {
            override fun onResponse(call: Call<File>, response: Response<File>){
                if (response.isSuccessful){
                    println("Imagen actualizada")
                    trainer.file = response.body()!!

                    apiPutTrainer(trainer)

                    uriImageUpdate = null
                }
            }
            override fun onFailure(call:Call<File>, t:Throwable){
                println("Error: updateFile() failure")
                println(t.message)
            }
        })
    }

    private fun apiPutTrainer(trainer:Trainer) {
        val apiService:ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultTrainer: Call<Trainer> = apiService.putTrainer(trainer, SelectedClass.trainerSelected.id_trainer)
        println(trainer)
        println(SelectedClass.trainerSelected)
        resultTrainer.enqueue(object : Callback<Trainer> {
            override fun onResponse(call: Call<Trainer>, response: Response<Trainer>){
                if (response.isSuccessful && response.body() != null){

                    val position = ArrayForClass.arrayTrainer.indexOf(
                        ArrayForClass.arrayTrainer.find { tr -> tr.id_trainer == response.body()!!.id_trainer }
                    )
                    ArrayForClass.arrayTrainer.removeAt(position)
                    ArrayForClass.arrayTrainer.add(position, response.body()!!)
                    initRecyclerView(R.id.recyclerview_trainers)

                    ForMessages.showSuccessMotionToast(fragment, "Entrenador Actualizado", "Se actualizó correctamente")
                } else{
                    ForMessages.showErrorMotionToast(fragment, "Entrenador No Actualizado", "Hubo un error al actualizar el entrenador")
                }
            }
            override fun onFailure(call:Call<Trainer>, t:Throwable){
                println("Error: putTrainer() failure")
                println(t.message)
            }
        })
    }



    private fun apiPostTrainerWithFile(trainer:Trainer){
        if(uriImagePost == null) return
        val apiService:ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultFile: Call<File> = apiService.postFile(processImage(uriImagePost))
        resultFile.enqueue(object : Callback<File> {
            override fun onResponse(call: Call<File>, response: Response<File>){
                if (response.isSuccessful){
                    println("Imagen registrada")
                    uriImagePost = null
                    trainer.file = response.body()!!

                    apiPostTrainer(trainer)
                } else {
                    ForMessages.showErrorMotionToast(fragment, "Imagen No Registrada", "La imagen es muy pesada o no es del formato correcto (png/jpg)")
                }
            }
            override fun onFailure(call:Call<File>, t:Throwable){
                println("Error: postFile() failure")
                println(t.message)
            }
        })
    }

    private fun apiPostTrainer(trainer:Trainer) {
        val apiService:ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultTrainer: Call<Trainer> = apiService.postTrainer(trainer)
        resultTrainer.enqueue(object : Callback<Trainer> {
            override fun onResponse(call: Call<Trainer>, response: Response<Trainer>){
                if (response.isSuccessful && response.body() != null){
                    ArrayForClass.arrayTrainer.add(response.body()!!)
                    initRecyclerView(R.id.recyclerview_trainers)
                    ForMessages.showSuccessMotionToast(fragment, "Entrenador Registrado", "Se registró correctamente")
                } else{
                    ForMessages.showErrorMotionToast(fragment, "Entrenador No Registrado", "Hubo un error al registrar el entrenador")
                }
            }
            override fun onFailure(call:Call<Trainer>, t:Throwable){
                println("Error: postTrainer() failure")
                println(t.message)
            }
        })
    }



}