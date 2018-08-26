package com.example.a18mas.mp3front.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSetting(

        @ColumnInfo(name = "setting_key")
        var setting_key: String = "",

        @ColumnInfo(name = "setting_value")
        var setting_value: String = "",

        @ColumnInfo(name = "setting_description")
        var setting_description: String = ""

) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}