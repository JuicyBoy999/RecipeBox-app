package com.example.recipebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipebox.R
import com.example.recipebox.ui.theme.RecipeBoxTheme
import com.example.recipebox.ui.theme.Orange

class RecipeBoxNewPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewPassword()
        }
    }
}

@Composable
fun NewPassword() {
    var password by remember { mutableStateOf("") }
    Column (
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFF3E0))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_chef_hat_24),
                contentDescription = "Chef Hat Icon",
                tint = Color(0xFFEF6C00),
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "RecipeBox",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Better remember it now",
            fontSize = 18.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .height(58.dp),
            placeholder = {
                Text("Enter your new password")
            },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.08f),
                focusedContainerColor = Color.Gray.copy(alpha = 0.08f)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        ElevatedButton(
            onClick = {},
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange
            )
        ) {
            Text("Change", style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewPasswordPreview() {
    NewPassword()
}