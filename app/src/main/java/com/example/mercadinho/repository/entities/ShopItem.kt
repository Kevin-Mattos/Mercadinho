package com.example.mercadinho.repository.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ShopGroup::class,
            childColumns = ["groupId"],
            parentColumns = ["shop_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class ShopItem(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "item_id") val id: Long, val groupId: Long, @ColumnInfo(name = "name") val name: String) {
    override fun toString(): String {
        return "ShopItem(id=$id, groupId=$groupId, name='$name')"
    }
}