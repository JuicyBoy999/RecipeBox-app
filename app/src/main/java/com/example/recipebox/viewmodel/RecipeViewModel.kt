package com.example.recipebox.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipebox.model.RecipeModel
import com.example.recipebox.repo.RecipeRepo

class RecipeViewModel(val repo: RecipeRepo) : ViewModel() {

    fun addRecipe(
        model: RecipeModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addRecipe(model, callback)
    }

    fun updateRecipe(
        model: RecipeModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.updateRecipe(model, callback)
    }

    fun deleteRecipe(
        id: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.deleteRecipe(id, callback)
    }

    private val _recipe = MutableLiveData<RecipeModel?>()
    val recipe: MutableLiveData<RecipeModel?> get() = _recipe

    private val _allRecipes = MutableLiveData<List<RecipeModel>?>()
    val allRecipes: MutableLiveData<List<RecipeModel>?> get() = _allRecipes

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    fun getRecipeById(id: String) {
        _loading.value = true
        repo.getRecipeById(id) { success, data ->
            _recipe.value = if (success) data else null
            _loading.value = false
        }
    }

    fun getAllRecipes() {
        _loading.value = true
        repo.getAllRecipes { success, data ->
            _allRecipes.value = if (success) data else emptyList()
            _loading.value = false
        }
    }
}
