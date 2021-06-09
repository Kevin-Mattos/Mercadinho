package com.example.mercadinho.repository.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.lang.RuntimeException

@Entity
class ShopGroup(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "shop_id") val id: Long,@ColumnInfo(name = "name") val name: String) {
    override fun toString(): String {
        return "ShopGroup(id=$id, name='$name')"
    }

    fun validate() {
        if(name.isEmpty())
            throw RuntimeException("Name cannot be empty")
    }
}