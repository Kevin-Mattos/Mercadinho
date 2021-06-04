package com.example.mercadinho.repository.database.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem


@Dao
interface ShopDao {

    @Query("SELECT * FROM ShopGroup")
    fun getAllGroups(): LiveData<List<ShopGroup>>

    @Insert
    fun insertShopGroup(shopGroup: ShopGroup)

    @Query("SELECT * FROM shopitem where groupId = :shopGroupId")
    fun getAllItemsFromGroup(shopGroupId: Long): LiveData<List<ShopItem>>

    @Insert
    fun insertShopItem(shopItem: ShopItem)


}