package com.example.recipebox

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipebox.model.PantryItemModel
import com.example.recipebox.repo.PantryRepoImpl
import com.example.recipebox.ui.theme.Orange
import com.example.recipebox.viewmodel.PantryViewModel

class PantryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantryBody()
        }
    }
}

@Composable
fun PantryBody() {
    val context = LocalContext.current
    val activity = context as Activity

    val pantryViewModel = remember { PantryViewModel(PantryRepoImpl()) }
    val allItems = pantryViewModel.allItems.observeAsState(initial = emptyList())
    val loading = pantryViewModel.loading.observeAsState(initial = false)

    var itemPendingDelete by remember { mutableStateOf<PantryItemModel?>(null) }

    LaunchedEffect(Unit) {
        pantryViewModel.getAllItems()
    }

    itemPendingDelete?.let { pantryItem ->
        AlertDialog(
            onDismissRequest = { itemPendingDelete = null },
            title = { Text("Delete item?") },
            text = { Text("\"${pantryItem.name}\" will be permanently deleted.") },
            confirmButton = {
                TextButton(onClick = {
                    itemPendingDelete = null
                    pantryViewModel.deleteItem(pantryItem.itemId) { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemPendingDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
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
                "Pantry",
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { pantryViewModel.getAllItems() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Orange
                    )
                }
                IconButton(onClick = {
                    val intent = Intent(context, AddEditPantryItemActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add item",
                        tint = Orange
                    )
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            if (loading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Orange)
                }
            } else if (allItems.value.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No pantry items yet. Tap + to add one.", color = Color.Gray)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(allItems.value ?: emptyList()) { pantryItem ->
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
                                    pantryItem.name,
                                    style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                )
                                if (pantryItem.quantity <= 0.0) {
                                    Text(
                                        "Out of stock",
                                        style = TextStyle(fontSize = 14.sp, color = Color.Red, fontWeight = FontWeight.SemiBold)
                                    )
                                } else {
                                    Text(
                                        "${pantryItem.quantity} ${pantryItem.unit}",
                                        style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                                    )
                                }
                            }
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Orange,
                                    modifier = Modifier.clickable {
                                        val intent = Intent(context, AddEditPantryItemActivity::class.java)
                                        intent.putExtra("itemId", pantryItem.itemId)
                                        context.startActivity(intent)
                                    }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Gray,
                                    modifier = Modifier.clickable {
                                        itemPendingDelete = pantryItem
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantryPreview() {
    PantryBody()
}
