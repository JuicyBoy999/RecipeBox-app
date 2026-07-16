package com.example.recipebox.repo

import com.example.recipebox.model.UserModel

interface UserRepo {

    fun login(
        email: String, password: String,
        callback: (Boolean, String) -> Unit
    )

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    )

    fun getUserById(
        id: String,
        callback: (Boolean, String, UserModel?) -> Unit
    )

    fun getAllUser(callback: (Boolean, String, List<UserModel?>) -> Unit)

    fun logout(callback: (Boolean, String) -> Unit)

    fun register(
        email: String, password: String,
        callback: (Boolean, String, String) -> Unit
    )

    fun addUser(id: String, model: UserModel, callback: (Boolean, String) -> Unit)

    fun editProfile(id: String, model: UserModel, callback: (Boolean, String) -> Unit)

    fun deleteUser(id: String,callback: (Boolean, String) -> Unit)
}
