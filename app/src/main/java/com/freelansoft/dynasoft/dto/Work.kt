package com.freelansoft.dynasoft.dto

import com.google.firebase.firestore.Exclude

data class Work(var jobName:String = "", var location:String = "", var supervisor: String = "", var description: String = "", var dateWorking:String = "", var workId : String = "", var jobId: Int = 0) {

    private var _events: ArrayList<Event> = ArrayList<Event>()

    var events : ArrayList<Event>
        @Exclude get() {return _events}
        set(value) {_events = value}

    override fun toString(): String {
        return "$jobName $description $location $supervisor"
    }
}