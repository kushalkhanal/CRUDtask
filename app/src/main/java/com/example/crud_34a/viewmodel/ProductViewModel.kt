package com.example.crud_34a.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crud_34a.model.ProductModel
import com.example.crud_34a.repository.ProductRepository

class ProductViewModel(val repository: ProductRepository) : ViewModel() {

    fun uploadImages(imageUri: Uri, callback: (Boolean, String?, String?,String?) -> Unit) {
        repository.uploadImages(imageUri) { success, imageUrl, imageName,message ->
            callback(success, imageUrl, imageName,message)
        }
    }
    fun addProducts(productModel: ProductModel, callback: (Boolean, String?) -> Unit){
        repository.addProducts(productModel,callback)
    }

    var _productList = MutableLiveData<List<ProductModel>?>()

    var productList = MutableLiveData<List<ProductModel>?>()
               get() = _productList

    var _loadingState = MutableLiveData<Boolean>()
    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState

    fun fetchAllProducts(){
        _loadingState.value = true
        repository.getAllProducts { products, success,message ->
            if(products!=null){
                _loadingState.value = false
                _productList.value = products
            }
        }
    }
}