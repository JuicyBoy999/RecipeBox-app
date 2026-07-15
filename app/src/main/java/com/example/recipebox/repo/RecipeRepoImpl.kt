package com.example.recipebox.repo

import com.example.recipebox.model.RecipeModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecipeRepoImpl : RecipeRepo {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("recipes")

    override fun addRecipe(
        model: RecipeModel,
        callback: (Boolean, String) -> Unit
    ) {
        val id = ref.push().key.toString()
        model.recipeId = id

        ref.child(id).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Recipe added successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateRecipe(
        model: RecipeModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(model.recipeId)
            .updateChildren(model.toMap()).addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Recipe updated successfully")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }
    }

    override fun deleteRecipe(
        id: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Recipe deleted successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getRecipeById(
        id: String,
        callback: (Boolean, RecipeModel?) -> Unit
    ) {
        ref.child(id).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val recipe = snapshot.getValue(RecipeModel::class.java)
                        callback(true, recipe)
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

    override fun getAllRecipes(callback: (Boolean, List<RecipeModel>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val allRecipes = mutableListOf<RecipeModel>()
                    for (data in snapshot.children) {
                        val recipe = data.getValue(RecipeModel::class.java)
                        recipe?.let {
                            allRecipes.add(it)
                        }
                    }
                    callback(true, allRecipes)
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
