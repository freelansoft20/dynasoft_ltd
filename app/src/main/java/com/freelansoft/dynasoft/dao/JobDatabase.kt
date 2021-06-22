package com.freelansoft.dynasoft.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.freelansoft.dynasoft.dto.Job

@Database(entities=arrayOf(Job::class), version = 1)
abstract  class JobDatabase : RoomDatabase() {
    abstract fun localJobDAO() : ILocalJobDAO
}