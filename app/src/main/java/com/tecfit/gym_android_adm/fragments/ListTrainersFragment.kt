package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.fragments.adapter.TrainerAdapter
import com.tecfit.gym_android_adm.models.Trainer
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root=inflater.inflate(R.layout.fragment_trainers,container,false)
        apiGetTrainers()
        addButton = root.findViewById(R.id.btn_add_trainer)

        addButton.setOnClickListener{

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



        return root
    }





    private fun initRecyclerView(id:Int){
        val recyclerView=root.findViewById<RecyclerView>(id)
        recyclerView.layoutManager=LinearLayoutManager(root.context)
        recyclerView.adapter= TrainerAdapter(trainersList)
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
}