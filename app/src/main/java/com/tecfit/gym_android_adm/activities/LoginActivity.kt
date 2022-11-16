package com.tecfit.gym_android_adm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.databinding.ActivityLoginBinding
import com.tecfit.gym_android_adm.models.User
import com.tecfit.gym_android_adm.models.UserInAppCustom
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginEnter.setOnClickListener{
            val email = binding.loginInputEmail.text.toString()
            val password = binding.loginInputPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                binding.errorMessageEmail.visibility = if(email.isEmpty()) View.VISIBLE else View.INVISIBLE
                binding.errorMessagePassword.visibility = if(password.isEmpty()) View.VISIBLE else View.INVISIBLE
            }
            else {
                if(email.equals("gimnasiotecfit2022@gmail.com") && password.equals("admin")){
                    binding.loginEnter.isEnabled = false
                    fetchUser(email)
                }else{
                    binding.errorMessagePassword.text = "Contraseña incorrecta"
                }
            }
        }
    }


    private fun fetchUser(email: String){
        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultUser: Call<User> = apiService.getUSer(email)

        resultUser.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                println("-----------------------------------------")
                println(response.body())
                UserInAppCustom.user = response.body()!!
                binding.errorMessageEmail.visibility = View.INVISIBLE
                binding.loginEnter.isEnabled = true
                reload()

            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println("-----------------------------------------")
                println("Error: postUser() failure.")
                binding.errorMessageEmail.text = "No existe una cuenta con ese correo"
                binding.errorMessageEmail.visibility = View.VISIBLE
                binding.loginEnter.isEnabled = true
            }
        })
    }

    private fun reload(){
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}