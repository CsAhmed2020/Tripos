package com.example.tripso.domain.util

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import com.example.tripso.R
import com.google.firebase.auth.FirebaseAuth



object Utils {
        fun showToast(context: Context?, stringResId: Int) {
            Toast.makeText(context, stringResId, Toast.LENGTH_SHORT).show()
        }

        fun getCurrentUserId(): String = FirebaseAuth.getInstance().currentUser?.uid!!


        fun validateLogIn(
            email:String,
            password:String,
            errorMessageResId: (Int) -> Unit = {}
        ):Boolean {
            var isValid=true
            if (email.isEmpty() || password.isEmpty()){
                isValid = false
                errorMessageResId.invoke(R.string.error_fill_all_fields)
            }else if (!isValidEmail(email)){
                isValid = false
                errorMessageResId.invoke(R.string.error_email_badly_formatted)
            }
            return isValid
        }

        fun validateSignUp(
            name: String,
            email: String,
            phone: String,
            password: String,
            errorMessageResId: (Int) -> Unit = {}
        ):Boolean {
            var isValid=true
            if (name.isEmpty()|| email.isEmpty() ||phone.isEmpty() || password.isEmpty()){
                errorMessageResId.invoke(R.string.error_fill_all_fields)
                isValid = false
            }else if (!isValidEmail(email)) {
                errorMessageResId.invoke(R.string.error_email_badly_formatted)
                isValid = false
            }else if (password.length < 5){
                errorMessageResId.invoke(R.string.error_invalid_password)
                isValid = false
            }else if (!isValidPhone(phone)){
                errorMessageResId.invoke(R.string.error_invalid_phone)
                isValid=false
            }
            return isValid
        }

        private fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        private fun isValidPassword(password: String?) : Boolean {
            password?.let {
                val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$"
                val passwordMatcher = Regex(passwordPattern)

                return passwordMatcher.find(password) != null
            } ?: return false
        }

        private fun isValidPhone(phone: String): Boolean{
            return Patterns.PHONE.matcher(phone).matches()
        }
    }



