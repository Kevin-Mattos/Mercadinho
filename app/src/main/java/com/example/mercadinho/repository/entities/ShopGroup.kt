package com.example.mercadinho.repository.entities

import java.io.Serializable


class ShopGroup(
    var id: String = "",
    var name: String,
    var user: String = "",
    var description: String = "",
) : Serializable {

    override fun toString(): String {
        return "ShopGroup(id=$id, name='$name')"
    }

    fun validate() {
        if (name.isEmpty())
            throw RuntimeException("Name cannot be empty")
    }
}