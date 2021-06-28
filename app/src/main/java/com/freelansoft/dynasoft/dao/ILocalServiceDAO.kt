package com.freelansoft.dynasoft.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.freelansoft.dynasoft.dto.Service

@Dao
interface ILocalServiceDAO {

    @Query("SELECT * FROM service")
    fun getAllJobs()  : LiveData<List<Service>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(services: ArrayList<Service>)

    @Delete
    fun delete(service: Service)

    @Insert
    fun save(service: Service)
}