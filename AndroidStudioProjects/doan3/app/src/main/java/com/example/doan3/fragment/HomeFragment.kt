package com.example.doan3.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.doan3.R
import com.example.doan3.adapter.productAdapter
import com.example.doan3.databinding.FragmentHomeBinding
import com.example.doan3.model.categoryModel
import com.example.doan3.model.productModel
import com.example.doan3a.adapter.categoryAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)


        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)

        if (preference.getBoolean("isCart",false)){
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)

        }

        getCategory()
        getSliderImg()
        getProduct()

        return binding.root
    }

    private fun getSliderImg() {
        Firebase.firestore.collection("slider").document("items")
            .get().addOnSuccessListener {
               val list = it.get("sliderImgs") as ArrayList<String>

                val slideList = ArrayList<SlideModel>()
                for (img in list){
                    slideList.add(SlideModel(img, ScaleTypes.FIT))
                }
                binding.imgSlider.setImageList(slideList)
            }
    }

    private fun getProduct() {
        val list = ArrayList<productModel>()
        val db = Firebase.firestore
        val collectionRef = db.collection("products")

        collectionRef.get()
            .addOnSuccessListener { documents ->
                list.clear()
                for (doc in documents) {

                    val data = doc.toObject(productModel::class.java)

                    if (data != null) {
                        list.add(data)

                    } else {

                    }

                }


                binding.productRv.adapter = productAdapter(list)

            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }

    }

    private fun getCategory(){
        val list = ArrayList<categoryModel>()
        val db = Firebase.firestore
        val collectionRef = db.collection("categories")


        val list1 =  ArrayList<categoryModel>()

        collectionRef.get()
            .addOnSuccessListener { documents ->
                list1.clear()

                for (document in documents) {
                    Log.e("product", "convert it :${documents} ", )
                    val data = document.toObject(categoryModel::class.java)
                    if (data != null) {
                        list1.add(data)


                    } else {
                        Log.e("Firestore", "Error converting document to categoryModel")
                    }
                }



                binding.categoryRv.adapter = categoryAdapter(list1)

            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }
    }
}