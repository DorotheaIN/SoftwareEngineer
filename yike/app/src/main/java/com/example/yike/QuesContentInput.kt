package com.example.yike

class QuesContentInputState : TextFieldState(
    validator = ::isValidQuesContent,
    errorFor = ::quesContentValidationError
) { }

private fun quesContentValidationError(ques: String): String {
    return "输入的补充不能为空"
}

private fun isValidQuesContent(quesContent: String): Boolean {
    return quesContent.isNotBlank()
}