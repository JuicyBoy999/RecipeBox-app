package com.example.recipebox.repo

import com.example.recipebox.model.PantryItemModel

interface PantryRepo {

    fun addItem(
        model: PantryItemModel,
        callback: (Boolean, String) -> Unit
    )

    fun updateItem(
        model: PantryItemModel,
        callback: (Boolean, String) -> Unit
    )

    fun deleteItem(
        id: String,
        callback: (Boolean, String) -> Unit
    )

    fun getItemById(
        id: String,
        callback: (Boolean, PantryItemModel?) -> Unit
    )

    fun getAllItems(
        callback: (Boolean, List<PantryItemModel>?) -> Unit
    )
}
