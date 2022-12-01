package com.tecfit.gym_android_adm.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.models.User

class UserAdapter(private val userList:List<User>, val manager: FragmentManager?) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):UserViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(layoutInflater.inflate(R.layout.item_users,parent,false), manager)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position:Int) {
        val item=userList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int =userList.size

}