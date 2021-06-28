package com.freelansoft.dynasoft.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.freelansoft.dynasoft.dto.Service

@Database(entities=arrayOf(Service::class), version = 1)
abstract  class ServiceDatabase : RoomDatabase() {
    abstract fun localServiceDAO() : ILocalServiceDAO
}