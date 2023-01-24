package com.colodori.mercadinho.repository.entities

data class UserInfo(
    val id: String? = null,
    val name: String? = null,
    val nickName: String? = null,
    val profileUrl: String? = null,
    val isAdmin: Boolean = false,
)