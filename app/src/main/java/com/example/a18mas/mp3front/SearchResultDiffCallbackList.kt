package com.example.a18mas.mp3front

import android.support.v7.util.DiffUtil
import com.example.a18mas.mp3front.data.model.SearchResult

class SearchResultListDiffCallback(private var mOldList: Array<SearchResult>?, private var mNewList: Array<SearchResult>?) : DiffUtil.Callback() {


    override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
        // Log.i("areItemsTheSame: ", "new ${mNewList?.size}: ${p1},,,\"old ${mOldList?.size}: ${p0}")
        return mNewList?.get(p1)?.video_id == mOldList?.get(p0)?.video_id
    }

    override fun getOldListSize(): Int {
        return if (mOldList != null) mOldList!!.size else 0
    }

    override fun getNewListSize(): Int {
        return if (mNewList != null) mNewList!!.size else 0
    }

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
        return mNewList?.get(p1)?.equals(mOldList?.get(p0))!!; }

}