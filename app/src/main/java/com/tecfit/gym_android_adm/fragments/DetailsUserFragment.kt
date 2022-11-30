package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.tecfit.gym_android_adm.activities.utilities.ForFragments
import com.tecfit.gym_android_adm.databinding.FragmentDetailsUserBinding
import com.tecfit.gym_android_adm.models.User
import com.tecfit.gym_android_adm.models.custom.SelectedClass

class DetailsUserFragment : Fragment() {

    lateinit var binding: FragmentDetailsUserBinding
    private val infoUserFragment = InfoUserFragment()
    private lateinit var user: User

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

       detailUser()


        return binding.root

    }

    private fun detailUser() {
        user=User(SelectedClass.userSelected.id_user, "",
            "",SelectedClass.userSelected.name,SelectedClass.userSelected.lastname,
            "",SelectedClass.userSelected.membership,SelectedClass.userSelected.image)

        if(user!!.image?.url!=null){
            Glide.with(this).load(user!!.image?.url).into(binding.photoProfile)
        }
        binding.detailUserName.text= user.name+" "+user.lastname
        binding.detailActivoMembership.isVisible= user!!.membership
    }


}