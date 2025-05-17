package com.example.apptechchallengeoh.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun TitleBar(title: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                spotColor = Color(0x1A000000),
                ambientColor = Color(0x1A000000)
            )
            .fillMaxWidth()
            .background(color = Color(0xFFFFFFFF))
            .padding(top = 32.dp)
            .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 16.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth(),
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 32.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF000000),
            )
        )
    }
}

@Composable
fun TitleBarWithComposable(title: String,  content: @Composable ColumnScope.() -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                spotColor = Color(0x1A000000),
                ambientColor = Color(0x1A000000)
            )
            .fillMaxWidth()
            .background(color = Color(0xFFFFFFFF))
            .padding(top = 32.dp)
            .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 16.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth(),
            style = TextStyle(
                fontSize = 32.sp,
                lineHeight = 40.sp,
                color = Color(0xFF000000),

                )
        )
        content()
    }
}

@Composable
fun MyAlertDialog(showDialog: Boolean, message: String){
    var showDialog by remember { mutableStateOf(showDialog) }
    if (showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Error de Login") },
            text = { Text("Datos no son correctos.\n$message") },
            confirmButton = {
                Button(
                    onClick = { showDialog = false  },
                    colors = ButtonDefaults.buttonColors(Color(0xFF00754A)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(
                        text = "OK",
                        color = Color(0xFFFFFFFF),
                    )
                }
            }
        )
    }
}

@Composable
fun ViewLoading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .zIndex(10f)
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White // Color de fondo del diálogo
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(16.dp),
                    color = Color(0xFF006241)
                )
               // CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun Dp.toPxx(): Float {
    return with(LocalDensity.current) { this@toPxx.toPx() }
}