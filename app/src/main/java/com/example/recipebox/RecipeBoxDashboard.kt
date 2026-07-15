package com.example.recipebox

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipebox.model.RecipeModel
import com.example.recipebox.repo.RecipeRepoImpl
import com.example.recipebox.ui.theme.Orange
import com.example.recipebox.viewmodel.RecipeViewModel

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
    val context = LocalContext.current

    val recipeViewModel = remember { RecipeViewModel(RecipeRepoImpl()) }

    val allRecipes = recipeViewModel.allRecipes.observeAsState(initial = emptyList())
    val loading = recipeViewModel.loading.observeAsState(initial = false)

    var recipePendingDelete by remember { mutableStateOf<RecipeModel?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    val categories = allRecipes.value.orEmpty()
        .map { it.category }
        .filter { it.isNotBlank() }
        .distinct()

    val visibleRecipes = allRecipes.value.orEmpty()
        .filter { selectedCategory == null || it.category == selectedCategory }
        .filter { !showFavoritesOnly || it.isFavorite }

    LaunchedEffect(Unit) {
        recipeViewModel.getAllRecipes()
    }

    recipePendingDelete?.let { recipe ->
        AlertDialog(
            onDismissRequest = { recipePendingDelete = null },
            title = { Text("Delete recipe?") },
            text = { Text("\"${recipe.title}\" will be permanently deleted.") },
            confirmButton = {
                TextButton(onClick = {
                    recipePendingDelete = null
                    recipeViewModel.deleteRecipe(recipe.recipeId) { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { recipePendingDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

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
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                ElevatedButton(
                    onClick = { selectedCategory = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory == null) Orange else Color.Gray.copy(alpha = 0.15f)
                    )
                ) {
                    Text("All", style = TextStyle(fontWeight = FontWeight.Bold))
                }
                categories.forEach { category ->
                    Spacer(modifier = Modifier.width(8.dp))
                    ElevatedButton(
                        onClick = { selectedCategory = category },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategory == category) Orange else Color.Gray.copy(alpha = 0.15f)
                        )
                    ) {
                        Text(category, style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(R.drawable.baseline_auto_graph_24),
                        contentDescription = null,
                        tint = Color(0xFFEF6C00))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Popular Recipes", style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    ))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "(${allRecipes.value.orEmpty().size})",
                        style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.Gray,
                        modifier = Modifier.clickable { recipeViewModel.getAllRecipes() }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = if (showFavoritesOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Show favorites only",
                        tint = if (showFavoritesOnly) Color.Red else Color.Gray,
                        modifier = Modifier.clickable { showFavoritesOnly = !showFavoritesOnly }
                    )
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            if (loading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Orange)
                }
            } else if (visibleRecipes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (allRecipes.value.isNullOrEmpty()) "No recipes yet. Tap Add to create one."
                        else "No recipes in this category yet.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp)
                ) {
                    items(visibleRecipes) { recipe ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(Color.Gray.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
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
                            Row {
                                Icon(
                                    imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (recipe.isFavorite) Color.Red else Color.Gray,
                                    modifier = Modifier.clickable {
                                        recipeViewModel.updateRecipe(recipe.copy(isFavorite = !recipe.isFavorite)) { _, _ -> }
                                    }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Orange,
                                    modifier = Modifier.clickable {
                                        val intent = Intent(context, AddEditRecipeActivity::class.java)
                                        intent.putExtra("recipeId", recipe.recipeId)
                                        context.startActivity(intent)
                                    }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Gray,
                                    modifier = Modifier.clickable {
                                        recipePendingDelete = recipe
                                    }
                                )
                            }
                        }
                    }
                }
            }
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    val intent = Intent(context, RecipeSearchActivity::class.java)
                    context.startActivity(intent)
                }
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    val intent = Intent(context, AddEditRecipeActivity::class.java)
                    context.startActivity(intent)
                }
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    val intent = Intent(context, PantryActivity::class.java)
                    context.startActivity(intent)
                }
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                }
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
