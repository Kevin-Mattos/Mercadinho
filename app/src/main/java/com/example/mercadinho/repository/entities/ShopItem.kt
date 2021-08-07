package com.example.mercadinho.repository.entities

import androidx.room.Entity
import androidx.room.ForeignKey

data class ShopItem(
    val groupId: String,
    val name: String,
    var bought: Boolean
) {
    override fun toString(): String {
        return "ShopItem(id=$id, groupId=$groupId, name='$name')"
    }
    var id: String = ""
    companion object {
        fun fromMap(id: String, map: Map<String, Any>) = ShopItem(
            map["groupId"] as String,
            map["name"] as String,
            map["bought"] as Boolean
        ).apply { this.id = id }
    }

}