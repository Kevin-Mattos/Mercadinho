package com.example.mercadinho.repository.database.shop

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single


@Dao
interface ShopDao {

    @Query("SELECT * FROM ShopGroup")
    fun getAllGroups(): Flowable<List<ShopGroup>>

    @Query("SELECT * FROM ShopGroup where name like '%'||:query||'%'")
    fun getAllGroups2(query: String): Single<List<ShopGroup>>

    @Insert
    fun insertShopGroup(shopGroup: ShopGroup): Completable

    @Query("DELETE FROM ShopGroup")
    fun deleteAllGroups(): Completable

    @Delete
    fun deleteGroup(shopGroup: ShopGroup): Completable

    @Query("DELETE FROM ShopItem where groupId = :groupId")
    fun deleteAllItemsFromGroup(groupId: Long): Completable

    @Query("SELECT * FROM ShopItem")
    fun getAllItems(): LiveData<List<ShopItem>>

    @Query("SELECT * FROM ShopItem where groupId = :shopGroupId")
    fun getAllItemsFromGroup(shopGroupId: Long): Flowable<List<ShopItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShopItem(shopItem: ShopItem): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllItens(shopItems: List<ShopItem>): Completable

    @Delete
    fun removeItem(shopItem: ShopItem): Completable
}