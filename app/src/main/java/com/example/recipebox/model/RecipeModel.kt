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
            // Firebase's reflection-based mapper serializes Kotlin's `isFavorite`
            // getter under the key "favorite" (strips the "is" prefix), which is
            // what setValue()/getValue() actually read and write elsewhere. This
            // key must match that, not the Kotlin property name, or updates here
            // silently write to a field nothing else ever reads.
            "favorite" to isFavorite,
            "userId" to userId,
        )
    }
}
