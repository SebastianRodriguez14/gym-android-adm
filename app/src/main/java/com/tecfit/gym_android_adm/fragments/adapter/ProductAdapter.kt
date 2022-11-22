package com.tecfit.gym_android_adm.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.models.Product

class ProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val item = productList[position]
        holder.render(item)

    }

    override fun getItemCount(): Int = productList.size
}