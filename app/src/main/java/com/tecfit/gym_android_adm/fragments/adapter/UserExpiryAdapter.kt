package com.tecfit.gym_android_adm.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.models.custom.UserToFinish

class UserExpiryAdapter(private val userToFinishList: List<UserToFinish>, val manager: FragmentManager?) : RecyclerView.Adapter<UserExpiryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserExpiryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserExpiryViewHolder(layoutInflater.inflate(R.layout.item_users_expiry,parent,false), manager)
    }

    override fun onBindViewHolder(holder: UserExpiryViewHolder, position: Int) {
        val item=userToFinishList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = userToFinishList.size



}