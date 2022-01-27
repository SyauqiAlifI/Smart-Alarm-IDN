package com.alif.smartalarm.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alif.smartalarm.data.Alarm

@Dao
interface AlarmDao {
    @Insert
    fun addAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm")
    fun getAlarm() : List<Alarm>
}