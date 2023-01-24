package com.merc.mercadao.di

import com.merc.mercadao.repository.EditItemRepository
import com.merc.mercadao.repository.ShopGroupDetailsRepository
import com.merc.mercadao.repository.ShopGroupRepository
import com.merc.mercadao.repository.ShopItemRepository
import com.merc.mercadao.repository.ShopRepositoryImpl
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
