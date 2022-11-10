package com.tecfit.gym_android_adm.fragments.adapter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.fragments.ListTrainersFragment
import com.tecfit.gym_android_adm.models.Trainer


class TrainerViewHolder(val view: View, val listTrainersFragment: ListTrainersFragment):RecyclerView.ViewHolder(view) {
    val tr_image = view.findViewById<ImageView>(R.id.item_trainer_image)
    val tr_name = view.findViewById<TextView>(R.id.text_name_trainner)
    val tr_description = view.findViewById<TextView>(R.id.text_description_trainner)
    val tr_linear = view.findViewById<LinearLayout>(R.id.item_trainer_linear)
    val REQUEST_GALERY = 1001

    fun render(trainer: Trainer){
        Glide.with(view.context).load(trainer.file.url).into(tr_image)
        tr_name.text=trainer.name + " " + trainer.lastname
        tr_description.text=trainer.description
        tr_linear.setOnClickListener {
            openUpdateDialog(trainer)
        }
    }

    private fun openUpdateDialog(trainer:Trainer) {
        val bottomSheetDialog = BottomSheetDialog(listTrainersFragment.requireActivity(), R.style.BottonSheetDialog)

        val bottomSheetView = listTrainersFragment.layoutInflater.inflate(R.layout.bottom_sheet_dialog_trainer_update, null)

        bottomSheetView.findViewById<TextView>(R.id.update_trainer_name).text = trainer.name
        bottomSheetView.findViewById<TextView>(R.id.update_trainer_lastname).text = trainer.lastname
        bottomSheetView.findViewById<TextView>(R.id.update_trainer_description).text = trainer.description
        Glide.with(view.context).load(trainer.file.url).into(bottomSheetView.findViewById(R.id.update_trainer_image))

        bottomSheetView.findViewById<TextView>(R.id.update_trainer_cancel).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<TextView>(R.id.update_trainer_save).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<TextView>(R.id.update_trainer_image_button).setOnClickListener {
            openGaleryClickListener()
        }

        bottomSheetDialog.setContentView(bottomSheetView)


        bottomSheetDialog.show()

    }

    private fun openGaleryClickListener() {
        //Verificación de la versión de android
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(listTrainersFragment.requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)  ==
                PackageManager.PERMISSION_DENIED){
                // Si no tiene permisos, lo pedimos
                val filePermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                listTrainersFragment.requireActivity().requestPermissions(filePermission, REQUEST_GALERY)
            } else {
                // Sí tiene permisos
                openGalery()
            }
        } else {
            // Versión de lollipop hacia abajo tienen permisos por defecto
            openGalery()
        }
    }

    private fun openGalery(){
        val intentGalery = Intent(Intent.ACTION_PICK)
        intentGalery.type = "image/*"
        listTrainersFragment.requireActivity().startActivityForResult(intentGalery, REQUEST_GALERY)
        Toast.makeText(listTrainersFragment.context, "Open galery...", Toast.LENGTH_SHORT).show()
    }

}