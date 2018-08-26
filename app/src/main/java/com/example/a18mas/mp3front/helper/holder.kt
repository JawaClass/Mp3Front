package com.example.a18mas.mp3front.helper

import android.support.design.widget.Snackbar
import com.example.a18mas.mp3front.data.model.SearchResult


import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.a18mas.mp3front.MyExoPlayer
import com.example.a18mas.mp3front.UI.myContext

//  [0] nach Title
//  [1] nach Artist
var searchMode: Int = 0

var currentMeta: SearchResult? = null
var playerImpl: MyExoPlayer? = null
var httpClient: HttpClient? = null

private var applicationContext: Context? = null

fun getMyContext(): Context {
    return applicationContext as Context
}

fun setMyContext(c: Context) {
    applicationContext = c
}
/////////////////////////////////////

fun streamMP3(meta: SearchResult) {
    currentMeta = meta
    var uri = Uri.parse("http://192.168.178.63:5545/streamX?id=${meta.video_id}")
    playerImpl?.setNewSource(uri)
    Log.i("HELP", "new Source is set $uri.")
    Log.i("HELP", "player is init ${playerImpl != null}")
}

fun playLocalMP3(uri: Uri) {

    var mdr = MediaMetadataRetriever()
    try {
        mdr.setDataSource(myContext, uri) //setDataSource(uri.toString())

        var artist = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        var album = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        var albumArtist = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
        var title = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        //  currentMeta = SearchResult(title = title, channel_title = artist)// ..


        Log.i("HELP", "$artist, $album, $albumArtist, $title")
    } catch (b: Exception) {
        Log.i("EXCEPTION_", " $b.")
    }
    var fileName = uri.toString()
    fileName = fileName.substring(fileName.lastIndexOf("/"))
    fileName = fileName.substring(1, fileName.lastIndexOf("."))
    Log.i("HELP", "filename_URI $uri")
    fileName = fileName.replace("%20", " ")

    Log.i("HELP", "filename $fileName")

    currentMeta = SearchResult(title = fileName)
    playerImpl?.setNewSource(uri)
    Log.i("HELP", "new Source is set $uri.")
    Log.i("HELP", "player is init ${playerImpl != null}")
}


fun View.makeSnackbarMessage(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()


fun Context.makeToastMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun Context.makeLongToastMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()


