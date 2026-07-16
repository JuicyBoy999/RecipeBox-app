package com.example.recipebox

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.recipebox.model.RecipeModel
import com.example.recipebox.repo.RecipeRepo
import com.example.recipebox.viewmodel.RecipeViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RecipeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val sampleRecipe = RecipeModel(
        recipeId = "r1",
        title = "Pancakes",
        ingredients = "Flour\nEggs\nMilk",
        instructions = "Mix and cook",
        cookTimeMinutes = 15,
        category = "Breakfast",
        isFavorite = false,
        userId = "u1"
    )

    @Test
    fun addRecipe_success_test() {
        val repo = mock<RecipeRepo>()
        val viewModel = RecipeViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Recipe added successfully")
            null
        }.`when`(repo).addRecipe(eq(sampleRecipe), any())

        var successResult = false
        var messageResult = ""

        viewModel.addRecipe(sampleRecipe) { success, msg ->
            successResult = success
            messageResult = msg
        }

        assertTrue(successResult)
        assertEquals("Recipe added successfully", messageResult)
        verify(repo).addRecipe(eq(sampleRecipe), any())
    }

    @Test
    fun updateRecipe_success_test() {
        val repo = mock<RecipeRepo>()
        val viewModel = RecipeViewModel(repo)
        val updated = sampleRecipe.copy(isFavorite = true)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Recipe updated successfully")
            null
        }.`when`(repo).updateRecipe(eq(updated), any())

        var successResult = false
        var messageResult = ""

        viewModel.updateRecipe(updated) { success, msg ->
            successResult = success
            messageResult = msg
        }

        assertTrue(successResult)
        assertEquals("Recipe updated successfully", messageResult)
        verify(repo).updateRecipe(eq(updated), any())
    }

    @Test
    fun deleteRecipe_success_test() {
        val repo = mock<RecipeRepo>()
        val viewModel = RecipeViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Recipe deleted successfully")
            null
        }.`when`(repo).deleteRecipe(eq("r1"), any())

        var successResult = false
        var messageResult = ""

        viewModel.deleteRecipe("r1") { success, msg ->
            successResult = success
            messageResult = msg
        }

        assertTrue(successResult)
        assertEquals("Recipe deleted successfully", messageResult)
        verify(repo).deleteRecipe(eq("r1"), any())
    }

    @Test
    fun getAllRecipes_success_test() {
        val repo = mock<RecipeRepo>()
        val viewModel = RecipeViewModel(repo)
        val recipes = listOf(sampleRecipe)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, List<RecipeModel>?) -> Unit>(0)
            callback(true, recipes)
            null
        }.`when`(repo).getAllRecipes(any())

        viewModel.getAllRecipes()

        assertEquals(recipes, viewModel.allRecipes.value)
        assertFalse(viewModel.loading.value ?: true)
        verify(repo).getAllRecipes(any())
    }

    @Test
    fun getRecipeById_success_test() {
        val repo = mock<RecipeRepo>()
        val viewModel = RecipeViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, RecipeModel?) -> Unit>(1)
            callback(true, sampleRecipe)
            null
        }.`when`(repo).getRecipeById(eq("r1"), any())

        viewModel.getRecipeById("r1")

        assertEquals(sampleRecipe, viewModel.recipe.value)
        assertFalse(viewModel.loading.value ?: true)
        verify(repo).getRecipeById(eq("r1"), any())
    }

    @Test
    fun getRecipeById_failure_test() {
        val repo = mock<RecipeRepo>()
        val viewModel = RecipeViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, RecipeModel?) -> Unit>(1)
            callback(false, null)
            null
        }.`when`(repo).getRecipeById(eq("missing"), any())

        viewModel.getRecipeById("missing")

        assertNull(viewModel.recipe.value)
        assertFalse(viewModel.loading.value ?: true)
    }
}
