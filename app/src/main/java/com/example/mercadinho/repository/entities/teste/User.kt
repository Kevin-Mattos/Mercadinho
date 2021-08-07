package com.example.mercadinho.repository.entities.teste

data class User(val userID: String,val nome: String)

data class UserGroup(val userId: String, val users: Map<String, Boolean>)

data class GroupUser(val groupId: String, val users: Map<String, Boolean>)

data class Grupos(val groupId: String, var groupName: String)

data class Item(val itemId: String, val nome: String)

