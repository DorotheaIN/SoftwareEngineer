package com.example.yike

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.yike.viewModel.GlobalViewModel

class CommentInputState : TextFieldState(
    validator = ::isValidComment,
    errorFor = ::commentValidationError
) {

}

private fun commentValidationError(comment: String): String {
    return if (comment.isBlank()){
        "评论不能为空"
    }else{
        "超过140字."
    }
}

private fun isValidComment(comment: String): Boolean {
//    return comment.length <= 140
      return comment.length <= 140 && comment.isNotBlank()
}