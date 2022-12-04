package com.tecfit.gym_android_adm.fragments

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.activities.utilities.ForValidations
import com.tecfit.gym_android_adm.databinding.FragmentsUsersBinding
import com.tecfit.gym_android_adm.fragments.adapter.UserAdapter
import com.tecfit.gym_android_adm.models.File
import com.tecfit.gym_android_adm.models.Membership
import com.tecfit.gym_android_adm.models.User
import com.tecfit.gym_android_adm.models.UserCustom
import com.tecfit.gym_android_adm.models.custom.ArraysForClass
import com.tecfit.gym_android_adm.models.custom.MembershipRegister
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.properties.Delegates

class FilterUsers{
    companion object{
        var nameUser:String=""
        var availableUser:Boolean=false
        fun applyFilters(users:List<User>):List<User>{
            val filteredUsers= users.filter { user ->
                val checkNameUser = if(nameUser == "") true else user.name.lowercase().startsWith(
                    nameUser.lowercase())
                val checkAvailable= if(availableUser) user.membership else true
                checkNameUser && checkAvailable
            }
            return filteredUsers
        }
    }
}

class ListUserFragment : Fragment() {

    private lateinit var bottomSheetDialogDetails:BottomSheetDialog
    private lateinit var bottomSheetViewDetails:View
    private lateinit var recyclerView: RecyclerView

    private lateinit var listUsersLinearLayout:LinearLayout
    private lateinit var listUsersVoidLinearLayout: LinearLayout

    lateinit var binding: FragmentsUsersBinding

    private lateinit var auth: FirebaseAuth
   // private lateinit var usersList:List<User>
    private lateinit var root:View
    private lateinit var addButton:LinearLayout
    private lateinit var textOnemes:TextView
    private lateinit var textTwomes:TextView
    private lateinit var textDatemes:TextView
    private lateinit var text_selected: TextView
    private lateinit var expiry_layout: LinearLayout

    private lateinit var txtNames: EditText
    private lateinit var txtLastname: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var membership_start_date:EditText
    private lateinit var txtPayment:EditText
    private lateinit var switch: SwitchMaterial

    private lateinit var errorName: TextView
    private lateinit var errorLastname: TextView
    private lateinit var errorPhone: TextView
    private lateinit var errorEmail: TextView
    private lateinit var errorPassword: TextView
    private lateinit var errorPayment: TextView

    private lateinit var start_date: String
    private lateinit var expiry_date: String
    private var payment by Delegates.notNull<Double>()

    private lateinit var inputNameUser : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root=inflater.inflate(R.layout.fragments_users,container,false)
        recyclerView=root.findViewById(R.id.recyclerview_users)
        binding = FragmentsUsersBinding.inflate(layoutInflater)
        apiGetUsers()
        createDetailsDialog()
        addButton=root.findViewById(R.id.btn_add_user)

        val gridLayoutManager= GridLayoutManager(root.context,1)
        gridLayoutManager.widthMode

        recyclerView.layoutManager=gridLayoutManager
        listUsersLinearLayout= root.findViewById(R.id.users_list_linear)
        listUsersVoidLinearLayout=root.findViewById(R.id.users_list_void_linear)

        if(ArraysForClass.arrayUsers == null) {
            apiGetUsers()
        }else{
            setArrayForRecycler()
        }

        switch=root.findViewById(R.id.users_switch)
        switch.setOnCheckedChangeListener { buttonView, isChecked->
            if(ArraysForClass.arrayUsers!=null){
                FilterUsers.availableUser= isChecked
                setArrayForRecycler(true)
            }
        }

