package com.example.doan3.activity

import android.content.Intent
import android.icu.util.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.doan3.R
import com.example.doan3.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var builder: AlertDialog
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button5.setOnClickListener(){
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

        binding.button3.setOnClickListener(){
            if (binding.userNumber.text!!.isEmpty()){
                Toast.makeText(this,"Please provide number",Toast.LENGTH_SHORT).show()
            }else{
                sendOTP(binding.userNumber.text.toString())
            }
        }
    }

    private fun sendOTP(number: String) {
        builder = AlertDialog.Builder(this)
            .setTitle("Loading")
            .setMessage("Please wait")
            .setCancelable(false)
            .create()
        builder.show()
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber("+84$number")       // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {


        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            builder.dismiss()
            val intent = Intent(this@LoginActivity,OTPActivity::class.java)
            intent.putExtra("verificationID",verificationId)
            intent.putExtra("number",binding.userNumber.text.toString())
            startActivity(intent)
        }
    }
}