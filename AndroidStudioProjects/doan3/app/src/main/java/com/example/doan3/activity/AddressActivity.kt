package com.example.doan3.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.doan3.databinding.ActivityAddressBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddressActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddressBinding
    private lateinit var preference: SharedPreferences
    private lateinit var totalCost:String
    private lateinit var productIDs: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference = this.getSharedPreferences("user", MODE_PRIVATE)
        totalCost = intent.getStringExtra("totalCost")!!
        productIDs = intent.getStringArrayListExtra("productIDs")!!
        loadUserInfo()
        binding.proceed.setOnClickListener{
            validateData(

                binding.userNumber.text.toString(),
                binding.userName.text.toString(),
                binding.userAddress.text.toString(),
            )
        }
    }

    private fun validateData(number: String, name: String, address: String) {
        if (number.isEmpty() || name.isEmpty() || address.isEmpty()){
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show()
        }else{
            storeData(address)
        }
    }

    private fun storeData(address: String) {

        val map = hashMapOf<String,Any>()
        map["address"] = address

        FirebaseFirestore.getInstance().collection("users")
            .document(preference.getString("number","")!!)
            .update(map).addOnSuccessListener {

                val intent = Intent(this,CheckoutActivity::class.java)
                Log.e("PRICE", totalCost )
                Log.e("ID", productIDs.toString() )
                intent.putExtra("productIDs",productIDs)
                intent.putExtra("totalCost",totalCost)

                startActivity(intent)


                Toast.makeText(this,"Updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{

                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {

        FirebaseFirestore.getInstance().collection("users")
            .document(preference.getString("number","")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userNumber"))
                binding.userAddress.setText(it.getString("address"))
            }.addOnFailureListener{

            }
    }
}