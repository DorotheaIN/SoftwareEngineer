package com.example.yike

class AnswerInputState : TextFieldState(
    validator = ::isValidAnswer,
    errorFor = ::answerValidationError
) { }

private fun answerValidationError(ques: String): String {
    return "输入的回答不能为空"
}

private fun isValidAnswer(quesContent: String): Boolean {
    return quesContent.isNotBlank()
}