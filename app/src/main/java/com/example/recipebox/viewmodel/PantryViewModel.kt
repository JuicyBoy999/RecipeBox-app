package com.example.recipebox.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipebox.model.PantryItemModel
import com.example.recipebox.repo.PantryRepo

class PantryViewModel(val repo: PantryRepo) : ViewModel() {

    fun addItem(
        model: PantryItemModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addItem(model, callback)
    }

    fun updateItem(
        model: PantryItemModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.updateItem(model, callback)
    }

    fun deleteItem(
        id: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.deleteItem(id, callback)
    }

    private val _item = MutableLiveData<PantryItemModel?>()
    val item: MutableLiveData<PantryItemModel?> get() = _item

    private val _allItems = MutableLiveData<List<PantryItemModel>?>()
    val allItems: MutableLiveData<List<PantryItemModel>?> get() = _allItems

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    fun getItemById(id: String) {
        _loading.value = true
        repo.getItemById(id) { success, data ->
            _item.value = if (success) data else null
            _loading.value = false
        }
    }

    fun getAllItems() {
        _loading.value = true
        repo.getAllItems { success, data ->
            _allItems.value = if (success) data else emptyList()
            _loading.value = false
        }
    }
}
