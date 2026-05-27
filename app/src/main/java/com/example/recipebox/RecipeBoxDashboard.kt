package com.example.recipebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipebox.ui.theme.RecipeBoxTheme
import com.example.recipebox.ui.theme.Orange

class RecipeBoxDashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Dashboard()
        }
    }
}

@Composable
fun Dashboard() {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column (
            modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hello, Good day!", style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.Gray
                    ))
                    Text("What would you like to Cook?", style = TextStyle (
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ))
                }
                Box(
                    modifier = Modifier
                        .size(80.dp)
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
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text("Categories", style = TextStyle (
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            ))
            Spacer(modifier = Modifier.height(5.dp))
            ElevatedButton(onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                )) {
                Text("All", style = TextStyle(
                    fontWeight = FontWeight.Bold
                ))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(painter = painterResource(R.drawable.baseline_auto_graph_24),
                    contentDescription = null,
                    tint = Color(0xFFEF6C00))
                Spacer(modifier = Modifier.width(5.dp))
                Text("Popular Recipes", style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                ))
            }
        }
        LazyColumn {
            item {  }
        }
        Row (
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxWidth()
                .height(75.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painter = painterResource(R.drawable.outline_fastfood_24),
                    contentDescription = null,
                    tint = Color(0xFFEF6C00))
                Text("Home", style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFEF6C00)
                ))
            }
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painter = painterResource(R.drawable.baseline_search_24),
                    contentDescription = null,
                    tint = Color.Gray)
                Text("Search", style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                ))
            }
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painter = painterResource(R.drawable.baseline_add_circle_outline_24),
                    contentDescription = null,
                    tint = Color.Gray)
                Text("Add", style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                ))
            }
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painter = painterResource(R.drawable.outline_inventory_2_24),
                    contentDescription = null,
                    tint = Color.Gray)
                Text("Pantry", style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                ))
            }
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painter = painterResource(R.drawable.baseline_person_outline_24),
                    contentDescription = null,
                    tint = Color.Gray)
                Text("Profile", style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                ))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    Dashboard()
}