package com.example.mercadinho

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

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