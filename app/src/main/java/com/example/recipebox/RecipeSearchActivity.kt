package com.example.recipebox

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipebox.repo.RecipeRepoImpl
import com.example.recipebox.ui.theme.Orange
import com.example.recipebox.viewmodel.RecipeViewModel

class RecipeSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeSearchBody()
        }
    }
}

@Composable
fun RecipeSearchBody() {
    val context = LocalContext.current
    val activity = context as Activity

    val recipeViewModel = remember { RecipeViewModel(RecipeRepoImpl()) }
    val allRecipes = recipeViewModel.allRecipes.observeAsState(initial = emptyList())
    val loading = recipeViewModel.loading.observeAsState(initial = false)

    var query by remember { mutableStateOf("") }
    val filteredRecipes = allRecipes.value.orEmpty()
        .filter { query.isBlank() || it.title.contains(query, ignoreCase = true) }

    LaunchedEffect(Unit) {
        recipeViewModel.getAllRecipes()
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { activity.finish() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Orange
                )
            }
            Text(
                "Search Recipes",
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
            )
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            placeholder = { Text("Search by recipe title") },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.08f),
                focusedContainerColor = Color.Gray.copy(alpha = 0.08f)
            )
        )

        Box(modifier = Modifier.weight(1f)) {
            if (loading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Orange)
                }
            } else if (filteredRecipes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (allRecipes.value.isNullOrEmpty()) "No recipes yet." else "No recipes match your search.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredRecipes) { recipe ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(Color.Gray.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                recipe.title,
                                style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            )
                            Text(
                                if (recipe.category.isNotBlank())
                                    "${recipe.category} · ${recipe.cookTimeMinutes} min"
                                else
                                    "${recipe.cookTimeMinutes} min",
                                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeSearchPreview() {
    RecipeSearchBody()
}
