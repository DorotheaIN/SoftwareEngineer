package com.example.yike

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.yike.data.Taylor
import com.example.yike.viewModel.GlobalViewModel
import com.example.yike.viewModel.PublishQuestion
import com.example.yike.viewModel.PublishQuestionViewModel


@Composable
fun InfoPublishQuesScreen(
    navController: NavController,
    publishQuestionViewModel: PublishQuestionViewModel
    ){
    publishQuestionViewModel.init()
    val publishQuestionList = publishQuestionViewModel.publishQuestionList.observeAsState()
    //QuestionList(questions = QuesData.ques,navController)
    QuestionList(publishQuestionList.value,navController)
}

@Composable
fun QuestionList(questionsList:List<PublishQuestion>?,navController: NavController){
    LazyColumn()
    {
        item {
            PubQuesTable(navController)
        }
        item(questionsList){
            if(questionsList != null) {
                questionsList.forEach{
                    QuestionCard(navController,it)
                }
//                ques ->
//                QuestionCard(navController, ques = ques)
            }
        }
    }
}

@Composable
fun PubQuesTable(
    navController: NavController
){
    Row(
        modifier = Modifier.fillMaxHeight()
    ) {
        IconButton(
            onClick = {
                navController.popBackStack()//回退
            } ,//do something
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
                tint = Color(0xFFB1A8A1)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "我提出的问题",
            color = Color.Black,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 16.dp)
                .align(Alignment.CenterVertically),
        )
    }
}

@Composable
fun QuestionCard(navController: NavController,ques:PublishQuestion) {
//    val user = Taylor
    val id = ques.id
    Surface(
        shape = MaterialTheme.shapes.medium, // 使用 MaterialTheme 自带的形状
        elevation = 0.dp,
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .clickable {
//                navController.navigate("detailed_screen")
                navController.navigate("question/${id}")
            }

    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(all = 8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(GlobalViewModel.getUserInfo()?.avator),//用户照片
                    contentDescription = "profile picture", //这个描述用于无障碍
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Column(

                ) {
                    Text(
                        text = GlobalViewModel.getUserInfo()?.user_NAME.toString(),
                        style = MaterialTheme.typography.subtitle2 // 添加 style
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = GlobalViewModel.getUserInfo()?.introduction.toString(),
                        style = MaterialTheme.typography.caption // 添加 style

                    )
                }
            }
            Text(
                text = ques.title,
                style = MaterialTheme.typography.h6, // 添加 style
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 5.dp,
                    bottom = 5.dp
                ),
            )
            Text(
                text = ques.content.substringAfter('/'),
                style = MaterialTheme.typography.body2, // 添加 style
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 5.dp,
                    bottom = 5.dp
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
                )
        }
    }
}