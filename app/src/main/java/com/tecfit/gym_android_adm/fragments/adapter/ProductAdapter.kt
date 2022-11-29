package com.tecfit.gym_android_adm.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.models.Product

class ProductAdapter(private val productList: List<Product>, val bottomSheetDialogUpdate: BottomSheetDialog,  val bottomSheetDialogDelete:BottomSheetDialog) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false), bottomSheetDialogUpdate, bottomSheetDialogDelete)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val item = productList[position]
        holder.render(item)

    }

    override fun getItemCount(): Int = productList.size
}