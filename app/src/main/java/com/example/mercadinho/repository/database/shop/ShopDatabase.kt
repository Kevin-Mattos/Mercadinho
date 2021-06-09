package com.example.mercadinho.repository.database.shop

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem


@Database(entities = [ShopGroup::class, ShopItem::class], version = 1)
abstract class ShopDatabase: RoomDatabase() {

    abstract fun shopDao(): ShopDao

}