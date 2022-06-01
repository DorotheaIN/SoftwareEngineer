package com.example.yike.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
//  private const val BASE_URL = "http://100.78.140.233:8888"
    private const val BASE_URL = "http://106.14.79.15:8888"
//  private const val BASE_URL = "http://192.168.1.100:8888"



    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
//    network中可以直接用下面的方法创建
//    val userService = ServiceCreator.create(UserService:: class.java)
//    调用class.java方法 获得类
//    也可以用下方的inline函数 val userService = ServiceCreator.create<UserService>()
    inline fun <reified T> create(): T = create(T::class.java)
}

