package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.databinding.FragmentProductsBinding
import com.tecfit.gym_android_adm.fragments.adapter.ProductAdapter
import com.tecfit.gym_android_adm.models.Product
import com.tecfit.gym_android_adm.models.custom.ArrayForClass
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
        val gridLayoutManager = GridLayoutManager(root.context, 2)
        gridLayoutManager.widthMode
        binding.recyclerviewProducts.layoutManager = gridLayoutManager

        if (ArrayForClass.arrayTrainer.isEmpty()){
            println("asd")
            apiGetProducts()
        } else {
            println("das")
            setArrayForRecycler()
        }

        return binding.root

    }

    private fun setArrayForRecycler(filter:Boolean = false) {
        var products = if (!filter) ArraysForClass.arrayProducts!! else FilterProducts.applyFilters(
            ArraysForClass.arrayProducts!!
        )

        binding.recyclerviewProducts.adapter = ProductAdapter(products)

        binding.productsListLinear.isVisible = products.isNotEmpty()
        binding.productsListVoidLinear.isVisible = products.isEmpty()
    }


    private fun apiGetProducts() {

        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val resultProducts: Call<List<Product>> = apiService.getProducts()

        resultProducts.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                val listProducts = response.body()

                if (listProducts != null) {
                    ArraysForClass.arrayProducts = listProducts
                    setArrayForRecycler()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                println("Error: getProducts() failure")
            }
        })


    }
}