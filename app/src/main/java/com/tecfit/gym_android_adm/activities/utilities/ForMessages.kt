package com.tecfit.gym_android_adm.activities.utilities

import androidx.fragment.app.Fragment
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class ForMessages {

    companion object {
        private fun showMotionToast(fragment: Fragment, title:String, message:String, motionToastStyle: MotionToastStyle){
            MotionToast.createColorToast(fragment.requireActivity(), title,
                message, motionToastStyle, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, null )
        }
        fun showSuccessMotionToast(fragment: Fragment, title:String, message:String){
            showMotionToast(fragment, title, message, MotionToastStyle.SUCCESS)
        }

        fun showDeleteMotionToast(fragment: Fragment, title:String, message:String){
            showMotionToast(fragment, title, message, MotionToastStyle.DELETE)
        }
        fun showErrorMotionToast(fragment: Fragment, title:String, message:String){
            showMotionToast(fragment, title, message, MotionToastStyle.ERROR)
        }
        fun showInfoMotionToast(fragment: Fragment, title:String, message:String){
            showMotionToast(fragment, title, message, MotionToastStyle.INFO)
        }

    }

}