package com.example.doan3.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductModel::class], version = 1)
abstract class AppDataBase:RoomDatabase() {
    companion object{
        private var database: AppDataBase? = null
        private var DATABASE_NAME = "doan3"

        @Synchronized
        fun getInstance(context:Context):AppDataBase{
            if (database == null){
                database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return database!!
        }
    }
    abstract fun  productDao():ProductDao
}

