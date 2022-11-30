package com.tecfit.gym_android_adm.activities.utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tecfit.gym_android_adm.R

class ForFragments {

    companion object{
        fun replaceFragment(fragmentManager: FragmentManager, container:Int, fragment: Fragment){
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(container, fragment)
            fragmentTransaction.commit()
        }

        fun replaceInFragment(fragment: Fragment, fragmentManager:FragmentManager?) {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, fragment)?.commit()
        }
    }

}