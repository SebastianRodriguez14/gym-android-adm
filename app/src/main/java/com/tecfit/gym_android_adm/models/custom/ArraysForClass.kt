package com.tecfit.gym_android_adm.models.custom

import com.tecfit.gym_android_adm.models.Product
import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.models.User


class ArraysForClass {

    companion object{
        var arrayProducts: MutableList<Product> = mutableListOf()
        var arrayTrainers: MutableList<Trainer> = mutableListOf()
        var arrayUsers: MutableList<User> = mutableListOf()
    }

}