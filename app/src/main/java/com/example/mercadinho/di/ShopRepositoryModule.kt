package com.example.mercadinho.di

import com.example.mercadinho.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ShopModule {
    @Binds
    abstract fun bindShopGroupRepository(
        shopRepositoryImpl: ShopRepositoryImpl
    ): ShopGroupRepository

    @Binds
    abstract fun bindShopItemRepository(
        shopRepositoryImpl: ShopRepositoryImpl
    ): ShopItemRepository
    @Binds
    abstract fun bindShopGroupDetailsRepository(
        shopRepositoryImpl: ShopRepositoryImpl
    ): ShopGroupDetailsRepository
    @Binds
    abstract fun bindEditItemRepository(
        shopRepositoryImpl: ShopRepositoryImpl
    ): EditItemRepository
}
