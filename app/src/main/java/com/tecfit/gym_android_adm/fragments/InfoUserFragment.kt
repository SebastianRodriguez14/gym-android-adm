package com.tecfit.gym_android_adm.fragments

import android.app.ProgressDialog.show
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tecfit.gym_android_adm.R

import com.tecfit.gym_android_adm.activities.utilities.ForFragments
import com.tecfit.gym_android_adm.databinding.FragmentInfoUserBinding
import com.tecfit.gym_android_adm.models.User
import com.tecfit.gym_android_adm.models.UserInAppCustom.Companion.user
import com.tecfit.gym_android_adm.models.custom.SelectedClass
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

        infoUser()

        return binding.root
    }

    private fun infoUser() {
        user=User(
            SelectedClass.userSelected.id_user, "",
            "", SelectedClass.userSelected.name, SelectedClass.userSelected.lastname,
            SelectedClass.userSelected.phone, SelectedClass.userSelected.membership, SelectedClass.userSelected.image)

        binding.infoUserPhone.text= user!!.phone

        binding.infoUserBtnOption.setOnClickListener{

        }
    }

        return binding.root
    }

}