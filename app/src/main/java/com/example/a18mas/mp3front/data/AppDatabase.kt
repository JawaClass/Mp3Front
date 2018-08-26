package com.example.a18mas.mp3front.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.example.a18mas.mp3front.data.model.SearchResult_Table
import com.example.a18mas.mp3front.helper.getMyContext
import kotlin.jvm.java


@Database(entities = arrayOf(SearchResult_Table::class), version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {


    abstract fun searchResultDao(): SearchResultDAO

    abstract fun appSettingstDao(): AppSettingsDAO

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val DB_NAME = "search_result_db"

        // internal
        fun getDatabase(): AppDatabase {
            if (INSTANCE == null) {
                // synchronized(AppDatabase::class.java) {

                // INSTANCE = Room.databaseBuilder(getMyContext(),
                //       AppDatabase::class.java, "my_database")
                //     .build()

                INSTANCE = Room.databaseBuilder(getMyContext(),
                        AppDatabase::class.java!!, DB_NAME).allowMainThreadQueries()
                        .build()


                //}
            }
            return INSTANCE as AppDatabase
        }
    }

}