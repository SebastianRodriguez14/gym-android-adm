package com.tecfit.gym_android_adm.activities.utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class ForFragments {

    companion object{
        fun replaceFragment(fragmentManager: FragmentManager, container:Int, fragment: Fragment){
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(container, fragment)
            fragmentTransaction.commit()
        }
    }

}