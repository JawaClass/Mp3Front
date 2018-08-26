package com.example.a18mas.mp3front

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.example.a18mas.mp3front.UI.ItemFragment.OnListFragmentInteractionListener
import com.example.a18mas.mp3front.data.model.SearchResult

import kotlinx.android.synthetic.main.fragment_item.view.*
import android.support.v7.util.DiffUtil
import android.util.Log
import android.widget.*
import com.example.a18mas.mp3front.UI.myContext
import com.example.a18mas.mp3front.data.AppDataManager
import com.example.a18mas.mp3front.data.AppDatabase
import com.example.a18mas.mp3front.data.SearchResultDAO
import com.example.a18mas.mp3front.helper.streamMP3
import com.squareup.picasso.Picasso


//// import kotlinx.android.synthetic.main.item_search_result.view.*


/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(_mValues: Array<SearchResult>, //// List<DummyItem>
                                mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    //  private var mySpinnerAdapter: MyItemSpinnerAdapter = MyItemSpinnerAdapter()

    private val mOnClickListener: View.OnClickListener
    private var mValues: Array<SearchResult>? = null

    private var mViewsDict: HashMap<String, ViewHolder> = hashMapOf()

    private var persistentVideoIDs: List<String>? = null

    fun checkAll() {

        this.mViewsDict!!.forEach {
            var video_id = it.key
            var view = it.value

            //view.mCheckBoxDownload.setOnCheckedChangeListener(null)

            if (!(video_id in persistentVideoIDs!!))
                if (!view.mCheckBoxDownload.isChecked) {
                    view.mCheckBoxDownload.toggle()
                    view.mCheckBoxDownload.setOnCheckedChangeListener(null)
                }

        }
        this.mValues!!.forEach {
            it.checked = it.video_id !in persistentVideoIDs!!
        }
        update(this.mValues!!)

    }

    fun getToDownloadItems(): ArrayList<Pair<*, *>> {
        var downloads_ids = arrayListOf<Pair<*, *>>()


        this.mValues?.filter { it.checked }?.forEach { downloads_ids.add(Pair(it.video_id, it)) }
        return downloads_ids
    }

    fun update(data: Array<SearchResult>) {
        //mValues = data

        val diffCallback = SearchResultListDiffCallback(this.mValues, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.mValues = data
        /// this.mValues = data


        diffResult.dispatchUpdatesTo(this)
    }


    init {

        persistentVideoIDs = AppDatabase.getDatabase().searchResultDao().allVideoIDs

        mValues = _mValues
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as SearchResult
            //  stream song after user click

            Log.i("stream", "stream song $v , $item")
            streamMP3(item)
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues!![position]
        //  model data
        holder.mIdView.text = position.toString()
        holder.mDurationView.text = item.duration_formatted
        holder.mTitleView.text = item.title///item.content
        holder.mChannelTitleView.text = item.channel_title///item.content


        mViewsDict.put(item.video_id, holder)
        var stat = mValues!![position].checked
        //in some cases, it will prevent unwanted situations
        holder.mCheckBoxDownload.setOnCheckedChangeListener(null)

        holder.mCheckBoxDownload.isChecked = stat

        holder.mCheckBoxDownload.setOnCheckedChangeListener { compoundButton, b ->

            item.checked = !item.checked
        }


        Picasso.with(myContext).load(mValues!![position].thumbnail?.get("medium")).fit().into(holder.mThumbnailView.thumbnail)


        if (item.video_id in persistentVideoIDs!!) {
            holder.mView.setBackgroundColor(Color.GREEN)
        } else {
            holder.mView.setBackgroundColor(Color.BLACK)
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues!!.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mTitleView: TextView = mView.title
        val mThumbnailView: ImageView = mView.thumbnail
        val mDurationView: TextView = mView.duration
        val mChannelTitleView: TextView = mView.channel_title
        //
        val mCheckBoxDownload: CheckBox = mView.checkBox_Download

        //  val mMoreVertView: ImageView = mView.more_vert
        //  val mMoreSpinnerView: Spinner = mView.more_spinner

        override fun toString(): String {
            return super.toString() + " '" + mTitleView.text + "'"
        }
    }
}
