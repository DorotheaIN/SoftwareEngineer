package com.example.yike.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@Composable
fun NavBottomBar(
    navController: NavController,
    dir: String
) {
    BottomAppBar(
        backgroundColor = Color(0xFFF1F1F1),
    ) {
        BottomButton(
            selected = dir == "Discuss",
            icon = Icons.Default.Home,
            labelText = "讨论",
            onClick = {
                navController.navigate("discuss")
            }
        )
        BottomButton(
            selected = dir == "Activity",
            icon = Icons.Default.FavoriteBorder,
            labelText = "活动",
            onClick = {
                navController.navigate("activity")
            }
        )
        BottomButton(
            selected = dir == "Info",
            icon = Icons.Default.AccountCircle,
            labelText = "个人",
            onClick = {
                navController.navigate("mainInfo_screen")
            },
        )
    }
}

@Composable
private fun RowScope.BottomButton(
    selected: Boolean,
    icon: ImageVector,
    labelText: String,
    onClick: () -> Unit = {}
) {
    BottomNavigationItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                icon,
                contentDescription = null,
            )
        },
        label = {
            Text(labelText)
        }
    )
}