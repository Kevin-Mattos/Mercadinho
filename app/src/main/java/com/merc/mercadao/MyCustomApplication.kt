package com.merc.mercadao

import android.app.Application
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.merc.mercadao.repository.local.LocalSharedPref
import com.merc.mercadao.repository.local.USER_PREF
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