        inputNameUser=root.findViewById(R.id.user_input_name)
        inputNameUser.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (ArraysForClass.arrayUsers!= null){
                    FilterUsers.nameUser=s.toString()
                    setArrayForRecycler(true)
                }
            }
        })

        addButton.setOnClickListener{
            val bottomSheetDialog = BottomSheetDialog(
                requireActivity(), R.style.BottonSheetDialog
            )

        val bottomSheetView=layoutInflater.inflate(R.layout.bottom_sheet_dialog_user,null)
            textOnemes = bottomSheetView.findViewById(R.id.text_onemes)
            textTwomes = bottomSheetView.findViewById(R.id.text_twomes)
            textDatemes = bottomSheetView.findViewById(R.id.text_datemes)
            expiry_layout = bottomSheetView.findViewById(R.id.expiry_date_layout)

            //Init Inputs
            txtNames = bottomSheetView.findViewById(R.id.txt_names)
            txtLastname = bottomSheetView.findViewById(R.id.txt_lastname)
            txtPhone = bottomSheetView.findViewById(R.id.txt_phone)
            txtEmail = bottomSheetView.findViewById(R.id.txt_email)
            txtPassword = bottomSheetView.findViewById(R.id.txt_password)
            txtPayment = bottomSheetView.findViewById(R.id.txt_payment)
            membership_start_date = bottomSheetView.findViewById(R.id.membership_start_date)

            //Init Errors
            errorName = bottomSheetView.findViewById(R.id.names_error)
            errorLastname = bottomSheetView.findViewById(R.id.lastname_error)
            errorPhone = bottomSheetView.findViewById(R.id.phone_error)
            errorEmail = bottomSheetView.findViewById(R.id.email_error)
            errorPassword = bottomSheetView.findViewById(R.id.password_error)
            errorPayment = bottomSheetView.findViewById(R.id.payment_error)

            val arrayOptions= arrayOf<TextView>(textOnemes,textTwomes,textDatemes)

            start_date = LocalDate.now().toString()
            text_selected = textOnemes

            setBackgroundSelected(arrayOptions, textOnemes)

            textOnemes.setOnClickListener{ setBackgroundSelected(arrayOptions, textOnemes) }

            textTwomes.setOnClickListener{ setBackgroundSelected(arrayOptions, textTwomes) }

            textDatemes.setOnClickListener{ setBackgroundSelected(arrayOptions, textDatemes) }

            bottomSheetView.findViewById<View>(R.id.membership_start_date).setOnClickListener{
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
                    membership_start_date.setText(dst)

                    setBackgroundSelected(arrayOptions, textOnemes)
                }

            }

            bottomSheetView.findViewById<View>(R.id.btn_register_user).setOnClickListener {
                if(validationDate() && validationRegister()){
                    binding.btnAddUser.background.alpha = 60
                    binding.btnAddUser.isEnabled = false
                    binding.btnAddUser.isEnabled = false
                    registerUserFirebase(txtEmail.text.toString(), txtPassword.text.toString())
                    registerUser()
                    bottomSheetDialog.dismiss()

                }
            }

            bottomSheetView.findViewById<View>(R.id.btn_register_user_cancel).setOnClickListener{
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()


        }
        // Aquï acaba la llamada al modal
        auth = FirebaseAuth.getInstance()
        return root
    }
    private fun createDetailsDialog() {
        bottomSheetDialogDetails = BottomSheetDialog(requireActivity(), R.style.BottonSheetDialog)

        bottomSheetViewDetails =
            layoutInflater.inflate(R.layout.bottom_sheet_dialog_user_details, null)

        bottomSheetDialogDetails.setContentView(bottomSheetViewDetails)
    }

    private fun setArrayForRecycler(filter:Boolean = false) {
        var users = if (!filter) ArraysForClass.arrayUsers!! else FilterUsers.applyFilters(
            ArraysForClass.arrayUsers!!
        )
        recyclerView.adapter=UserAdapter(users, fragmentManager )

        listUsersLinearLayout.isVisible = users.isNotEmpty()
        listUsersVoidLinearLayout.isVisible = users.isEmpty()
    }


