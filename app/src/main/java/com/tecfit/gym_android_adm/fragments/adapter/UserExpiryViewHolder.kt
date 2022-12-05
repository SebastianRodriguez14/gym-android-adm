package com.tecfit.gym_android_adm.fragments.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.activities.utilities.ForFragments
import com.tecfit.gym_android_adm.fragments.DetailsUserFragment
import com.tecfit.gym_android_adm.models.custom.SelectedClass
import com.tecfit.gym_android_adm.models.custom.UserToFinish
import java.util.*
import java.util.concurrent.TimeUnit

class UserExpiryViewHolder(val view:View, val manager: FragmentManager?) : RecyclerView.ViewHolder(view) {
    val us_ex_image = view.findViewById<ImageView>(R.id.item_user_expiry_image)
    val us_ex_name = view.findViewById<TextView>(R.id.item_user_expiry_name)
    val us_ex_time = view.findViewById<TextView>(R.id.item_user_expiry_time)
    val us_ex_layout = view.findViewById<LinearLayout>(R.id.item_user_expiry)

    fun render(userToFinish: UserToFinish){
        userToFinish.start_date = Date(userToFinish.start_date.time + (1000 * 60 * 60 * 24))
        userToFinish.expiration_date = Date(userToFinish.expiration_date.time + (1000 * 60 * 60 * 24))
        println(userToFinish)
        if (userToFinish.user.image != null){
            Glide.with(view.context).load(userToFinish.user.image.url).into(us_ex_image)
        }
        us_ex_name.text = "${userToFinish.user.name} ${userToFinish.user.lastname}"
        us_ex_time.text = getRemainingDays(userToFinish.expiration_date)

        val detailsUserFragment= DetailsUserFragment()

        us_ex_layout.setOnClickListener{
            SelectedClass.userSelected= userToFinish.user
            ForFragments.replaceInFragment(detailsUserFragment,manager)
            //bottomSheetDialogDetails.show()
        }
    }

    private fun getRemainingDays(expiration_date:Date): String {

        val currentDate = Date()
        val time_elapsed:Long = expiration_date.time - currentDate.time

        val unit = TimeUnit.DAYS

        val remainingDays = unit.convert(time_elapsed, TimeUnit.MILLISECONDS).toInt() + 1//Días restantes

        return if (remainingDays == 1) {
            "1 día restante"
        } else {
            "${remainingDays} días restantes"
        }
    }


}