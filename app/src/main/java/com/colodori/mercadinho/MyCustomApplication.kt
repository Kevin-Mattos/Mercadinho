package com.colodori.mercadinho

import android.app.Application
import android.content.Context
import com.colodori.mercadinho.repository.local.LocalSharedPref
import com.colodori.mercadinho.repository.local.USER_PREF
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyCustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LocalSharedPref.sharedPreferences = applicationContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
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