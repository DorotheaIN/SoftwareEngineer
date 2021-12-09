package com.example.yike.service

import androidx.lifecycle.liveData
import com.example.yike.model.QuestionResponse
import com.example.yike.viewModel.Question
import com.example.yike.viewModel.UserInfo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

//repo中函函数名与VM中相同 表用户方法
//network和接口中定义的函数名相同 通过get post来反应请求类型

object LoginRepository {
    fun checkLoginStatus(userName: String, passWord: String) = liveData(Dispatchers.IO) {
        val result = try {
            val loginResponse = Network.getLoginStatus(userName, passWord)
//            val testResponse = Network.getTest()
            if (loginResponse.status == 200) {
                UserInfo(loginResponse.result.userId, loginResponse.result.userName,
                    loginResponse.result.userStatus)
//                UserInfo(testResponse.result, "1", "user")
            } else {
                println("response status is not ok")
                UserInfo()
            }
        } catch (e: Exception){
            println(e)
            UserInfo()
        }
        emit(result)
    }
}

object QuestionRepository {
    fun getQuestionList() = liveData(Dispatchers.IO) {
        val result = try {
            val questionList = Network.getQuestionList()
            if (questionList.status == 200) {
                questionList.result
            } else {
                println("response status is not ok")
                null
            }
        } catch (e: Exception){
            println(e)
            null
        }
        emit(result)
    }
}

object AnswerRepository {
    fun getAnswerList() = liveData(Dispatchers.IO) {
        val result = try {
            val answerList = Network.getAnswerList()
            if (answerList.status == 200) {
                answerList.result
            } else {
                println("response status is not ok")
                null
            }
        } catch (e: Exception){
            println(e)
            null
        }
        emit(result)
    }
}

//object Repository {
//    封装写法：
//    fun checkLoginStatus(userName: String, passWord: String) = fire(Dispatchers.IO) {
////        val loginResponse = Network.getLoginStatus(userName, passWord)
//        val loginResponse = Network.getLoginStatus()
//        println(loginResponse)
//        if (loginResponse.status == "ok") {
//            val userInfo = UserInfo(loginResponse.result.userId, loginResponse.result.userName,
//                loginResponse.result.passWord, loginResponse.result.userStatus)
//            println("response userInfo:")
//            println(userInfo)
//            Result.success(userInfo)
//        } else {
////            RequestResult(false, RuntimeException("response status is ${loginResponse.status}"))
//            Result.failure(RuntimeException("response status is ${loginResponse.status}"))
//        }
//    }
//
//    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
//        liveData(context) {
//            var result = try {
//                block()
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//            emit(result)
//        }
//}



