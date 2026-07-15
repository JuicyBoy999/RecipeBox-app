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
import com.example.recipebox.model.PantryItemModel
import com.example.recipebox.repo.PantryRepoImpl
import com.example.recipebox.ui.theme.Orange
import com.example.recipebox.viewmodel.PantryViewModel
import com.google.firebase.auth.FirebaseAuth

class AddEditPantryItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val itemId = intent.getStringExtra("itemId")
        setContent {
            AddEditPantryItemBody(itemId)
        }
    }
}

@Composable
fun AddEditPantryItemBody(itemId: String? = null) {
    val context = LocalContext.current
    val activity = context as Activity
    val isEditMode = !itemId.isNullOrBlank()

    val pantryViewModel = remember { PantryViewModel(PantryRepoImpl()) }
    val existingItem = pantryViewModel.item.observeAsState(initial = null)

    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(itemId) {
        if (isEditMode) {
            pantryViewModel.getItemById(itemId!!)
        }
    }

    LaunchedEffect(existingItem.value) {
        existingItem.value?.let {
            name = it.name
            quantity = it.quantity.toString()
            unit = it.unit
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
            text = if (isEditMode) "Edit Pantry Item" else "Add Pantry Item",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            placeholder = { Text("Item name") },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.08f),
                focusedContainerColor = Color.Gray.copy(alpha = 0.08f)
            )
        )

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            placeholder = { Text("Quantity") },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.08f),
                focusedContainerColor = Color.Gray.copy(alpha = 0.08f)
            )
        )

        OutlinedTextField(
            value = unit,
            onValueChange = { unit = it },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            placeholder = { Text("Unit (e.g. cups, g, pcs)") },
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
                if (name.isBlank()) {
                    Toast.makeText(context, "Please enter an item name", Toast.LENGTH_SHORT).show()
                    return@ElevatedButton
                }
                isSaving = true
                val model = PantryItemModel(
                    itemId = itemId ?: "",
                    name = name,
                    quantity = quantity.toDoubleOrNull() ?: 0.0,
                    unit = unit,
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
                    pantryViewModel.updateItem(model, onResult)
                } else {
                    pantryViewModel.addItem(model, onResult)
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
                    if (isEditMode) "Save Changes" else "Add Item",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEditPantryItemPreview() {
    AddEditPantryItemBody()
}
