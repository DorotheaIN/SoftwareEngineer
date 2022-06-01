package com.example.yike

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yike.viewModel.AddAnswerViewModel
import com.example.yike.viewModel.AddQuestionViewModel
import com.example.yike.viewModel.GlobalViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PublishQuestionScreen(
    navController: NavController,
    addQuestionViewModel: AddQuestionViewModel
){
    val inputAddQuestionInfo = addQuestionViewModel.inputAddQuestionInfo.observeAsState()

    PublishScreenContent(navController){
            content, title, userId ->  addQuestionViewModel.addQues(content, title, userId)
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PublishScreenContent(
    navController: NavController,
    clickEvent:(content: String,title:String,userId: String) -> Unit
){
    val quesInput = remember { QuesInputState()}
    val quesContentInput = remember {QuesContentInputState()}
    Scaffold(
        Modifier.padding(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Button(onClick = {
                        navController.popBackStack()//回退
                    },
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFFFFFF),
                            Color(0xFFFFFF)
                        ),
                        elevation = ButtonDefaults.elevation(0.dp,0.dp,0.dp)
                    ){
                        Text("取消",
                            color = Color(0xFFB1A8A1)
                        )
                    }
                },
                backgroundColor = Color(0xFFFFFF),
                contentColor = Color(0xFFFFFF),
                elevation = 0.dp,
                actions = {
                    Button(onClick = {
                        if (quesInput.isValid && quesContentInput.isValid){
                            val current = LocalDateTime.now()

                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val formatted = current.format(formatter)

                            println("当前日期和时间为: $formatted")
                            GlobalViewModel.getUserInfo()?.let {
                                clickEvent(formatted.toString()+"/"+quesContentInput.text,quesInput.text,
                                    it.id)
                            }
                            navController.popBackStack()//回退
                        }
                    },
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFFFFFF),
                            Color(0xFFFFFF)
                        ),
                        elevation = ButtonDefaults.elevation(0.dp,0.dp,0.dp)
                    ){
                        Text("发布问题",
                            color = Color(0xFF1084E0)
                        )
                    }
                }
            )
        },
    ) {
        Column {
            InputQues(quesInput)
            Spacer(modifier = Modifier.height(20.dp))
            InputQuesIntroduction(quesContentInput)
        }
    }
}

@Composable
fun InputQues(
    quesInput:QuesInputState
){
    var textQues = remember {
        QuesInputState()
    }
    Column() {
//
        Surface(
            color = Color(0x51E4DFDB),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                value = textQues.text,
                onValueChange = { newString ->
                    textQues.text = newString
                    quesInput.text = textQues.text
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFF0D0D0E),
                    backgroundColor = Color.Transparent,
                    cursorColor = Color(0xFF045DA0),
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 26.sp
                ),
                placeholder = {
                    Text(
                        "输入一个问题吧",
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = Color(0xFFBBB4B4),
                        fontSize = 26.sp,
                    )
                },
                isError = quesInput.showErrors,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.onFocusChanged { it ->
                    val isFocused = it.isFocused
                    quesInput.onFocusChange(isFocused)
                    quesInput.enableShowErrors()
                },
            )
        }

        quesInput.getError()?.let { errorMessage ->
            TextFieldError(textError = errorMessage)
        }
    }

}

@Composable
fun InputQuesIntroduction(
    quesContentInput:QuesContentInputState
){
    var textQuesContent = remember {
        QuesContentInputState()
    }
    Column() {

        Surface(
            color = Color(0x51E4DFDB),
            modifier = Modifier
                .fillMaxWidth()
        ) {

            TextField(
                value = textQuesContent.text,
                onValueChange = { newString ->
                    textQuesContent.text = newString
                    quesContentInput.text = textQuesContent.text
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFF0D0D0E),
                    backgroundColor = Color.Transparent,
                    cursorColor = Color(0xFF045DA0),
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp
                ),
                placeholder = {
                    Text(
                        "给提出的问题一些补充吧",
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = Color(0xFFBBB4B4),
                        fontSize = 16.sp,
                    )
                },
                isError = quesContentInput.showErrors,
                modifier = Modifier.onFocusChanged { it ->
                    val isFocused = it.isFocused
                    quesContentInput.onFocusChange(isFocused)
                    quesContentInput.enableShowErrors()
                }
            )
        }
        quesContentInput.getError()?.let { errorMessage ->
            TextFieldError(textError = errorMessage)
        }
    }
}

@Composable
private fun TextFieldError(textError: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 30.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(color = MaterialTheme.colors.error)
        )
    }
}