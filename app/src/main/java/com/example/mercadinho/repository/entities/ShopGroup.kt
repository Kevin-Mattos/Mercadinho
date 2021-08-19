package com.example.mercadinho.repository.entities


class ShopGroup(
    var id: String? = null,
    var name: String,
    var user: String = ""
) {


    override fun toString(): String {
        return "ShopGroup(id=$id, name='$name')"
    }

    fun validate() {
        if (name.isEmpty())
            throw RuntimeException("Name cannot be empty")
    }
}