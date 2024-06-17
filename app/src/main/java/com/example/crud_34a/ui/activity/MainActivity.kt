package com.example.crud_34a.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_34a.R
import com.example.crud_34a.adapter.ProductAdapter
import com.example.crud_34a.databinding.ActivityMainBinding
import com.example.crud_34a.model.ProductModel
import com.example.crud_34a.viewmodel.ProductViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding


    lateinit var productAdapter: ProductAdapter

    lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        productAdapter= ProductAdapter(this@MainActivity,ArrayList())

        productViewModel.fetchAllProducts()

        productViewModel.productList.observe(this){

        }


//        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                var id = productAdapter.getProductID(viewHolder.adapterPosition)
//                var imageName = productAdapter.getImageName(viewHolder.adapterPosition)
//                ref.child(id).removeValue().addOnCompleteListener {
//                    if(it.isSuccessful){
//
//                        storageReference.child("products").
//                        child(imageName).delete()
//
//                        Toast.makeText(applicationContext,"Deleted",Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }).attachToRecyclerView(mainBinding.recyclerView)



        mainBinding.floatingActionButton.setOnClickListener {
            var intent = Intent(this@MainActivity,
                AddProductActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}