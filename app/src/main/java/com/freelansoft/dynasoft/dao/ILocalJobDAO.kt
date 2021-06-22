package com.freelansoft.dynasoft.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.freelansoft.dynasoft.dto.Job

@Dao
interface ILocalJobDAO {

    @Query("SELECT * FROM job")
    fun getAllJobs()  : LiveData<List<Job>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(jobs: ArrayList<Job>)

    @Delete
    fun delete(job: Job)

    @Insert
    fun save(job: Job)
}