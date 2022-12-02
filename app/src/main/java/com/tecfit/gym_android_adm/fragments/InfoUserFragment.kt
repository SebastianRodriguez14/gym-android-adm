package com.tecfit.gym_android_adm.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.databinding.FragmentDetailsUserBinding
import com.tecfit.gym_android_adm.databinding.FragmentInfoUserBinding
import com.tecfit.gym_android_adm.models.User
import com.tecfit.gym_android_adm.models.UserInAppCustom.Companion.user
import com.tecfit.gym_android_adm.models.custom.SelectedClass
import com.tecfit.gym_android_adm.models.Membership
import com.tecfit.gym_android_adm.models.UserInAppCustom
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit


class   InfoUserFragment : Fragment() {

    lateinit var binding: FragmentInfoUserBinding
    val activateMembershipFragment = ActivateMembershipFragment()
    lateinit var bindinDetail: FragmentDetailsUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoUserBinding.inflate(layoutInflater)
        bindinDetail= FragmentDetailsUserBinding.inflate(layoutInflater)
        infoUser()

        return binding.root
    }

    private fun infoUser() {
        user=User(
            SelectedClass.userSelected.id_user, "",
            "", SelectedClass.userSelected.name, SelectedClass.userSelected.lastname,
            SelectedClass.userSelected.phone, SelectedClass.userSelected.membership, SelectedClass.userSelected.image)

        binding.infoUserPhone.text= user!!.phone
        membershipUser(user!!.id_user)
    }

    private fun membershipUser(id_user:Int){
        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultMembership: Call<Membership> = apiService.getActiveMembershipByUser(id_user)

        resultMembership.enqueue(object : Callback<Membership> {
            override fun onResponse(call: Call<Membership>, response: Response<Membership>) {
                UserInAppCustom.membership = response.body()
                if (UserInAppCustom.membership != null) {
                    UserInAppCustom.membership!!.start_date = Date(UserInAppCustom.membership!!.start_date.time + (1000 * 60 * 60 * 24))
                    UserInAppCustom.membership!!.expiration_date = Date(UserInAppCustom.membership!!.expiration_date.time + (1000 * 60 * 60 * 24))


                } else {
                    UserInAppCustom.membership = Membership(0, Date(), Date(), 0.0)
                }
                typeMembership()
                requireParentFragment().requireActivity().findViewById<TextView>(R.id.info_user_btn_option).isVisible=true
                requireParentFragment().requireActivity().findViewById<ImageView>(R.id.detail_return_list_user).isVisible=true
            }

            override fun onFailure(call: Call<Membership>, t: Throwable) {
                println("Error: membershipUser failure.")
            }


        })
    }

    private fun typeMembership() {
        if (UserInAppCustom.membership!!.id_membership == 0){
            binding.infoUserTimeMembership.setText("Sin membresía")
            println("Sin membresía")

        } else {
            println("Con membresía")
            val currentDate = Date()

            val time_elapsed:Long = UserInAppCustom.membership!!.expiration_date.time - currentDate.time
            val unit = TimeUnit.DAYS
            val days = unit.convert(time_elapsed, TimeUnit.MILLISECONDS) //Días restantes

            val remainingDays = formatRemainingDays(days)

            binding.infoUserTimeMembership.setText(remainingDays)
        }
    }

    fun formatRemainingDays(days:Long):String {
        var remainingDays = ""
        if (days>30L){
            remainingDays += "${days/30} mes y ${days%30} días"
        } else if (days == 30L ) {
            remainingDays += "${days/30} mes"
        } else {
            remainingDays += "${days%30} días"
        }
        return remainingDays
    }
}