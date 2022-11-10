package com.tecfit.gym_android_adm.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.fragments.adapter.TrainerAdapter
import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.models.custom.SelectedClass
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class ListTrainersFragment: Fragment() {

    private lateinit var root:View
    private lateinit var trainersList:List<Trainer>
    private lateinit var addButton: LinearLayout
    private lateinit var btnAdd : TextView
    private val REQUEST_GALERY = 1001
    private lateinit var bottomSheetDialogUpdate:BottomSheetDialog
    private lateinit var bottomSheetViewUpdate:View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root=inflater.inflate(R.layout.fragment_trainers,container,false)
        createUpdateDialog()
        apiGetTrainers()
        addButton = root.findViewById(R.id.btn_add_trainer)

        addButton.setOnClickListener{
            openRegisterDialog()
        }
        return root
    }

    private fun initRecyclerView(id:Int){
        val recyclerView=root.findViewById<RecyclerView>(id)
        recyclerView.layoutManager=LinearLayoutManager(root.context)
        recyclerView.adapter= TrainerAdapter(trainersList, bottomSheetDialogUpdate)
    }

    private fun apiGetTrainers(){
        val apiService:ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val resultTrainers: Call<List<Trainer>> = apiService.getTrainers()

        resultTrainers.enqueue(object : Callback<List<Trainer>> {
            override fun onResponse(call: Call<List<Trainer>>, response: Response<List<Trainer>>){
                val listTrainers = response.body()
                if (listTrainers != null){
                    trainersList = listTrainers
                    initRecyclerView(R.id.recyclerview_trainers)
                }
            }
            override fun onFailure(call:Call<List<Trainer>>, t:Throwable){
                println("Error: getTrainers() failure")
                println(t.message)
            }
        })

    }

    private fun openRegisterDialog(){

        val bottomSheetDialog = BottomSheetDialog(
            requireActivity(), R.style.BottonSheetDialog
        )

        val bottomSheetView  = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

        bottomSheetView.findViewById<View>(R.id.btn_register).setOnClickListener{
            bottomSheetDialog.dismiss()
            MotionToast.createColorToast(requireActivity(), "Entrenador Registrado",
                "Se registró correctamente", MotionToastStyle.SUCCESS, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, null )

        }

        bottomSheetView.findViewById<View>(R.id.image_tooltip).setOnClickListener{
            MotionToast.createColorToast(requireActivity(), "Entrenador Registrado",
                "El formato debe ser PNG\nImagen una tanto cuadrada", MotionToastStyle.INFO, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, null )

        }

        bottomSheetDialog.setContentView(bottomSheetView)
        //bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.show()
        /*
        val dialog = BottomSheetDialog(root.context, R.style.BottonSheetDialog)
        val vista = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
         */
    }

    private fun createUpdateDialog() {
        bottomSheetDialogUpdate = BottomSheetDialog(requireActivity(), R.style.BottonSheetDialog)

        bottomSheetViewUpdate = layoutInflater.inflate(R.layout.bottom_sheet_dialog_trainer_update, null)

        bottomSheetDialogUpdate.setContentView(bottomSheetViewUpdate)

        bottomSheetDialogUpdate.setOnShowListener {
            bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_name).text = SelectedClass.trainerSelected.name
            bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_lastname).text = SelectedClass.trainerSelected.lastname
            bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_description).text = SelectedClass.trainerSelected.description
            Glide.with(this).load(SelectedClass.trainerSelected.file.url).into(bottomSheetViewUpdate.findViewById(R.id.update_trainer_image))
        }


        bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_cancel).setOnClickListener {
            bottomSheetDialogUpdate.dismiss()
        }

        bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_save).setOnClickListener {
            bottomSheetDialogUpdate.dismiss()
        }

        bottomSheetViewUpdate.findViewById<TextView>(R.id.update_trainer_image_button).setOnClickListener {
            openGaleryClickListener()
        }


    }

    private fun openGaleryClickListener() {
        //Verificación de la versión de android
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)  ==
                PackageManager.PERMISSION_DENIED){
                // Si no tiene permisos, lo pedimos
                val filePermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requireActivity().requestPermissions(filePermission, REQUEST_GALERY)
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
        startActivityForResult(intentGalery, REQUEST_GALERY)
        Toast.makeText(context, "Open galery...", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALERY){
            bottomSheetViewUpdate.findViewById<ImageView>(R.id.update_trainer_image).setImageURI(data?.data)
        }

    }

}