package com.tecfit.gym_android_adm.fragments.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.activities.utilities.ForMessages
import com.tecfit.gym_android_adm.fragments.ListTrainersFragment
import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.models.custom.SelectedClass


class TrainerViewHolder(val view: View, val bottomSheetDialoUpdate: BottomSheetDialog,val bottomSheetDialogDelete: BottomSheetDialog ):RecyclerView.ViewHolder(view) {
    val tr_image = view.findViewById<ImageView>(R.id.item_trainer_image)
    val tr_name = view.findViewById<TextView>(R.id.text_name_trainner)
    val tr_description = view.findViewById<TextView>(R.id.text_description_trainner)
    val tr_linear = view.findViewById<TextView>(R.id.text_description_trainner)
    val tr_delete = view.findViewById<LinearLayout>(R.id.btn_delete_trainer)


    fun render(trainer: Trainer){

        Glide.with(view.context).load(trainer.file.url).into(tr_image)
        tr_name.text=trainer.name + " " + trainer.lastname
        tr_description.text=trainer.description

        tr_delete.setOnClickListener{
            SelectedClass.trainerSelected = trainer
            bottomSheetDialogDelete.show()
        }

        tr_linear.setOnClickListener {
            SelectedClass.trainerSelected = trainer
            bottomSheetDialoUpdate.show()
        }


    }



}

