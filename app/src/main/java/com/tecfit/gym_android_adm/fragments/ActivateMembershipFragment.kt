package com.tecfit.gym_android_adm.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.activities.utilities.ForFragments
import com.tecfit.gym_android_adm.databinding.FragmentActivateMembershipBinding
import com.tecfit.gym_android_adm.databinding.FragmentDetailsUserBinding
import com.tecfit.gym_android_adm.models.Membership
import com.tecfit.gym_android_adm.models.User
import com.tecfit.gym_android_adm.models.custom.MembershipCustom
import com.tecfit.gym_android_adm.models.custom.SelectedClass
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.properties.Delegates

class ActivateMembershipFragment :Fragment(){

    lateinit var binding:FragmentActivateMembershipBinding
    lateinit var  user:User
    private lateinit var text_selected: TextView
    private lateinit var start_date: String
    private lateinit var expiry_date: String
    private var payment by Delegates.notNull<Double>()

    lateinit var bindinDetail: FragmentDetailsUserBinding

    val listUserFragment = ListUserFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentActivateMembershipBinding.inflate(layoutInflater)
        bindinDetail=FragmentDetailsUserBinding.inflate(layoutInflater)
        val arrayOptions= arrayOf<TextView>(binding.textOnemes,binding.textTwomes,binding.textDatemes)
        start_date = LocalDate.now().toString()
        text_selected = binding.textOnemes
        setBackgroundSelected(arrayOptions, binding.textOnemes)
        requireParentFragment().requireActivity().findViewById<TextView>(R.id.info_user_btn_option).isVisible=false

        binding.membershipStartDate.setOnClickListener{
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())

            val dateStartPicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build()
            dateStartPicker.show(childFragmentManager,"date_picker")

            dateStartPicker.addOnPositiveButtonClickListener {

                //Date picker plus 1 day
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                var dst = dateFormatter.format(Date(it))
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val c = Calendar.getInstance()
                c.time = sdf.parse(dst) as Date
                c.add(Calendar.DATE, 1)
                dst = sdf.format(c.time)
                start_date = dst
                println("start: $start_date")
                binding.membershipStartDate.setText(dst)

                setBackgroundSelected(arrayOptions, binding.textOnemes)
            }

        }


        binding.textOnemes.setOnClickListener{ setBackgroundSelected(arrayOptions, binding.textOnemes)
        }
        binding.textTwomes.setOnClickListener{ setBackgroundSelected(arrayOptions, binding.textTwomes)
        }
        binding.textDatemes.setOnClickListener{ setBackgroundSelected(arrayOptions, binding.textDatemes)
        }

        binding.btnActivateMembership.setOnClickListener{
            if(validationDate()){
                postMemership()
                binding.btnActivateMembership.background.alpha = 60
                binding.btnActivateMembership.isEnabled = false
                binding.btnActivateMembershipCancel.isEnabled = false
            }
        }
        binding.btnActivateMembershipCancel.setOnClickListener {
            ForFragments.replaceFragment(parentFragmentManager,R.id.frame_details_user, InfoUserFragment())
           requireParentFragment().requireActivity().findViewById<TextView>(R.id.info_user_btn_option).isVisible=true
        }

        return binding.root
    }
    private fun setBackgroundSelected(arrayTextView: Array<TextView>, text: TextView){
        for(textview in arrayTextView){
            if (textview == text){
                textview.setBackgroundResource(R.drawable.shape_info_page_option_selected)

                text_selected = text

                when(text){
                    binding.textOnemes->{
                        binding.expiryDateLayout.visibility = View.INVISIBLE
                        binding.txtPayment.isEnabled = false
                        payment = 80.0

                        val c = Calendar.getInstance()
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        c.time = sdf.parse(start_date) as Date
                        c.add(Calendar.MONTH, 1)
                        expiry_date = sdf.format(c.time)
                        println("expiry: $expiry_date")
                        binding.textDatemes.text = "Fecha Personalizada"
                    }
                    binding.textTwomes->{

                        payment = 150.0
                        binding.expiryDateLayout.visibility = View.INVISIBLE
                        binding.txtPayment.isEnabled = false

                        val c = Calendar.getInstance()
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        c.time = sdf.parse(start_date) as Date
                        c.add(Calendar.MONTH, 3)
                        expiry_date = sdf.format(c.time)
                        println("expiry: $expiry_date")
                        binding.textDatemes.text = "Fecha Personalizada"
                    }
                    binding.textDatemes-> {
                        binding.txtPayment.isEnabled = true
                        val constraintsBuilder =
                            CalendarConstraints.Builder()
                                .setValidator(DateValidatorPointForward.now())

                        val dateFinishPicker =
                            MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Select dates")
                                .setSelection(
                                    MaterialDatePicker.todayInUtcMilliseconds()
                                )
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()
                        dateFinishPicker.show(childFragmentManager,"date_picker")

                        dateFinishPicker.addOnPositiveButtonClickListener {
                            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            var dft = dateFormatter.format(Date(it))
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val c = Calendar.getInstance()
                            c.time = sdf.parse(dft) as Date
                            c.add(Calendar.DATE, 1)
                            dft = sdf.format(c.time)
                            expiry_date = dft
                            println("expiry: $expiry_date")
                            binding.textDatemes.text = dft
                        }
                        binding.expiryDateLayout.visibility = View.VISIBLE
                    }
                }
            }
            else{
                textview.setBackgroundResource(R.drawable.shape_info_page_option)
            }
            println(textview.text.toString() + " - " + textview.id)
        }
    }


    fun postMemership(){
        if(binding.txtPayment.isEnabled){
            payment = binding.txtPayment.text.toString().toDouble()
        }


        val membership = MembershipCustom( 0, start_date, expiry_date,payment)
        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        apiService.postMembership(SelectedClass.userSelected.id_user,membership).enqueue(
            object : Callback<Membership>{
                override fun onResponse(call: Call<Membership>, response: Response<Membership>) {
                    val membership = response.body()
                    println(membership.toString())
                    println("asdasd")
                    binding.btnActivateMembership.background.alpha = 255
                    binding.btnActivateMembership.isEnabled = true
                    binding.btnActivateMembershipCancel.isEnabled = true
                    ForFragments.replaceFragment(parentFragmentManager,R.id.frame_details_user, InfoUserFragment())

                }

                override fun onFailure(call: Call<Membership>, t: Throwable) {
                    println("Issue: Todo es culpa de Tatiana")
                }

            }
        )
    }
    private fun validationDate():Boolean{
        var isPass: Boolean

        if(binding.membershipStartDate.text.isEmpty()){
            binding.membershipStartDate.setBackgroundResource(R.drawable.shape_input_error)
            isPass = false
        } else{
            binding.membershipStartDate.setBackgroundResource(R.drawable.shape_input)
            if(binding.txtPayment.isEnabled && binding.txtPayment.text.isEmpty()){
                binding.paymentError.visibility = View.VISIBLE
                isPass = false
            } else{
                binding.paymentError.visibility = View.INVISIBLE
                isPass = true
            }
        }

        return isPass
    }
}