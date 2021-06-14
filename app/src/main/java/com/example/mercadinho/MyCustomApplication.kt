package com.example.mercadinho

import android.app.Application
import androidx.room.Room
import com.example.mercadinho.repository.ShopRepository
import com.example.mercadinho.repository.database.shop.ShopDatabaseManager
import com.example.mercadinho.repository.database.shop.ShopDao
import com.example.mercadinho.repository.database.shop.ShopDatabase
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

@HiltAndroidApp
class MyCustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        startKoin {
//            androidContext(applicationContext)
//            modules(mainModule)
//        }
    }

//    private val mainModule = module {
//        single<ShopDatabase> {
//            Room.databaseBuilder(
//                androidApplication(),
//                ShopDatabase::class.java, "shop.db"
//            )
//                .fallbackToDestructiveMigration()
//                .build()
//        }
//
//        single<ShopDao> { get<ShopDatabase>().shopDao() }
//
//        single<ShopDatabaseManager> {ShopDatabaseManager(get()) }
//
//        single<ShopRepository> { ShopRepository(get()) }
//
//
//
//    }

}