package com.example.yike.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.yike.R
import com.example.yike.viewModel.Answer

@Composable
fun AnswerCard(
    answerInfo: Answer
) {
    Surface(
        shape = MaterialTheme.shapes.medium, // 使用 MaterialTheme 自带的形状
        elevation = 5.dp,
        modifier = Modifier.padding(all = 8.dp).fillMaxWidth()
    ){
        Row(
            modifier = Modifier.padding(all = 8.dp) // 在我们的 Card 周围添加 padding
        ) {
            UserAvatar(avatar = R.drawable.fiddle_leaf)
            Spacer(Modifier.padding(horizontal = 8.dp)) // 添加一个空的控件用来填充水平间距，设置 padding 为 8.dp
            Column {
                Text(text = answerInfo.userInfo.name)
                Spacer(Modifier.padding(vertical = 4.dp))
                AnswerContent(content = answerInfo.content)
            }
        }
    }
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(15.dp) // 外边距
//            .clickable { },
//
//        // 设置点击波纹效果，注意如果 CardDemo() 函数不在 MaterialTheme 下调用
//        // 将无法显示波纹效果
//
//        elevation = 10.dp // 设置阴影
//    ) {
//        Column(
//            modifier = Modifier.padding(15.dp) // 内边距
//        ) {
//            UserAvatar(userInfo = answerInfo)
//            AnswerContent()
//        }
//    }
}

@Composable
private fun AnswerContent(content: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(content)
    }
}

@Composable
private fun UserAvatar(avatar: Int) {
    Image(
        painterResource(id = avatar),
        contentDescription = "profile picture",
        modifier = Modifier
            .size(50.dp) // 改变 Image 元素的大小
            .clip(CircleShape) // 将图片裁剪成圆形
    )
}