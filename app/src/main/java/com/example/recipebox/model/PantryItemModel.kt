package com.example.recipebox.model

data class PantryItemModel(
    var itemId: String = "",
    var name: String = "",
    var quantity: Double = 0.0,
    var unit: String = "",
    var userId: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "quantity" to quantity,
            "unit" to unit,
            "userId" to userId,
        )
    }
}
