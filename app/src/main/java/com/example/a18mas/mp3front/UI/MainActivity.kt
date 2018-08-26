package com.example.a18mas.mp3front.UI

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.example.a18mas.mp3front.R
import com.example.a18mas.mp3front.helper.*
import com.example.a18mas.mp3front.data.model.SearchResult
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import com.example.a18mas.mp3front.MyExoPlayer
import com.example.a18mas.mp3front.data.AppDataManager
import com.example.a18mas.mp3front.data.AppDatabase
import com.example.a18mas.mp3front.data.SearchResultDAO
import com.example.a18mas.mp3front.data.model.SearchResult_Table


var myContext: Context? = null
var myActivity: Activity? = null


class MainActivity : AppCompatActivity(),
        SearchView.OnQueryTextListener,
        MyEventListener,
        MyHttpClientListener,
        ItemFragment.OnListFragmentInteractionListener, MyDownloadListener {
    override fun OnDownloadSuccess(searchResult: SearchResult, function: () -> Unit) {
        Log.i(TAG, "OnDownloadSuccess")

        var dbManager: AppDataManager = AppDataManager.getAppDataManager()
        var db: AppDatabase = AppDatabase.getDatabase()
        var searchResultDAO: SearchResultDAO = db.searchResultDao()

        dbManager.saveSearchResult(searchResult.getTable())

/*
        var srt = SearchResult_Table("borutoots", "boruto", "120",
                102, "120", "test",
                "test", "test", "test",
                "19950909")
        searchResultDAO.insertAll(srt)
*/
        var entries = searchResultDAO.all

        Log.i(TAG, "ENTRIES: ${entries.toString()}")

        entries.forEach {
            Log.i(TAG, "entry: ${it.toString()}")

        }

    }

    override fun OnDownloadFailed(searchResult: SearchResult, function: () -> Unit) {
        Log.i(TAG, "OnDownloadFailed ${searchResult.title}")
    }


    override fun onListFragmentInteraction(item: SearchResult?) {
        Log.i(TAG, "onListFragmentInteraction: $item")
    }


    override fun OnHaveToHide(hide: Boolean, function: () -> Unit) {
        ////Log.i(TAG, "onHaveToHide $hide")
        hideBottomPlayer(hide)
    }


    override fun OnSetNewDataSource(function: () -> Unit) {
        Log.i(TAG, "Main_actvity: OnSetNewDataSource")

    }


    override fun OnCompletion(function: () -> Unit) {
        ///Log.i(TAG, "OnCompletion")
    }

    private var item_Fragment: ItemFragment? = null

    var bottomPlayer_Fragment: BottomPlayer? = null
    private var main_Fragment: MainActivityFragment? = null
    private var searchResultList_Fragment: SearchResultList? = null
    private var TAG = "MAIN-ACTIVITY"
    private var searchView: SearchView? = null

    private val TAG_ITEM_FRAGMENT = "ITEM_FRAGMENT"
    private val TAG_MAIN_FRAGMENT = "MAIN_FRAGMENT"
    private val TAG_BOTTOM_FRAGMENT = "BOTTOM_FRAGMENT"


    fun openWith(intent: Intent) {
        val action = intent.action
        val type = intent.type

        Log.i("MAIN", "action \"$action\"")
        Log.i("MAIN", "type \"$type\"")
        Log.i("MAIN", "data \"${intent.data}\"")
        Log.i("MAIN", "dataString \"${intent.dataString}\"")


        Log.i("MAIN", "intent \"$intent\"")
        playLocalMP3(intent.data)

    }

    private fun init() {


        setSupportActionBar(toolbar)
        myContext = this
        setMyContext(this)
        myActivity = this

        fab.setOnClickListener { view ->
            view?.makeSnackbarMessage("make message!!!!!!!")
            ///hideBottomPlayer()
        }


        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (fragment_container_bottom != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.


            // Create a new Fragment to be placed in the activity layout
            // 
            bottomPlayer_Fragment = BottomPlayer()
            bottomPlayer_Fragment?.setArguments(intent.extras)

            replaceFragment(R.id.fragment_container_bottom, bottomPlayer_Fragment!!, TAG_BOTTOM_FRAGMENT)

            main_Fragment = MainActivityFragment()
            replaceFragment(R.id.fragment_container_center, main_Fragment!!, TAG_MAIN_FRAGMENT)
        }
        MyExoPlayer()
        //  open with...
        if (intent.data != null) {
            /// Thread.sleep(5000)
            openWith(intent)
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("MAIN", "ON_CREATE.")

        if (savedInstanceState != null) {
            return
        } else
            init()


    }

    override fun OnFailed(exception: Exception, function: () -> Unit) {
        Log.i(TAG, "OnFailed")
        this@MainActivity.runOnUiThread(java.lang.Runnable {
            myContext?.makeLongToastMessage("Connection Error")

        })
    }

    /// one query done
    override fun OnDoneSuccess(data: Array<SearchResult>, function: () -> Unit) {
        this@MainActivity.runOnUiThread(java.lang.Runnable {

            myContext?.makeLongToastMessage("Update Search Result")

            Log.i(TAG, "OnDoneSuccess")
            if (item_Fragment == null) {
                Log.i(TAG, "INIT...")

                item_Fragment = ItemFragment()
                val args = Bundle()
                args.putParcelableArray("init-data", data)

                item_Fragment?.arguments = args

                replaceFragment(R.id.fragment_container_center, item_Fragment!!, TAG_ITEM_FRAGMENT)

            } else {
                Log.i(TAG, "UPDATE...")
                var fr = supportFragmentManager.findFragmentById(R.id.linearLayout)
                if (fr == null || !fr?.isVisible) {
                    replaceFragment(R.id.fragment_container_center, item_Fragment!!, TAG_ITEM_FRAGMENT)
                }

                item_Fragment?.update(data)
            }
        })
    }

    fun replaceFragment(containerInt: Int, fragment: Fragment, TAG: String) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().replace(containerInt,
                fragment, TAG).addToBackStack(TAG).commitAllowingStateLoss()

        //supportFragmentManager.beginTransaction().replace(containerInt,
        //              fragment, TAG).commitNowAllowingStateLoss()
        Log.i(TAG, "replaceFragment")

    }

    /// handle query and start search result fragment
    override fun onQueryTextSubmit(p0: String?): Boolean {
        // data
        if (httpClient == null) {
            httpClient = HttpClient()
            httpClient?.setOnDoneSuccess(this)
            httpClient?.setOnDownloadSuccess(this)


        }

        httpClient?.fetchSearchResultData(p0 as String)
        // TODO Loader

        Log.i("search", "onSubmit")
        closeKeyboard()


        // Create fragment and give it an argument specifying the article it should
        if (searchResultList_Fragment == null) {
            searchResultList_Fragment = SearchResultList()
            searchResultList_Fragment?.setOnHaveToHideListener(this)


        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        Log.i("search", "onChange")
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)


        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView?.setOnQueryTextListener(this)
        searchView?.queryHint = "Title:"

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_home -> {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_center, main_Fragment as MainActivityFragment, TAG_MAIN_FRAGMENT).commit()
            true
        }

        R.id.action_settings -> {
            true
        }

        R.id.action_back -> {
            supportFragmentManager.popBackStackImmediate()
            true
        }


        R.id.by_title -> {
            Log.i("by_title", "${item.itemId}")
            searchMode = 0
            searchView?.queryHint = "Title:"

            this@MainActivity.runOnUiThread(java.lang.Runnable {
                myContext?.makeLongToastMessage("Title Search")

            })


            true
        }
        R.id.by_artist -> {
            Log.i("by_artist", "${item.itemId}")
            searchMode = 1
            searchView?.queryHint = "Artist:"
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                myContext?.makeLongToastMessage("Artist Search")

            })
            true
        }

        R.id.action_download -> {
            httpClient?.download(item_Fragment?.download()!!)
            true
        }

        R.id.action_check_all -> {
            item_Fragment?.check_all()

            true
        }

        R.id.action_search -> {
            // search logic...

            //
            super.onOptionsItemSelected(item)
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }


    }

    fun closeKeyboard() {
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
    }

    fun hideBottomPlayer(hide: Boolean) {
        ///Log.i(TAG, "visible=${bottomPlayer_Fragment?.isVisible}")

        when (hide) {
            true -> {
                if (bottomPlayer_Fragment?.isVisible == true)
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in).hide(bottomPlayer_Fragment as Fragment).commit()
            }
            false -> {
                ////Log.i(TAG, "SHOW")

                if (bottomPlayer_Fragment?.isVisible == false)
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).show(bottomPlayer_Fragment as Fragment).commit()

            }

        }

    }
}


