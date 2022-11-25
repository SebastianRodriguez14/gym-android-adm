package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.activities.utilities.ForMessages
import com.tecfit.gym_android_adm.activities.utilities.ForValidations
import com.tecfit.gym_android_adm.databinding.FragmentProductsBinding
import com.tecfit.gym_android_adm.fragments.adapter.ProductAdapter
import com.tecfit.gym_android_adm.models.File
import com.tecfit.gym_android_adm.models.Product
import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.models.custom.ArraysForClass
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

class ListProductFragment: Fragment() {

    private lateinit var root:View
    lateinit var binding: FragmentProductsBinding

    private lateinit var bottomSheetDialogRegister:BottomSheetDialog
    private lateinit var bottomSheetViewRegister:View

    private lateinit var bottomSheetDialogUpdate:BottomSheetDialog
    private lateinit var bottomSheetViewUpdate:View


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
        val gridLayoutManager = GridLayoutManager(root.context, 2)
        gridLayoutManager.widthMode
        binding.recyclerviewProducts.layoutManager = gridLayoutManager

        if (ArraysForClass.arrayProducts.isEmpty()){
            apiGetProducts()
        } else {
            setArrayForRecycler()
        }

        binding.btnAddProduct.setOnClickListener {
            createRegisterDialog()
        }


        return binding.root

    }

    private fun createRegisterDialog(){

        bottomSheetDialogRegister = BottomSheetDialog(
            requireActivity(), R.style.BottonSheetDialog
        )

        bottomSheetViewRegister  = layoutInflater.inflate(R.layout.bottom_sheet_dialog_register_product, null)

        bottomSheetDialogRegister.setContentView(bottomSheetViewRegister)
        bottomSheetDialogRegister.show()
    }

    private fun createUpdateDialog(){
        bottomSheetDialogUpdate = BottomSheetDialog(
            requireActivity(), R.style.BottonSheetDialog
        )

        bottomSheetViewUpdate  = layoutInflater.inflate(R.layout.bottom_sheet_dialog_update_product, null)

        bottomSheetDialogUpdate.setContentView(bottomSheetViewUpdate)

    }

    private fun setArrayForRecycler(filter:Boolean = false) {
        var products = if (!filter) ArraysForClass.arrayProducts else FilterProducts.applyFilters(
            ArraysForClass.arrayProducts
        )

        binding.recyclerviewProducts.adapter = ProductAdapter(products, bottomSheetDialogUpdate)

        binding.productsListLinear.isVisible = products.isNotEmpty()
        binding.productsListVoidLinear.isVisible = products.isEmpty()
    }


    private fun apiGetProducts() {

        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val resultProducts: Call<List<Product>> = apiService.getProducts()

        resultProducts.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                val listProducts = response.body()

                if (listProducts !=  null) {
                    ArraysForClass.arrayProducts = listProducts.toMutableList()
                    setArrayForRecycler()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                println("Error: getProducts() failure")
                apiGetProducts()
            }
        })


    }
}