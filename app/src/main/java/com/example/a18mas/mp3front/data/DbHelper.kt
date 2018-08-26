package com.example.a18mas.mp3front.data

import com.example.a18mas.mp3front.data.model.AppSetting
import com.example.a18mas.mp3front.data.model.SearchResult
import com.example.a18mas.mp3front.data.model.SearchResult_Table

interface DbHelper {

    fun allSearchResults(): List<SearchResult_Table>

    fun allSettings(): List<AppSetting>

    fun saveSearchResult(searchResult: SearchResult_Table): Boolean

    fun saveAppSetting(setting: AppSetting): Boolean


}

