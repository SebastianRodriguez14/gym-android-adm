package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tecfit.gym_android_adm.databinding.FragmentActivateMembershipBinding
import com.tecfit.gym_android_adm.models.User

class ActivateMembershipFragment :Fragment(){

    lateinit var binding:FragmentActivateMembershipBinding
    lateinit var  user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentActivateMembershipBinding.inflate(layoutInflater)

        return binding.root

    }

}