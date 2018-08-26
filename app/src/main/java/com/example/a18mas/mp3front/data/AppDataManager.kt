package com.example.a18mas.mp3front.data

import android.arch.persistence.room.Room
import com.example.a18mas.mp3front.data.model.AppSetting
import com.example.a18mas.mp3front.data.model.SearchResult
import com.example.a18mas.mp3front.data.model.SearchResult_Table
import com.example.a18mas.mp3front.helper.getMyContext


class AppDataManager : DbHelper {


    var searchResultDAO: SearchResultDAO? = null
    var appSettingstDAO: AppSettingsDAO? = null

    companion object {

        var INSTANCE: AppDataManager? = null

        fun getAppDataManager(): AppDataManager {
            if (INSTANCE == null)
                INSTANCE = AppDataManager()
            return INSTANCE as AppDataManager

        }
    }

    init {


        searchResultDAO = AppDatabase.getDatabase().searchResultDao()
        appSettingstDAO = AppDatabase.getDatabase().appSettingstDao()

    }


    override fun saveSearchResult(searchResult: SearchResult_Table): Boolean {
        try {
            searchResultDAO?.insertAll(searchResult)

        } catch (e: Error) {
            return false
        }
        return true
    }

    override fun saveAppSetting(setting: AppSetting): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun allSearchResults(): List<SearchResult_Table> {
        return searchResultDAO!!.all
    }

    override fun allSettings(): List<AppSetting> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //TODO return appSettingstDAO!!.all
    }


}

