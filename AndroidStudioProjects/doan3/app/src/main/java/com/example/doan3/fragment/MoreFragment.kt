package com.example.doan3.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.doan3.MainActivity
import com.example.doan3.R
import com.example.doan3.activity.LoginActivity
import com.example.doan3.adapter.AllOrderAdapter
import com.example.doan3.databinding.FragmentMoreBinding
import com.example.doan3.model.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MoreFragment : Fragment() {
    private lateinit var binding:FragmentMoreBinding
    private lateinit var list : ArrayList<OrderModel>
    private lateinit var user:FirebaseAuth
    private lateinit var userID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       userID = requireContext().getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE).getString("number","")
           .toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()

        FirebaseFirestore.getInstance().collection("users").document(userID)
            .get().addOnSuccessListener{
                binding.usernameTv.text = it.getString("userName")
            }


        binding.logoutBtn.setOnClickListener {
            user.signOut()

            startActivity(Intent(requireContext(),LoginActivity::class.java))

        }



        FirebaseFirestore.getInstance().collection("orders")
            .whereEqualTo("userID",userID)
            .get().addOnSuccessListener {

                list = ArrayList()
                for (doc in it){

                    list.add(doc.toObject(OrderModel::class.java))
                }


                binding.rv.adapter = AllOrderAdapter(list,requireContext())
            }

        return binding.root
    }


}