package com.example.a18mas.mp3front.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.a18mas.mp3front.data.model.AppSetting

@Dao
interface AppSettingsDAO {
    ///@get:Query("SELECT * FROM app_settings")
    ///val all: List<AppSetting>

}
