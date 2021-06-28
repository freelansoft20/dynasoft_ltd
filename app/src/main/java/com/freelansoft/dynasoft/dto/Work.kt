package com.freelansoft.dynasoft.dto

import com.google.firebase.firestore.Exclude

data class Work(var serviceName:String = "", var location:String = "", var supervisor: String = "",var type:String = "", var room:String = "", var user: String = "", var description: String = "", var dateWorking:String = "", var workId : String = "", var serviceId: Int = 0) {

    private var _events: ArrayList<Event> = ArrayList<Event>()

    var events : ArrayList<Event>
        @Exclude get() {return _events}
        set(value) {_events = value}

    override fun toString(): String {
        return "$serviceName"
    }


}