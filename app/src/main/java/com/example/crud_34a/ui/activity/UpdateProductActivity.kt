package com.example.crud_34a.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34a.R
import com.example.crud_34a.databinding.ActivityUpdateProductBinding
import com.example.crud_34a.model.ProductModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class UpdateProductActivity : AppCompatActivity() {
    lateinit var updateProductBinding: ActivityUpdateProductBinding
    var id = ""
    var imageName = ""
    var firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref : DatabaseReference = firebaseDatabase.reference.child("products")
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null

    var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageReference: StorageReference = firebaseStorage.reference

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateProductBinding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(updateProductBinding.root)
        registerActivityForResult()

        var product : ProductModel? = intent.getParcelableExtra("product")

        updateProductBinding.editTextNameUpdate.setText(product?.productName)
        updateProductBinding.editTextPriceUpdate.setText(product?.productPrice.toString())
        updateProductBinding.editTextDescUpdate.setText(product?.productDesc)

        Picasso.get().load(product?.url).into(updateProductBinding.imageUpdate)


        id = product?.id.toString()
        imageName = product?.imageName.toString()

        updateProductBinding.buttonUpdate.setOnClickListener {
            uploadImage()
        }

        updateProductBinding.imageUpdate.setOnClickListener {
            var permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                android.Manifest.permission.READ_MEDIA_IMAGES
            } else {
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    permissions
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(permissions), 1)
            } else {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                activityResultLauncher.launch(intent)
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun uploadImage(){

        var imageReference = storageReference.child("products").
        child(imageName)

        imageUri?.let {url->
            imageReference.putFile(url).addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener {url->
                    var imageUrl =  url.toString()
                    updateProduct(imageUrl)
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext,
                    it.localizedMessage,
                    Toast.LENGTH_LONG).show()
            }
        }


    }

    fun updateProduct(url : String){
        var updatedName : String= updateProductBinding.editTextNameUpdate.text.toString()
        var updatedPrice: Int = updateProductBinding.editTextPriceUpdate.text.toString().toInt()
        var updatedDesc : String = updateProductBinding.editTextDescUpdate.text.toString()

        var updatedMap = mutableMapOf<String,Any>()
        updatedMap["productName"] = updatedName
        updatedMap["productPrice"] = updatedPrice
        updatedMap["productDesc"] = updatedDesc
        updatedMap["id"] = id
        updatedMap["url"] = url

        ref.child(id).updateChildren(updatedMap).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(applicationContext,"Data updated"
                    ,Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(applicationContext,it.exception?.message
                    ,Toast.LENGTH_LONG).show()
            }
        }
    }
    fun registerActivityForResult() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                val resultcode = result.resultCode
                val imageData = result.data
                if (resultcode == RESULT_OK && imageData != null) {
                    imageUri = imageData.data
                    imageUri?.let {
                        Picasso.get().load(it).into(updateProductBinding.imageUpdate)
                    }
                }

            })
    }
}