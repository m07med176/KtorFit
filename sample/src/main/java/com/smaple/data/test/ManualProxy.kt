package com.smaple.data.test

import android.content.SharedPreferences

//class NetworkService {
//
//    fun callApi(item: String): Boolean {
//        return true
//    }
//
//    fun getItemInCart(): String {
//        return ""
//    }
//}
//
//class DatabaseClass {
//
//    fun saveResult(result: String): Boolean {
//        return true
//    }
//
//    fun getResult(): String {
//        return ""
//    }
//}
//
//
//
//interface CartRepo {
//    fun addToCart(item: String): Boolean
//}
//
//class AuthorizedUserCartRepo(val networkSerivce: NetworkService, val databaseClass: DatabaseClass) :
//    CartRepo {
//    override fun addToCart(item: String): Boolean {
//        // call network service
//        val result = networkSerivce.callApi(item)
//        // save data in cart
//        databaseClass.saveResult(item)
//        return result
//    }
//
//}
//
//class GuestUserCartRepo(val databaseClass: DatabaseClass) : CartRepo {
//    override fun addToCart(item: String): Boolean {
//        return databaseClass.saveResult(item)
//    }
//}
//
//class ProxyCartRepo(
//    val pref: SharedPreferences,
//    val networkSerivce: NetworkService,
//    val databaseClass: DatabaseClass
//) : CartRepo {
//
//    var repo: CartRepo? = null
//
//    override fun addToCart(item: String): Boolean {
//        //get the type of user
//        val type = pref.getString("type", "guest")
//
//        //there is two secario
//        if (type.equals("authorized")) {
//            if (repo == null)
//                repo = AuthorizedUserCartRepo(networkSerivce, databaseClass)
//        } else
//            if (repo == null)
//                repo = GuestUserCartRepo(databaseClass)
//
//        return repo!!.addToCart(item)
//    }
//}