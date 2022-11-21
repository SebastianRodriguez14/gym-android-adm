package com.tecfit.gym_android_adm.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.activities.utilities.ForFragments
import com.tecfit.gym_android_adm.fragments.ListProductFragment
import com.tecfit.gym_android_adm.fragments.ListTrainersFragment
import com.tecfit.gym_android_adm.fragments.ListUserFragment

class MainActivity : AppCompatActivity() {

    private val listUserFragment=ListUserFragment()
    private val listTrainersFragment = ListTrainersFragment()
    private val listProductFargment=ListProductFragment()
    private lateinit var btnNavigation:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNavigation= findViewById(R.id.btnnavigation)

        btnNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.item_users -> ForFragments.replaceFragment(supportFragmentManager,R.id.frame_container, listUserFragment)
                R.id.item_trainers -> ForFragments.replaceFragment(supportFragmentManager, R.id.frame_container, listTrainersFragment)
                R.id.item_products-> ForFragments.replaceFragment(supportFragmentManager, R.id.frame_container,listProductFargment)

            }
            true
        }
    }

}