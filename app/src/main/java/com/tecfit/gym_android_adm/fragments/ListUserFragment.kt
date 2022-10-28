package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.fragments.adapter.UserAdapter
import com.tecfit.gym_android_adm.models.User
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI.create


class ListUserFragment : Fragment() {

    private lateinit var usersList:List<User>
    private lateinit var root:View
    private lateinit var addButton:LinearLayout
    private lateinit var textOnemes:TextView
    private lateinit var textTwomes:TextView
    private lateinit var textDatemes:TextView
    private lateinit var text_selected: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root=inflater.inflate(R.layout.fragments_users,container,false)
        apiGetUsers()

        addButton=root.findViewById(R.id.btn_add_user)

        addButton.setOnClickListener{
            val bottomSheetDialog = BottomSheetDialog(
                requireActivity(), R.style.BottonSheetDialog
            )

        val bottomSheetView=layoutInflater.inflate(R.layout.bottom_sheet_dialog_user,null)
            textOnemes = bottomSheetView.findViewById(R.id.text_onemes)
            textTwomes = bottomSheetView.findViewById(R.id.text_twomes)
            textDatemes = bottomSheetView.findViewById(R.id.text_datemes)

            val arrayOptions= arrayOf<TextView>(textOnemes,textTwomes,textDatemes)

            text_selected = textOnemes
            setBackgroundSelected(arrayOptions,textOnemes)

        bottomSheetView.findViewById<View>(R.id.btn_register_user).setOnClickListener {
            bottomSheetDialog.dismiss()
//            MotionToast.createColorToast(requireActivity(), "Usuario Registrado",
//                "Se registró correctamente", MotionToastStyle.SUCCESS, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, null )

        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        }
        return root
    }


    private fun initRecyclerView(id:Int){
        val recyclerView = root.findViewById<RecyclerView>(id)

        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.adapter=UserAdapter(usersList)
    }

    private fun apiGetUsers(){
        val apiService:ApiService= RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultUsers:Call<List<User>> = apiService.getUsers()

        resultUsers.enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>,response: Response<List<User>>) {
                val listUsers=response.body()
                if(listUsers!=null){
                    usersList=listUsers
                    initRecyclerView(R.id.recyclerview_users)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                println("Error:getUsers() failure")
                println(t.message)
            }
        })
    }

    private fun setBackgroundSelected(arrayTextView: Array<TextView>,text:TextView){
        for(textview in arrayTextView){
            if (textview == text){
                textview.setBackgroundResource(R.drawable.shape_info_page_option_selected)

                text_selected = text

                when(text){
                    textOnemes->{

                    }
                    textTwomes->{

                    }
                    textDatemes-> {

                        val dateRangePicker =
                            MaterialDatePicker.Builder.dateRangePicker()
                                .setTitleText("Select dates")
                                .setSelection(
                                    Pair(
                                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                                        MaterialDatePicker.todayInUtcMilliseconds()
                                    )
                                )
                                .build()

                        dateRangePicker.show(supportFragmentManager,"date_picker")
                    }
                }
            }
            else{
                textview.setBackgroundResource(R.drawable.shape_info_page_option)
            }
            println(textview.text.toString() + " - " + textview.id)
        }
    }

}

