package com.example.yike.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yike.R
import com.example.yike.component.AnswerCard
import com.example.yike.component.DiscussTopBar
import com.example.yike.viewModel.Answer
import com.example.yike.viewModel.Question
import com.example.yike.viewModel.QuestionViewModel

@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel,
    navController: NavController,
    ) {
    val isGet = viewModel.isGet.observeAsState()
    val postRes = viewModel.postResult.observeAsState()
    if (isGet.value != true) {
        viewModel.getAnswerList()
    } else {
        val originStatus = viewModel.originStatus.observeAsState()
        if (originStatus.value != null && !viewModel.isSet()) viewModel.setQuestionStatus()
        val answerList = viewModel.answerList.observeAsState()
        val questionStatus = viewModel.questionStatus.observeAsState()
        viewModel.getQuestionBody()?.let {
            QuestionScreenScaffold(answerList.value, viewModel.getQuestionBody(), originStatus.value,
                questionStatus.value,
                backEvent = {
                    viewModel.postQuestionStatus()
                    navController.popBackStack()
                },
                clickEvent1 = {
                    viewModel.convertQuestionStatus()
                },
                navController,//whl增加
                viewModel.questionId,//whl增加
                it.title,
            )
        }
    }
}

@Composable
private fun QuestionScreenScaffold(
    answerList: ArrayList<Answer>?,
    questionBody: Question?,
    originStatus: Int?,
    questionStatus: Int?,
    backEvent: () -> Unit,
    clickEvent1: () -> Unit,
    navController: NavController,//whl增加
    questionId:String,//whl增加
    questionTitle: String,//whl增加
) {
    Scaffold(
        topBar = {
            DiscussTopBar(backEvent)
        }
    ) { paddingValues ->

        if (questionBody == null || originStatus == null) {
            QuestionScreenLoader(paddingValues)
        } else {
            QuestionScreenContent(
                answerList,
                questionBody,
                questionStatus!!,
                paddingValues,
                navController,// whl增加
                questionId,//whl增加
                questionTitle,//whl增加
                clickEvent1,
//                routeEvent

            )
        }
    }
}

@Composable
private fun QuestionScreenLoader(
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun QuestionScreenContent(
    answerList: ArrayList<Answer>?,
    questionBody: Question,
    questionStatus: Int,
    paddingValues: PaddingValues,
    navController: NavController,//whl增加
    questionId:String,//whl增加
    questionTitle: String,//whl增加
    clickEvent1: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            QuestionSection(
                questionBody
            )

            UserOperationSection(
                questionStatus,
                questionId,
                questionTitle,
                navController,
                clickEvent1,
            )

            AnswerListSection(
                answerList,
                navController,//whl增加
                questionId,//whl增加
            )
        }
    }
}

@Composable
private fun QuestionSection(
    questionBody: Question
) {
    Surface(
        shape = MaterialTheme.shapes.medium, // 使用 MaterialTheme 自带的形状
        elevation = 5.dp,
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            Modifier.padding(15.dp,15.dp)
        ){
            Text(
                text = questionBody.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(//折叠的
                text = questionBody.description.substringAfter('/'),
//                fontSize = 15.sp,
                color = Color(0xFF5F5F5F)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = questionBody.description.substringBefore('/'),
                                fontSize = 15.sp,
                color = Color(0xFFA5A5A5)

            )
        }
    }
}

@Composable
private fun UserOperationSection(
    questionStatus: Int,
    questionId: String,
    questionTitle:String,
    navController: NavController,
    clickEvent1: () -> Unit = {}
) {
    Surface(
        shape = MaterialTheme.shapes.medium, // 使用 MaterialTheme 自带的形状
        elevation = 5.dp,
        modifier = Modifier.padding(all = 8.dp)
    ) {
        val change = remember{ mutableStateOf(false) }
        val buttonSize by animateDpAsState(
            targetValue = if(change.value) 30.dp else 24.dp
        )
        if(buttonSize == 30.dp) {
            change.value = false
        }
        Row(
            Modifier.fillMaxWidth(1f)
        ){
            Column(
                modifier = Modifier
                    .padding(40.dp, 5.dp, 15.dp, 5.dp)
                    .fillMaxWidth(0.45f)
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    IconButton(
                        onClick = {
                            clickEvent1()
                            change.value = true
                        },
//                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(painter = painterResource(id = if(questionStatus == 1) R.drawable.collect_selected else R.drawable.collect),
                            contentDescription = "Star",
                            modifier = Modifier.size(buttonSize),
                            tint = if(questionStatus == 1) Color(0xFFEEBB21) else Color.Gray
                        )
                    }
                    Text(text="关注",color = Color.DarkGray,
                        fontSize = 18.sp,
//                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)
                    )


                }
            }
            Column(
                modifier = Modifier.padding(40.dp,5.dp,15.dp,5.dp)
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    IconButton(
                        onClick = {
                            navController.navigate("publishAnswer_screen/${questionId}/${questionTitle}")
                        },
//                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Outlined.Edit,//交互
                            contentDescription = "Edit"
                        )
                    }
                    Text(text="回答",color = Color.DarkGray,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)
                    )
                }
            }
        }


    }

}

@Composable
private fun AnswerListSection(
    answerList: ArrayList<Answer>?,
    navController: NavController,//whl增加
    questionId:String,//whl增加
) {
//    LazyColumn {
//        items(answerModel) { answer ->
//            AnswerCard(answerInfo = answer)
//        }
//    }
    Column() {
        answerList?.reversed()?.forEach{ answer ->
            AnswerCard(answerInfo = answer,navController,questionId)
        }
    }
}

//@Preview(
//    name = "Day Mode",
//    uiMode = Configuration.UI_MODE_NIGHT_NO,
//)
//@Composable
//private fun QuestionScreenPreview() {
//    YikeTheme {
//        QuestionScreen()
//    }
//}