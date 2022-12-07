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
import com.tecfit.gym_android_adm.activities.utilities.ForValidations
import com.tecfit.gym_android_adm.databinding.FragmentDetailsUserBinding
import com.tecfit.gym_android_adm.databinding.FragmentExtendMembershipBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.properties.Delegates

class ExtendMembershipFragment: Fragment() {

    lateinit var binding: FragmentExtendMembershipBinding
    private lateinit var text_selected: TextView
    private lateinit var start_date: String
    private lateinit var expiry_date: String
    private var payment by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentExtendMembershipBinding.inflate(layoutInflater)
        val arrayOptions= arrayOf<TextView>(binding.extendTextOnemes,binding.extendTextTwomes,binding.extendTextDatemes)
        start_date = LocalDate.now().toString()
        text_selected = binding.extendTextOnemes
        setBackgroundSelected(arrayOptions, binding.extendTextOnemes)
        requireParentFragment().requireActivity().findViewById<TextView>(R.id.info_user_btn_option).isVisible=false

        binding.extendTextOnemes.setOnClickListener{ setBackgroundSelected(arrayOptions, binding.extendTextOnemes)
        }
        binding.extendTextTwomes.setOnClickListener{ setBackgroundSelected(arrayOptions, binding.extendTextTwomes)
        }
        binding.extendTextDatemes.setOnClickListener{ setBackgroundSelected(arrayOptions, binding.extendTextDatemes)
        }

        binding.btnExtendMembership.setOnClickListener {
            if(validationDate()){
                ForFragments.replaceFragment(parentFragmentManager,R.id.frame_details_user, InfoUserFragment())
                binding.btnExtendMembership.background.alpha = 60
                binding.btnExtendMembership.isEnabled = false
                binding.btnExtendMembershipCancel.isEnabled = false
            }
        }

        binding.btnExtendMembershipCancel.setOnClickListener {
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
                    binding.extendTextOnemes->{
                        binding.expiryDateLayout.visibility = View.INVISIBLE
                        binding.extendTxtPayment.isEnabled = false
                        payment = 80.0

                        val c = Calendar.getInstance()
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        c.time = sdf.parse(start_date) as Date
                        c.add(Calendar.MONTH, 1)
                        expiry_date = sdf.format(c.time)
//                        println("expiry: $expiry_date")
                        binding.extendTextDatemes.text = "Fecha Personalizada"
                    }
                    binding.extendTextTwomes->{

                        payment = 150.0
                        binding.expiryDateLayout.visibility = View.INVISIBLE
                        binding.extendTxtPayment.isEnabled = false

                        val c = Calendar.getInstance()
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        c.time = sdf.parse(start_date) as Date
                        c.add(Calendar.MONTH, 3)
                        expiry_date = sdf.format(c.time)
//                        println("expiry: $expiry_date")
                        binding.extendTextDatemes.text = "Fecha Personalizada"
                    }
                    binding.extendTextDatemes-> {
                        binding.extendTxtPayment.isEnabled = true
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
//                            println("expiry: $expiry_date")
                            binding.extendTextDatemes.text = dft
                        }
                        binding.expiryDateLayout.visibility = View.VISIBLE
                    }
                }
            }
            else{
                textview.setBackgroundResource(R.drawable.shape_info_page_option)
            }
//            println(textview.text.toString() + " - " + textview.id)
        }
        println("${text_selected.text.toString()} - ${expiry_date}")

    }

    private fun validationDate():Boolean{
        val isPass: Boolean
            if(binding.extendTxtPayment.isEnabled && ForValidations.valInput(binding.extendTxtPayment, binding.paymentError, ForValidations::valPrice)){
                isPass = false
            } else {
                binding.paymentError.visibility = View.INVISIBLE
                isPass = true
            }

        return isPass
    }



}