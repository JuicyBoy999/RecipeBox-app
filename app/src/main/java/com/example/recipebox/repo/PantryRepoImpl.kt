package com.example.recipebox.repo

import com.example.recipebox.model.PantryItemModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PantryRepoImpl : PantryRepo {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("pantryItems")

    override fun addItem(
        model: PantryItemModel,
        callback: (Boolean, String) -> Unit
    ) {
        val id = ref.push().key.toString()
        model.itemId = id

        ref.child(id).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Item added successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateItem(
        model: PantryItemModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(model.itemId)
            .updateChildren(model.toMap()).addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Item updated successfully")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }
    }

    override fun deleteItem(
        id: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Item deleted successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getItemById(
        id: String,
        callback: (Boolean, PantryItemModel?) -> Unit
    ) {
        ref.child(id).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val item = snapshot.getValue(PantryItemModel::class.java)
                        callback(true, item)
                    } else {
                        callback(false, null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, null)
                }
            }
        )
    }

    override fun getAllItems(callback: (Boolean, List<PantryItemModel>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val allItems = mutableListOf<PantryItemModel>()
                    for (data in snapshot.children) {
                        val item = data.getValue(PantryItemModel::class.java)
                        item?.let {
                            allItems.add(it)
                        }
                    }
                    callback(true, allItems)
                } else {
                    callback(true, emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, emptyList())
            }
        })
    }
}
