package com.tecfit.gym_android_adm.fragments.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.models.User

class UserViewHolder (val view: View) : RecyclerView.ViewHolder(view){

    val user_image=view.findViewById<ImageView>(R.id.item_user_image)
    val user_name=view.findViewById<TextView>(R.id.item_user_name)
    //icono verde para usuarios con membresia activa -> cambiar visibility a false si no cuenta con una membresia
    val user_status=view.findViewById<LinearLayout>(R.id.item_user_status)

    fun render(user:User){

        user_name.text=user.name
        if(user.image!=null){
            Glide.with(view.context).load(user.image.url).into(user_image)
        }


       if(user.membership!=true){
            user_status.visibility=View.INVISIBLE
        }
    }

}