//    private fun initRecyclerView(id:Int){
//       val recyclerView = root.findViewById<RecyclerView>(id)
//
//        recyclerView.layoutManager = LinearLayoutManager(root.context)
//
//        recyclerView.adapter=UserAdapter(usersList, fragmentManager )
//
//        setArrayForRecycler(false)
//    }

    private fun apiGetUsers(){
        val apiService:ApiService= RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultUsers:Call<List<User>> = apiService.getUsers()

        resultUsers.enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>,response: Response<List<User>>) {
                val listUsers=response.body()
                if(listUsers!=null){
                    //usersList=listUsers.filter { u -> u.email != "gimnasiotecfit2022@gmail.com" }
                   //initRecyclerView(R.id.recyclerview_users)
                    ArraysForClass.arrayUsers= listUsers
                    setArrayForRecycler()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                println("Error:getUsers() failure")
                apiGetUsers()
                println(t.message)
            }
        })
    }

    private fun registerUser(){

        if(txtPayment.isEnabled){
            payment = txtPayment.text.toString().toDouble()
        }

         val membership = MembershipRegister(start_date, expiry_date,payment, UserCustom(txtEmail.text.toString(),
         txtPassword.text.toString(),txtNames.text.toString(), txtLastname.text.toString(), txtPhone.text.toString(),
         File("https://res.cloudinary.com/dfh14vom7/image/upload/v1668617884/xsdguuvlt17mzub3dzkj.png", 22)))

        val apiService:ApiService= RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        apiService.saveMembershipWithUser(membership).enqueue(
            object : Callback<Membership>{
                override fun onFailure(call: Call<Membership>, t: Throwable) {
                    Toast.makeText(root.context, "No se pudo registrar al usuario", Toast.LENGTH_SHORT).show()

                }

                override fun onResponse(call: Call<Membership>, response: Response<Membership>) {
                    val addedUser = response.body()
                    MotionToast.createColorToast(requireActivity(), "Usuario Registrado",
                        "Se registró correctamente", MotionToastStyle.SUCCESS, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, null )
                    println(addedUser.toString())
                    binding.btnAddUser.background.alpha = 255
                    binding.btnAddUser.isEnabled = true
                    binding.btnAddUser.isEnabled = true
                    apiGetUsers()
                }
            }
        )
    }

    private fun registerUserFirebase(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            if (it.isSuccessful) {
                auth.currentUser?.sendEmailVerification()
               //registerUser()
            } else {
                println(it)
                Toast.makeText(root.context, "Ya existe una cuenta con este correo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validationRegister():Boolean{
        val checks = arrayOf(
            ForValidations.valInput(txtNames, errorName, ForValidations::valOnlyText),
            ForValidations.valInput(txtLastname, errorLastname, ForValidations::valOnlyText),
            ForValidations.valInput(txtPhone, errorPhone, ForValidations::valOnlyPhone),
            ForValidations.valInput(txtEmail, errorEmail, ForValidations::valOnlyEmail),
            ForValidations.valInput(txtPassword, errorPassword, ForValidations::valOnlyPassword)
        )

        return !checks.contains(true)
    }

    private fun validationDate():Boolean{
        var isPass: Boolean

        if(membership_start_date.text.isEmpty()){
            membership_start_date.setBackgroundResource(R.drawable.shape_input_error)
            isPass = false
        } else{
            membership_start_date.setBackgroundResource(R.drawable.shape_input)
            if(txtPayment.isEnabled && txtPayment.text.isEmpty()){
                errorPayment.visibility = View.VISIBLE
                isPass = false
            } else{
                errorPayment.visibility = View.INVISIBLE
                isPass = true
            }
        }

        return isPass
    }

    private fun setBackgroundSelected(arrayTextView: Array<TextView>,text:TextView){
        for(textview in arrayTextView){
            if (textview == text){
                textview.setBackgroundResource(R.drawable.shape_info_page_option_selected)

                text_selected = text

                when(text){
                    textOnemes->{
                        expiry_layout.visibility = View.INVISIBLE
                        txtPayment.isEnabled = false
                        payment = 80.0

                        val c = Calendar.getInstance()
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                        c.time = sdf.parse(start_date) as Date
                        c.add(Calendar.MONTH, 1)
                        expiry_date = sdf.format(c.time)
                        println("expiry: $expiry_date")
                        textDatemes.text = "Fecha Personalizada"
                    }
                    textTwomes->{

                        payment = 150.0
                        expiry_layout.visibility = View.INVISIBLE
                        txtPayment.isEnabled = false

                        val c = Calendar.getInstance()
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        c.time = sdf.parse(start_date) as Date
                        c.add(Calendar.MONTH, 3)
                        expiry_date = sdf.format(c.time)
                        println(expiry_date)
                        textDatemes.text = "Fecha Personalizada"
                    }
                    textDatemes-> {
                        txtPayment.isEnabled = true
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
                            println(dft)
                            textDatemes.text = dft
                        }
                        expiry_layout.visibility = View.VISIBLE
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

