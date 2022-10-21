package com.tecfit.gym_android_adm.fragments

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.activities.utilities.ForValidations
import com.tecfit.gym_android_adm.fragments.adapter.TrainerAdapter
import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime
import java.util.*


class ListTrainersFragment: Fragment() {

    private lateinit var root: View
    private lateinit var trainersList: List<Trainer>
    private lateinit var addButton: LinearLayout

    lateinit var Img: ImageView
    lateinit var nam: EditText
    lateinit var lastnam: EditText
    lateinit var descri: EditText
    lateinit var errorname: TextView
    lateinit var errorlastname: TextView
    lateinit var errordescription: TextView
    lateinit var errorimg: TextView

    lateinit var bitmap:Bitmap
    var valor =1001
    var rutaimagen: Uri? = null
    lateinit var f: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_trainers, container, false)
        apiGetTrainers()
        addButton = root.findViewById(R.id.btn_add_trainer)


        addButton.setOnClickListener {

            val bottomSheetDialog = BottomSheetDialog(
                requireActivity(), R.style.BottonSheetDialog
            )

            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

            Img = bottomSheetView.findViewById(R.id.register_trainer_img)
            nam = bottomSheetView.findViewById(R.id.register_trainer_input_name)
            lastnam = bottomSheetView.findViewById(R.id.register_trainer_input_lastname)
            descri = bottomSheetView.findViewById(R.id.register_trainer_input_description)
            errorname = bottomSheetView.findViewById(R.id.trainer_error_name)
            errorlastname = bottomSheetView.findViewById(R.id.trainer_error_lastname)
            errordescription = bottomSheetView.findViewById(R.id.trainer_error_description)
            errorimg = bottomSheetView.findViewById(R.id.trainer_error_img)


            bottomSheetView.findViewById<View>(R.id.register_trainer_btn_review)
                .setOnClickListener {
                    this.cargarImagen();
                }

            //boton register
            bottomSheetView.findViewById<View>(R.id.register_trainer_btn_register)
                .setOnClickListener {
                    if (checkInputCompleted()) {
                        registerTrainer()

                        bottomSheetDialog.dismiss()
                        MotionToast.createColorToast(
                            requireActivity(),
                            "Entrenador Registrado",
                            "Se registró correctamente",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            null
                        )
                    }


                }

            //info image
            bottomSheetView.findViewById<View>(R.id.image_tooltip).setOnClickListener {
                MotionToast.createColorToast(
                    requireActivity(),
                    "Imagen del Entrenador",
                    "El formato debe ser PNG\nImagen una tanto cuadrada",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    null
                )

            }

            bottomSheetDialog.setContentView(bottomSheetView)
            //bottomSheetDialog.setCancelable(true)
            bottomSheetDialog.show()

            //boton cancel
            bottomSheetView.findViewById<View>(R.id.register_trainer_btn_cancel)
                .setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

            /*
            val dialog = BottomSheetDialog(root.context, R.style.BottonSheetDialog)
            val vista = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
             */

        }

        return root
    }

    private fun checkInputCompleted(): Boolean {
        val checks = arrayOf(
            ForValidations.valInput(nam, errorname, ForValidations::valOnlyText),
            ForValidations.valInput(lastnam, errorlastname, ForValidations::valOnlyText),
            ForValidations.valInput(descri, errordescription, ForValidations::valOnlyText),
        )
        return !checks.contains(true)
    }

    private fun initRecyclerView(id: Int) {
        val recyclerView = root.findViewById<RecyclerView>(id)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.adapter = TrainerAdapter(trainersList)
    }

    private fun apiGetTrainers() {
        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val resultTrainers: Call<List<Trainer>> = apiService.getTrainers()

        resultTrainers.enqueue(object : Callback<List<Trainer>> {
            override fun onResponse(call: Call<List<Trainer>>, response: Response<List<Trainer>>) {
                val listTrainers = response.body()
                if (listTrainers != null) {
                    trainersList = listTrainers
                    initRecyclerView(R.id.recyclerview_trainers)
                }
            }

            override fun onFailure(call: Call<List<Trainer>>, t: Throwable) {
                println("Error: getTrainers() failure")
                println(t.message)
            }
        })

    }

    private fun cargarImagen() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        i.setType("image/")
        startActivityForResult(Intent.createChooser(i, "Selecciona una aplicación"), valor)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode === RESULT_OK && requestCode == valor) {
            try{
                rutaimagen=data?.data
                Img.setImageURI(rutaimagen)


            }catch (e:FileNotFoundException){
                e.printStackTrace()
            }

        }

    }


    private fun registerTrainer(){
        apiPostTrainer()
    }


    private fun apiPostTrainer(){
        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)


        val fechahora = LocalDateTime.now()
        val valores = ContentValues()
        valores.put(MediaStore.Images.Media.DISPLAY_NAME,"image"+fechahora+".png")
        valores.put(MediaStore.Images.Media.MIME_TYPE,"image/png")

//        val bitm=Bitmap.createBitmap(64,64,Bitmap.Config.ARGB_8888)

        val trainer = Trainer(0,nam.text.toString(),
                                lastnam.text.toString(),
                                descri.text.toString(),
                                file = com.tecfit.gym_android_adm.models.File("dh","juhio"))
        println(trainer)

        val resulTrainer:Call<Trainer> = apiService.postTrainer(trainer)

        resulTrainer.enqueue(object : Callback<Trainer>{
            override fun onResponse(call: Call<Trainer>, response: Response<Trainer>) {
                println(response.body())
            }

            override fun onFailure(call: Call<Trainer>, t: Throwable) {
               println("ERROR: postTrainer()failure")
            }
        })
    }

}


