package com.tecfit.gym_android_adm.fragments.adapter

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.models.Product

class ProductViewHolder(val view:View) :RecyclerView.ViewHolder(view){

    val pr_image = view.findViewById<ImageView>(R.id.item_product_image)

    val pr_description = view.findViewById<TextView>(R.id.item_product_description)

    val pr_price = view.findViewById<TextView>(R.id.item_product_price)

    val pr_status_background = view.findViewById<FrameLayout>(R.id.item_product_status_background)

    val pr_status = view.findViewById<LinearLayout>(R.id.item_product_status)

    val pr_discount = view.findViewById<TextView>(R.id.item_product_discount)

    fun render(product: Product) {

        Glide.with(view.context).load(product.image.url).into(pr_image)
        pr_description.text = product.name
        pr_price.text = "S/. %.2f".format(product.price)

        if (product.discount > 0){
            pr_discount.visibility = View.VISIBLE
            pr_discount.text = "Sale " + Math.round(product.discount) + "%"

        }

        if (!product.status){
            pr_status_background.setBackgroundColor(Color.parseColor("#34E10A0A"))
            pr_status.visibility = View.INVISIBLE
            pr_image.setColorFilter(Color.parseColor("#34E10A0A"))

        }



    }

}