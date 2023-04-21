package com.example.doan3.activity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.doan3.MainActivity
import com.example.doan3.R
import com.example.doan3.databinding.ActivityOtpactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.MainScope

class OTPActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOtpactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button3.setOnClickListener(){
            if (binding.userOTP.text!!.toString().isEmpty()){
                Toast.makeText(this,"Please provide OTP",Toast.LENGTH_SHORT).show()

            }else{
                verifyOTP(binding.userOTP.text!!.toString())
            }
        }
    }

    private fun verifyOTP(OTP: String) {
        val credential = PhoneAuthProvider.getCredential(intent.getStringExtra("verificationID")!!, OTP)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val preference = this.getSharedPreferences("user", MODE_PRIVATE)
                    val editor = preference.edit()
                    editor.putString("number",intent.getStringExtra("number")!!)

                    editor.apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {

                   Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }
    }

}