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

    companion object {
        fun fromMap(map: HashMap<String, Any>): ShopGroup {
            return ShopGroup(
                id = (map[ShopGroup::id.name] as String?).orEmpty(),
                name = (map[ShopGroup::name.name] as String?).orEmpty(),
                user = (map[ShopGroup::user.name] as String?).orEmpty(),
                description =(map[ShopGroup::description.name] as String?).orEmpty())
        }
    }
}