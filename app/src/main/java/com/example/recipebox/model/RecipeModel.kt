package com.example.recipebox.model

data class RecipeModel(
    var recipeId: String = "",
    var title: String = "",
    var ingredients: String = "",
    var instructions: String = "",
    var cookTimeMinutes: Int = 0,
    var category: String = "",
    var isFavorite: Boolean = false,
    var userId: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "ingredients" to ingredients,
            "instructions" to instructions,
            "cookTimeMinutes" to cookTimeMinutes,
            "category" to category,
            "isFavorite" to isFavorite,
            "userId" to userId,
        )
    }
}
