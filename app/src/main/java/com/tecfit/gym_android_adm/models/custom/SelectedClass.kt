package com.tecfit.gym_android_adm.models.custom

import com.tecfit.gym_android_adm.models.Product
import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.models.User

class SelectedClass {

    companion object {
        lateinit var trainerSelected: Trainer
        lateinit var productSelected: Product
        lateinit var userSelected: User
    }
}