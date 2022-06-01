package com.example.yike

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

class QuesInputState : TextFieldState(
    validator = ::isValidQues,
    errorFor = ::quesValidationError
) { }

private fun quesValidationError(ques: String): String {
    return if(ques.isBlank()){
        "输入的问题不能为空"
    }else{
        "问题长度不能超过50字"
    }
}

private fun isValidQues(ques: String): Boolean {
    return ques.isNotBlank() && ques.length <= 50
}