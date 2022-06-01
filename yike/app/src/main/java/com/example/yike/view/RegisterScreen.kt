package com.example.yike

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yike.viewModel.*
import kotlinx.coroutines.delay


@Composable
fun ResgisterScreen(navController: NavController,
                    simpleVerifyViewModel: SimpleVerifyViewModel,
                    ){

    val getTo = simpleVerifyViewModel.getTo.observeAsState()
    if(!getTo.value.isNullOrEmpty()) {
        println("nowOk${getTo.value}")
    }

    RegisterScreenContent(navController,getTo.value){
        email ->  simpleVerifyViewModel.sendEmail(email)
    }


}

@Composable
fun RegisterScreenContent(navController: NavController,
                          code: String?,
                           clickEvent: (email: String) -> Unit,
                           ){
    val emailInput = remember { EmailState() }

    println("122code=$code")
    if(!code.isNullOrEmpty()){
        println("1code=$code")
        GlobalViewModel.updateVerifyCode(code)
        navController.navigate("personRegister2_screen")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xB2806FA0), Color(0xE14256C4)),
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            ),
    ) {
        Box(
            Modifier.align(Alignment.TopStart)
        ){
            RegisterTable(navController)
        }
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 100.dp)
            ) {

                Spacer(Modifier.height(50.dp))
                RegistDescript()
                Spacer(Modifier.height(30.dp))
                TextEmail(emailInput)
                Spacer(Modifier.height(10.dp))
                VerifyButton(
                    onClick = {
                        if (emailInput.isValid) {
                            clickEvent(emailInput.text)
                            GlobalViewModel.updateEmail(emailInput.text)
                            println("check!2222222")
//                    println(GlobalViewModel.getVerifyCode())
                        }
                    }
                )

            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(0.dp, 30.dp),
            contentAlignment = Alignment.Center
        ){
            TermsOfServiceLabel(){
                navController.navigate("login")
            }
        }
    }

}


@Composable
private fun TermsOfServiceLabel(
    registerEvent: () -> Unit = {}
) {
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color(0xFFC7C7C7),
                fontSize = 16.sp
            )
        ){
            append("已有账号？")
        }
        pushStringAnnotation(
            tag = "tag",
            annotation = "转到登录界面"
        )
        withStyle(
            style = SpanStyle(
                color = Color(0xFF227AFF),
                fontSize = 16.sp
            )
        ) {
            append("立刻登录")
        }
        pop()
    }
    ClickableText(
        text = text,
        modifier = Modifier
            .paddingFromBaseline(top = 24.dp),
        onClick = { offset ->
            text.getStringAnnotations(
                tag = "tag", start = offset,
                end = offset
            ).firstOrNull()?.let { annotation ->
                run(registerEvent)
            }
        },
    )
}

@Composable
private fun RegisterTable(navController: NavController){
    TextButton(onClick = {
        navController.navigate("officialRegister_screen")
    }) {
        Text(
            text = "官方组织注册",
            fontSize =20.sp,
            color = Color(0xFFFFFFFF),
            style = MaterialTheme.typography.button,
            modifier = Modifier.padding(start = 8.dp,end = 8.dp,top = 8.dp)
        )
    }
}

@Composable
fun RegistDescript(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column() {
            Text(
                //"开启我的大学一刻",
                "个人用户注册",
                color = Color(0xFFFFFFFF),
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
//                .padding(start = 60.dp, end = 60.dp)
            )
        }

    }
}

@Composable
private fun TextEmail(emailInput:EmailState){
//    var text by remember{ mutableStateOf("") }

    val textState = remember {
        EmailState()
    }

    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color(0x51E4DFDB),
        modifier = Modifier
            .size(width = 700.dp, height = 55.dp)
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = textState.text,
            onValueChange = { newString ->
                textState.text = newString
                emailInput.text = textState.text
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0D0D0E),
                backgroundColor = Color.Transparent,
                cursorColor = Color(0xFF045DA0),
            ),
            maxLines = 1,
            placeholder = { Text("请输入邮箱号获取验证码",
                modifier = Modifier
//                    .padding(start = 75.dp ,end =50.dp )
                    .fillMaxWidth(),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                ) },
//            shape = RoundedCornerShape(30.dp)
        )
    }
}

@Composable
private fun VerifyButton(
//    navController: NavController,
//                 emailInput: EmailState,
                 onClick: () -> Unit
                 ){

    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color(0xFFFFFFFF),
        modifier = Modifier
            .size(width = 700.dp, height = 55.dp)
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("获取邮箱验证码",
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                style =MaterialTheme.typography.button,
                color = Color(0xFF0D0D0E),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }

    }
}

