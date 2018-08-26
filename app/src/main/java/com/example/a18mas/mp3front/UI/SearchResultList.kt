package com.example.a18mas.mp3front.UI

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a18mas.mp3front.helper.MyEventListener

import com.example.a18mas.mp3front.data.model.SearchResult
import com.example.a18mas.mp3front.R


class SearchResultList : Fragment() {

    /* RecyclerView */
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var data_search_result: Array<SearchResult>
    /* Helper */
    private var myEventListener: MyEventListener? = null
    var handler: Handler = Handler()
    var delay_until_show: Long = 3000
    var flag_is_initialized: Boolean = false


    fun setOnHaveToHideListener(eventListener: MyEventListener) {
        this.myEventListener = eventListener
    }

/*
    fun updateData(data: Array<SearchResult>) {
        activity!!.runOnUiThread {
            this.data_search_result = data
            (this.viewAdapter as AdapterSearchResultList).updateData(data)


            this.viewAdapter.notifyDataSetChanged()
            Log.i("UPDATE", "OnDoneSuccess-Data: ${data.size}:0. ${data[0]}")
            data_search_result?.forEach {Log.i("data====","${it.thumbnail}")}
            }


        Log.i("updateData", "data changed????")
    }*/


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_search_result_list, container, false)

        var q = arguments?.get("SEARCH_TERM") as String
        ///var init_data = arguments?.getParcelableArray("INIT_DATA")
        Log.i("SEARCH_TERM", q)
        ///Log.i("INIT_DATA", init_data.toString())
        flag_is_initialized = true

        //
        recyclerView = view.findViewById<RecyclerView>(R.id.recView)
        ////recyclerView = recView -error@view-binding-

        /// set initial data
        /// data_search_result = init_data as Array<SearchResult>//// listData as Array
        this.data_search_result = arguments?.getParcelableArray("INIT_DATA") as Array<SearchResult>
        Log.i("SearchResult", "initdata " + data_search_result.toString())


        viewManager = LinearLayoutManager(context!!) as RecyclerView.LayoutManager
        //  this.viewAdapter = AdapterSearchResultList(this.data_search_result, context!!, activity!!.currentFocus as View)
        //this.viewAdapter = MyItemRecyclerViewAdapter(this.data_search_result, )
        //(this.data_search_result, context!!, activity!!.currentFocus as View)


        //
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                delay_until_show = if (delay_until_show < 3000) delay_until_show + 500 else 3000

                if (!recyclerView.canScrollVertically(1)) {
                    Log.i("Scroll", "is buttom")
                    //  myEventListener?.OnHaveToHide(false) {}
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //// Log.i("DY", "" + dy)
                if (dy > 0) {
                    myEventListener?.OnHaveToHide(true) {}
                } else {
                    myEventListener?.OnHaveToHide(false) {}
                }
            }
        })
        /*   /// hide buttom player and activate handler to show ///
        handler.postDelayed(object : Runnable {
            override fun run() {
                recyclerView.setOnScrollChangeListener { view, i1, i2, i3, i4 ->
                    myEventListener?.OnHaveToHide(true) {}
                    Log.i("scroll", "$i1, $i2, $i3, $i4 | scrollX ${view.x},scrollY ${view.y}")
                    delay_until_show = if (delay_until_show < 3000) delay_until_show + 500 else 3000
                }
                //
                handler.post(unhideBottomPlayer)
            }
        }, 500)*/


        //
        return view
    }

    ///

/*  /// show buttom player ///
    val unhideBottomPlayer = object : Runnable {
        override fun run() {

            myEventListener?.OnHaveToHide(false) {}
            handler.postDelayed(this, delay_until_show)
        }
    }*/


}
