package com.smarttoni.database

import android.content.Context

import com.smarttoni.MainApplication
import com.smarttoni.entities.DaoMaster
import com.smarttoni.entities.DaoMaster.dropAllTables
import com.smarttoni.entities.DaoSession


import org.greenrobot.greendao.database.Database

class DbOpenHelper(context: Context, name: String) : DaoMaster.OpenHelper(context, name) {

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        //super.onUpgrade(db, oldVersion, newVersion)
        dropAllTables(db, true)
        onCreate(db)
    }

    companion object {
        val DB_NAME = "smartoni.db"
        fun getDaoSession(context: Context): DaoSession {
            return (context.applicationContext as MainApplication).daoSession
        }
    }
}
