package com.example.doan3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.doan3.R
import com.example.doan3.databinding.ActivityRegisterBinding
import com.example.doan3.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button5.setOnClickListener(){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        binding.button3.setOnClickListener(){
            validateUser()
        }
    }

    private fun validateUser() {
        if (binding.userName.text!!.isEmpty() ||binding.userNumber.text!!.isEmpty()){
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show()
        }else{
            storeData()
        }
    }

    private fun storeData() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Loading")
            .setMessage("Please wait")
            .setCancelable(false)
            .create()
        builder.show()

        val preference = this.getSharedPreferences("user", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putString("number",binding.userNumber.text.toString())
        editor.putString("name",binding.userName.text.toString())
        editor.apply()

        val data = UserModel(userName = binding.userName.text.toString(), userNumber = binding.userNumber.text.toString())


        FirebaseFirestore.getInstance().collection("users").document(binding.userNumber.text.toString())
            .set(data).addOnSuccessListener {
                Toast.makeText(this,"User registered",Toast.LENGTH_SHORT).show()
                builder.dismiss()
                openLogin()

            }
            .addOnFailureListener(){
                Toast.makeText(this,"Failed to register",Toast.LENGTH_SHORT).show()
            }
    }

    private fun openLogin() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}