package com.example.yike.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.yike.data.Taylor
import com.example.yike.viewModel.FollowQuestion
import com.example.yike.viewModel.FollowQuestionViewModel
import com.example.yike.viewModel.PublishQuestion
import com.example.yike.viewModel.PublishQuestionViewModel

@Composable
fun InfoFollowQuesScreen(
    navController: NavController,
    followQuestionViewModel: FollowQuestionViewModel
){
    followQuestionViewModel.init()
    val followQuestionList = followQuestionViewModel.followQuestionList.observeAsState()
    //QuestionList(questions = QuesData.ques,navController)
    QuestionList(followQuestionList.value,navController)
}

@Composable
fun QuestionList(questionsList:List<FollowQuestion>?, navController: NavController){
    LazyColumn()
    {
        item {
            FollowQuesTable(navController)
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
fun FollowQuesTable(
    navController: NavController
){
    Row() {
        IconButton(
            onClick = {
                navController.popBackStack()//回退
            }, //do something
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
            text = "我关注的问题",
            color = Color.Black,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 16.dp)
                .align(Alignment.CenterVertically),
        )
    }
}

@Composable
fun QuestionCard(navController: NavController, ques: FollowQuestion) {
//    val user = Taylor
    val id = ques.question_ID
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
//                    painterResource(user.pic),
                    painter = rememberImagePainter(ques.avator.toString()),//提出问题人的头像
                    contentDescription = "profile picture", //这个描述用于无障碍
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Column(

                ) {
                    Text(
                        text = ques.user_NAME.toString(),//提出问题人的名字
                        style = MaterialTheme.typography.subtitle2 // 添加 style
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = ques.introduction.toString(),//提出问题人的简介
                        style = MaterialTheme.typography.caption // 添加 style

                    )
                }
            }
            Text(
                text = ques.title.toString(),//问题的标题
                style = MaterialTheme.typography.h6, // 添加 style
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 5.dp,
                    bottom = 5.dp
                ),
            )
            Text(
                text = ques.content.toString().substringAfter('/'),//提出问题的具体内容
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