package com.freelansoft.dynasoft.service

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.room.Room
import com.freelansoft.dynasoft.dao.ILocalJobDAO
import com.freelansoft.dynasoft.dao.JobDatabase
import com.freelansoft.dynasoft.dto.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class JobService(application: Application) {
    private val application = application

    internal suspend fun fetchJobs(jobName: String) {
        withContext(Dispatchers.IO) {
//            updateLocalJobs(jobs)

        }
    }

    /**
     * Store these Jobs locally, so that we can use the data without network latency
     */
    private suspend fun updateLocalJobs(jobs: ArrayList<Job>?) {
        var sizeOfJobs = jobs?.size
        try {
            var localJobDAO = getLocalJobDAO()
            localJobDAO.insertAll(jobs!!)
        }catch (e: Exception) {
            e.message?.let { Log.e(ContentValues.TAG, it) }
        }

    }

    internal fun getLocalJobDAO() : ILocalJobDAO {
        val db = Room.databaseBuilder(application, JobDatabase::class.java, "mydiary").build()
        val localJobDAO = db.localJobDAO()
        return localJobDAO
    }

    internal fun save(job:Job) {
        getLocalJobDAO().save(job)
    }
}