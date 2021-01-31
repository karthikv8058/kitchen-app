package com.smarttoni.devtools.database

import android.content.Context
import androidx.room.Room

//object database {
//    lateinit var appDatabase: AppDatabase
//
//    fun getDatabse(context: Context):AppDatabase{
//        if(!::appDatabase.isInitialized){
//            appDatabase = Room.databaseBuilder(context,AppDatabase::class.java,"smartoni.db")
//                    .addMigrations(AppDatabase.MIGRATION_1_2)
//                    .build()
//        }
//        return appDatabase
//    }
//}