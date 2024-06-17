package com.example.crud_34a.repository

import android.net.Uri
import com.example.crud_34a.model.ProductModel

interface ProductRepository {
    fun addProducts(productModel: ProductModel,callback:(Boolean,String?)-> Unit)
    fun uploadImages(imageUri : Uri,callback:(Boolean,String?,String?,String?)-> Unit)

    fun getAllProducts(callback: (List<ProductModel>?, Boolean,
                                  String?) -> Unit)
    fun updateProducts(id:String,data: MutableMap<String,Any>?,
                       callback: (Boolean, String?) -> Unit)
    fun deleteProducts(id: String,callback: (Boolean, String?) -> Unit)
    fun deleteImage(imageName: String,callback: (Boolean, String?) -> Unit)
}

