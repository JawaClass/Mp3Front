package com.example.a18mas.mp3front.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.a18mas.mp3front.data.model.SearchResult_Table

@Dao
interface SearchResultDAO {
    @get:Query("SELECT * FROM search_results")
    val all: List<SearchResult_Table>

    @get:Query("SELECT video_id FROM search_results")
    val allVideoIDs: List<String>

    @Query("SELECT * FROM search_results WHERE video_id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<SearchResult_Table>

    @Query("SELECT * FROM search_results WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): SearchResult_Table

    @Insert
    fun insertAll(vararg searchResults: SearchResult_Table)

    @Delete
    fun delete(searchResult: SearchResult_Table)
}
