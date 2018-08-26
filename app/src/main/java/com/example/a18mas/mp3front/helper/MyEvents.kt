package com.example.a18mas.mp3front.helper

import com.example.a18mas.mp3front.data.model.SearchResult


interface MyEventListener {

    fun OnSetNewDataSource(function: () -> Unit)

    fun OnHaveToHide(hide: Boolean, function: () -> Unit)

    fun OnCompletion(function: () -> Unit)

}

interface MyDownloadListener {
    fun OnDownloadSuccess(searchResult: SearchResult, function: () -> Unit)
    fun OnDownloadFailed(searchResult: SearchResult, function: () -> Unit)

}

interface MyHttpClientListener {

    fun OnDoneSuccess(data: Array<SearchResult>, function: () -> Unit)
    fun OnFailed(exception: Exception, function: () -> Unit)

}


interface PlayerEventListener {
    fun OnSetNewDataSource(function: () -> Unit)


    fun OnCompletion(function: () -> Unit)

}


