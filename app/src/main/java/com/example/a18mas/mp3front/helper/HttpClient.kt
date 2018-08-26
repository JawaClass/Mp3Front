package com.example.a18mas.mp3front.helper

import android.os.Environment
import android.util.Log
import com.example.a18mas.mp3front.data.model.SearchResult
import com.example.a18mas.mp3front.UI.myContext
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL
import kotlinx.coroutines.experimental.async
import java.io.File
import java.net.ConnectException

class HttpClient() {

    private val jsonFactory = Gson()
    // val DOWNLOAD_DIR_PATH = "/sdcard/Music/MP3DOWNLOADS/"
    val DOWNLOAD_DIR_PATH = "${Environment.getExternalStorageDirectory().path}Music/MP3DOWNLOADS/"
    // https://my.noip.com/#!/dynamic-dns
    //  expires every 30 days...
    val domain = "http://tatooine9000.ddns.net:5545"
    //  val domain = "http://192.168.178.63:5545"


    fun preQuest() {
        if (!File(DOWNLOAD_DIR_PATH).isDirectory)
            File(DOWNLOAD_DIR_PATH).mkdir()
    }

    init {
        Log.i("HTTP_CLIENT", "INIT")
        preQuest()


    }

    private var myHttpClientListener: MyHttpClientListener? = null

    private var myDownloadListener: MyDownloadListener? = null


    fun setOnDoneSuccess(eventListener: MyHttpClientListener) {
        this.myHttpClientListener = eventListener
    }

    fun setOnDownloadSuccess(eventListener: MyDownloadListener) {
        this.myDownloadListener = eventListener
    }

    //


    fun download(videoid_searchResult: ArrayList<Pair<*, *>>) {
        for (id_sr in videoid_searchResult) {
            var id: String = id_sr.first as String
            var searchResult: SearchResult = id_sr.second as SearchResult
            Log.i("HTTP_CLIENT", "sr=${searchResult}")

            var url = "$domain/download?id=${id}&title=${(searchResult.title).replace(" ", "+")}"

            Fuel.download(url).destination { response, url ->
                File.createTempFile(searchResult.title, ".webm", File(DOWNLOAD_DIR_PATH))
            }.response { req, res, result ->

                when (result) {
                    is Result.Failure -> {
                        Log.d("HTTP_CLIENT", "nicht ok:" + result.getException().toString())
                        myContext?.makeLongToastMessage("Download failed \"${searchResult.title}\"")
                        myDownloadListener?.OnDownloadFailed(searchResult) {}

                    }
                    is Result.Success -> {
                        Log.d("HTTP_CLIENT", "OK.")
                        myContext?.makeLongToastMessage("File \"${searchResult.title}\"stored in ${DOWNLOAD_DIR_PATH}")
                        myDownloadListener?.OnDownloadSuccess(searchResult) {}

                    }

                }

            }

        }

    }

    //

    fun fetchSearchResultData(query: String) {
        Log.i("Request", "fetchSearchResultData")
        query.replace(" ", "+")
        var endpoint = ""
        when (searchMode) {
            0 -> {
                endpoint = "http://192.168.178.63:5545/apisearch?q="

            }
            1 -> {
                endpoint = "http://192.168.178.63:5545/artist?q="
            }
            else -> {
            }

        }

        async {

            val result = try {
                URL(endpoint + query.replace(" ", "+")).readText()

            } catch (e: ConnectException) {
                e
            }

            when (result) {
                is String -> {
                    Log.i("HTTP_CLIENT", "parse json")
                    try {
                        val listSearchResult = jsonFactory.fromJson<Array<SearchResult>>(result)
                        myHttpClientListener?.OnDoneSuccess(listSearchResult) {}

                    } catch (e: Exception) {
                        Log.i("EXCEPTION JSON", "says: $e")

                    }


                }
                is ConnectException -> {
                    myHttpClientListener?.OnFailed(result) {}
                }
                else -> {
                    /// ...
                }
            }


        }

    }

    // https://kotlinlang.org/docs/reference/inline-functions.html
    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

}