package com.freelansoft.dynasoft.service

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.room.Room
import com.freelansoft.dynasoft.dao.ILocalServiceDAO
import com.freelansoft.dynasoft.dao.ServiceDatabase
import com.freelansoft.dynasoft.dto.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServiceService(application: Application) {
    private val application = application

    internal suspend fun fetchServices(serviceName: String) {
        withContext(Dispatchers.IO) {
//            updateLocalServices(services)

        }
    }

    /**
     * Store these Services locally, so that we can use the data without network latency
     */
    private suspend fun updateLocalServices(services: ArrayList<Service>?) {
        var sizeOfServices = services?.size
        try {
            var localServiceDAO = getLocalServiceDAO()
            localServiceDAO.insertAll(services!!)
        }catch (e: Exception) {
            e.message?.let { Log.e(ContentValues.TAG, it) }
        }

    }

    internal fun getLocalServiceDAO() : ILocalServiceDAO {
        val db = Room.databaseBuilder(application, ServiceDatabase::class.java, "mydiary").build()
        val localServiceDAO = db.localServiceDAO()
        return localServiceDAO
    }

    internal fun save(service:Service) {
        getLocalServiceDAO().save(service)
    }
}