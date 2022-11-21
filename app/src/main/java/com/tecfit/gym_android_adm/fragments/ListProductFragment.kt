package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.fragments.adapter.ProductAdapter
import com.tecfit.gym_android_adm.models.custom.ArrayForClass

class ListProductFragment: Fragment() {

    private lateinit var root:View

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

        if (ArrayForClass.arrayTrainer.isEmpty()){
            //
        } else {
            initRecyclerView(R.id.recyclerview_products)
        }
        fragment = this

        return root

    }

    private fun initRecyclerView(id: Int) {
        val recyclerView=root.findViewById<RecyclerView>(id)
        recyclerView.layoutManager=LinearLayoutManager(root.context)
    }


}