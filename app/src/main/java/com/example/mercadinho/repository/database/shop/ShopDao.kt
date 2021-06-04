package com.example.mercadinho.repository.database.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mercadinho.repository.entities.ShopGroup


@Dao
interface ShopDao {

    @Query("SELECT * FROM ShopGroup")
    fun getAll(): LiveData<List<ShopGroup>>

    @Insert
    fun insertShopGroup(shopGroup: ShopGroup)


}