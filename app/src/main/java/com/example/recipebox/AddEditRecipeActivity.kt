package com.example.recipebox

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipebox.model.RecipeModel
import com.example.recipebox.repo.RecipeRepoImpl
import com.example.recipebox.ui.theme.Orange
import com.example.recipebox.viewmodel.RecipeViewModel
import com.google.firebase.auth.FirebaseAuth

class AddEditRecipeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val recipeId = intent.getStringExtra("recipeId")
        setContent {
            AddEditRecipeBody(recipeId)
        }
    }
}

@Composable
fun AddEditRecipeBody(recipeId: String? = null) {
    val context = LocalContext.current
    val activity = context as Activity
    val isEditMode = !recipeId.isNullOrBlank()

    val recipeViewModel = remember { RecipeViewModel(RecipeRepoImpl()) }
    val existingRecipe = recipeViewModel.recipe.observeAsState(initial = null)

    var title by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var cookTime by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(recipeId) {
        if (isEditMode) {
            recipeViewModel.getRecipeById(recipeId!!)
        }
    }

    LaunchedEffect(existingRecipe.value) {
        existingRecipe.value?.let {
            title = it.title
            ingredients = it.ingredients
            instructions = it.instructions
            cookTime = it.cookTimeMinutes.toString()
            category = it.category
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            .fillMaxSize()
    ) {
        Row {
            IconButton(onClick = { activity.finish() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Orange
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (isEditMode) "Edit Recipe" else "Add Recipe",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            placeholder = { Text("Recipe title") },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.08f),
                focusedContainerColor = Color.Gray.copy(alpha = 0.08f)
            )
        )

        OutlinedTextField(
            value = cookTime,
            onValueChange = { cookTime = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            placeholder = { Text("Cook time (minutes)") },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.08f),
                focusedContainerColor = Color.Gray.copy(alpha = 0.08f)
            )
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            placeholder = { Text("Category (e.g. Breakfast, Dessert)") },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.08f),
                focusedContainerColor = Color.Gray.copy(alpha = 0.08f)
            )
        )

        OutlinedTextField(
            value = ingredients,
            onValueChange = { ingredients = it },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Ingredients (one per line)") },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.08f),
                focusedContainerColor = Color.Gray.copy(alpha = 0.08f)
            )
        )

        OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(150.dp),
            placeholder = { Text("Instructions") },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.08f),
                focusedContainerColor = Color.Gray.copy(alpha = 0.08f)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedButton(
            onClick = {
                if (title.isBlank()) {
                    Toast.makeText(context, "Please enter a recipe title", Toast.LENGTH_SHORT).show()
                    return@ElevatedButton
                }
                val cookTimeValue = cookTime.toIntOrNull()
                if (cookTime.isNotBlank() && (cookTimeValue == null || cookTimeValue <= 0)) {
                    Toast.makeText(context, "Cook time must be a positive number", Toast.LENGTH_SHORT).show()
                    return@ElevatedButton
                }
                isSaving = true
                val model = RecipeModel(
                    recipeId = recipeId ?: "",
                    title = title,
                    ingredients = ingredients,
                    instructions = instructions,
                    cookTimeMinutes = cookTimeValue ?: 0,
                    category = category,
                    userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                )
                val onResult: (Boolean, String) -> Unit = { success, msg ->
                    isSaving = false
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    if (success) {
                        activity.finish()
                    }
                }
                if (isEditMode) {
                    recipeViewModel.updateRecipe(model, onResult)
                } else {
                    recipeViewModel.addRecipe(model, onResult)
                }
            },
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange
            ),
            enabled = !isSaving
        ) {
            if (isSaving) {
                CircularProgressIndicator(modifier = Modifier.height(20.dp))
            } else {
                Text(
                    if (isEditMode) "Save Changes" else "Add Recipe",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEditRecipePreview() {
    AddEditRecipeBody()
}
