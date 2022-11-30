package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.activities.utilities.ForFragments
import com.tecfit.gym_android_adm.databinding.BottomSheetDialogRegisterProductBinding
import com.tecfit.gym_android_adm.databinding.FragmentDetailsUserBinding

class DetailsUserFragment : Fragment() {

    lateinit var binding: FragmentDetailsUserBinding
    private val infoUserFragment = InfoUserFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsUserBinding.inflate(layoutInflater)
        ForFragments.replaceFragment(childFragmentManager, binding.frameDetailsUser.id, infoUserFragment )
        return binding.root

    }

}