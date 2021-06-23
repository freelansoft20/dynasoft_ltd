package com.freelansoft.dynasoft.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freelansoft.dynasoft.dto.Event
import com.freelansoft.dynasoft.dto.Job
import com.freelansoft.dynasoft.dto.Work
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage

class MainViewModel : ViewModel() {

    private var _jobs: MutableLiveData<ArrayList<Job>> = MutableLiveData<ArrayList<Job>>()
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _works: MutableLiveData<ArrayList<Work>> = MutableLiveData<ArrayList<Work>>()
    private var storageReferenence = FirebaseStorage.getInstance().getReference()
    private var _work = Work()
    private var _events = MutableLiveData<List<Event>>()



    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToWorks()
    }

    private fun listenToWorks() {
        firestore.collection("Works").addSnapshotListener {
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

    fun save(work: Work,user: FirebaseUser) {
        val document =
                if (work.workId != null && !work.workId.isEmpty()) {
                    // updating existing
                    firestore.collection("works").document(work.workId)
                } else {
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

    internal fun save(event: Event) {
        val collection = firestore.collection("Works").document(work.workId).collection("events")
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
            val document = firestore.collection("works").document(work.workId).collection("events").document(event.id)
            val task = document.delete();
            task.addOnSuccessListener {
                Log.e(ContentValues.TAG, "Event ${event.id} Deleted")
            }
            task.addOnFailureListener {
                Log.e(ContentValues.TAG, "Event ${event.id} Failed to delete.  Message: ${it.message}")
            }
        }
    }

    internal fun fetchEvents() {
        var eventsCollection = firestore.collection("works")
                .document(work.workId)
                .collection("events")
        eventsCollection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            var innerEvents = querySnapshot?.toObjects(Event::class.java)
            _events.postValue(innerEvents!!)
        }
    }

    fun fetchPlants(s: String) {

    }

    internal var plants: MutableLiveData<ArrayList<Job>>
        get() {return _jobs}
        set(value) {_jobs = value}

    internal var works: MutableLiveData<ArrayList<Work>>
        get() { return _works}
        set(value) {_works = value}

    internal var work: Work
        get() {return _work}
        set(value) {_work = value}

    internal var events : MutableLiveData<List<Event>>
        get() { return _events}
        set(value) {_events = value}

}