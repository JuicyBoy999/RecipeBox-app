package com.example.recipebox.repo

import com.example.recipebox.model.RecipeModel

interface RecipeRepo {

    fun addRecipe(
        model: RecipeModel,
        callback: (Boolean, String) -> Unit
    )

    fun updateRecipe(
        model: RecipeModel,
        callback: (Boolean, String) -> Unit
    )

    fun deleteRecipe(
        id: String,
        callback: (Boolean, String) -> Unit
    )

    fun getRecipeById(
        id: String,
        callback: (Boolean, RecipeModel?) -> Unit
    )

    fun getAllRecipes(
        callback: (Boolean, List<RecipeModel>?) -> Unit
    )
}
