package com.freelansoft.dynasoft.service

import com.freelansoft.dynasoft.dto.Work
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class DiaryFirebase {

    private  val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getPostList(): Task<QuerySnapshot> {
        return firebaseFirestore
                .collection("works")
                .whereEqualTo("description", "")
//                .orderBy("supervisor")
                .get()
    }

    fun getDoingList(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("works")
            .whereEqualTo("description", "assigned")
            .get()
    }

    fun getDoneList(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("works")
            .whereEqualTo("description", "done")
            .get()
    }

    fun getPendingList(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("works")
            .whereEqualTo("description", "pending")
            .get()
    }

}