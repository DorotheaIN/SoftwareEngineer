package com.example.yike.service

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//Token的使用?

object Network {

    //service:
    private val userService = ServiceCreator.create<UserService>()

    //suspend fun:
    suspend fun getLoginStatus(userName: String, passWord: String) =
        userService.getLoginStatus(userName, passWord).await()

    suspend fun getQuestionList() =
        userService.getQuestionList().await()

    suspend fun getAnswerList() =
        userService.getAnswerList().await()

    suspend fun getTest() =
        userService.getTest().await()

    //为call添加扩展函数 await
    //这样所有返回call的函数都可以调用之
    private suspend fun <T> Call<T>.await() :T {
        return suspendCoroutine { continuation -> //传入调用者的上下文
            enqueue(object : Callback<T> { //开启retrofit请求
                //重载回调部分
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body) // 如果body合法 则恢复协程 返回body
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    println(t)
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}
