package com.example.mercadinho.di

import com.example.mercadinho.repository.ShopGroupRepository
import com.example.mercadinho.repository.ShopItemRepository
import com.example.mercadinho.repository.ShopRepositoryImpl
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
}
