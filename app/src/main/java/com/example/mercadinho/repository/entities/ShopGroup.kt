package com.example.mercadinho.repository.entities


class ShopGroup(
    val name: String,
    var user: String = ""
) {
    var id: String = ""

    override fun toString(): String {
        return "ShopGroup(id=$id, name='$name')"
    }

    fun validate() {
        if (name.isEmpty())
            throw RuntimeException("Name cannot be empty")
    }

    companion object {
        fun fromHash(map1: String, map: HashMap<String, Any>): ShopGroup{
            return ShopGroup(map["name"] as String, map["user"] as String).apply { id = map1 }
        }
    }
}