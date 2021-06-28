package com.freelansoft.dynasoft.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freelansoft.dynasoft.dto.Event
import com.freelansoft.dynasoft.dto.Service
import com.freelansoft.dynasoft.dto.Work
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot

class MainViewModel : ViewModel() {

    private var _services: MutableLiveData<ArrayList<Service>> = MutableLiveData<ArrayList<Service>>()
    private lateinit var firestore : FirebaseFirestore
    private var _works: MutableLiveData<ArrayList<Work>> = MutableLiveData<ArrayList<Work>>()
    private var _work = Work()
    private var _service = Service()
    private var _events = MutableLiveData<List<Event>>()



    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToWorks()
        listenToServices()
    }

    private fun listenToWorks() {
        firestore.collection("works").addSnapshotListener {
            snapshot, e ->
            // if there is an exception we want to skip.
            if (e != null) {
                Log.w(ContentValues.TAG, "Listen Failed", e)
                return@addSnapshotListener
            }
            // if we are here, we did not encounter an exception
            if (snapshot != null) {
                // now, we have a populated shapshot
                val allWorks = ArrayList<Work>()
                val documents = snapshot.documents
                documents.forEach {

                    val work = it.toObject(Work::class.java)
                    if (work != null) {
                        work.workId = it.id
                        allWorks.add(work!!)
                    }
                }
                _works.value = allWorks
            }
        }
    }

    private fun listenToServices() {
        firestore.collection("services").addSnapshotListener {
            snapshot, e ->
            // if there is an exception we want to skip.
            if (e != null) {
                Log.w(ContentValues.TAG, "Listen Failed", e)
                return@addSnapshotListener
            }
            // if we are here, we did not encounter an exception
            if (snapshot != null) {
                // now, we have a populated shapshot
                val allServices = ArrayList<Service>()
                val documents = snapshot.documents
                documents.forEach {

                    val service = it.toObject(Service::class.java)
                    if (service != null) {
                        service.serviceId = it.id
                        allServices.add(service!!)
                    }
                }
                _services.value = allServices
            }
        }
    }

    fun save(service: Service) {
        val document = firestore.collection("services").document()
        service.serviceId = document.id
        val set = document.set(service)

        set.addOnSuccessListener {
            Log.d("Firebase", "document saved")

        }

        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed")
        }
    }

    fun save(work: Work) {
        val document =
                if (work.workId != null && work.workId.isNotEmpty()) {
                    // updating existing
                    firestore.collection("works").document(work.workId)
                }
                else {
                    // create new
                    firestore.collection("works").document()
                }
        work.workId = document.id
        val set = document.set(work)

        set.addOnSuccessListener {
            Log.d("Firebase", "document saved")

        }

        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed")
        }
    }

    fun save(event: Event) {
        val collection = firestore.collection("works")
                .document(work.workId)
                .collection("events")
        val task = collection.add(event)

        task.addOnSuccessListener {
            event.id = it.id
        }

        task.addOnFailureListener {
            var message = it.message
            var i = 1 + 1
        }

    }

    internal fun delete(event: Event) {
        if (event.id != null) {
            val document = firestore.collection("works")
                    .document(work.workId)
                    .collection("events")
                    .document(event.id)
            val task = document.delete();

            task.addOnSuccessListener {
                Log.e(ContentValues.TAG, "Event ${event.id} Deleted")
            }

            task.addOnFailureListener {
                Log.e(ContentValues.TAG, "Event ${event.id} Failed to delete.  Message: ${it.message}")
            }
        }
    }

    internal fun delete(work: Work) {
        if (work.workId != null) {
            val document = firestore.collection("works")
                    .document(work.workId)
            val task = document.delete();

            task.addOnSuccessListener {
                Log.e(ContentValues.TAG, "Event ${work.workId} Deleted")
            }

            task.addOnFailureListener {
                Log.e(ContentValues.TAG, "Event ${work.workId} Failed to delete.  Message: ${it.message}")
            }
        }
    }

    internal fun fetchEvents() {
        var eventsCollection = firestore.collection("services")
                .document(service.serviceId)
                .collection("works")

        eventsCollection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            var innerEvents = querySnapshot?.toObjects(Work::class.java)
            _works.postValue(innerEvents!! as ArrayList<Work>?)
        }
    }

    fun fetchPlants(s: String) {

    }

    internal var services: MutableLiveData<ArrayList<Service>>
        get() {return _services}
        set(value) {_services = value}

    internal var works: MutableLiveData<ArrayList<Work>>
        get() { return _works}
        set(value) {_works = value}

    internal var work: Work
        get() {return _work}
        set(value) {_work = value}
    internal var service: Service
        get() {return _service}
        set(value) {_service = value}

    internal var events : MutableLiveData<List<Event>>
        get() { return _events}
        set(value) {_events = value}

}