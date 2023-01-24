package com.colodori.mercadinho.di

import com.colodori.mercadinho.repository.EditItemRepository
import com.colodori.mercadinho.repository.ShopGroupDetailsRepository
import com.colodori.mercadinho.repository.ShopGroupRepository
import com.colodori.mercadinho.repository.ShopItemRepository
import com.colodori.mercadinho.repository.ShopRepositoryImpl
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
