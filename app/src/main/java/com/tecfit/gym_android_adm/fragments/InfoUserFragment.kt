package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.databinding.FragmentInfoUserBinding
import com.tecfit.gym_android_adm.databinding.FragmentProductsBinding

class InfoUserFragment : Fragment() {

    lateinit var binding: FragmentInfoUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoUserBinding.inflate(layoutInflater)
        return binding.root
    }
}