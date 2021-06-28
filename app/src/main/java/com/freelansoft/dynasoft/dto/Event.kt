package com.freelansoft.dynasoft.dto

data class Event(var type: String = "", var date: String = "", var quantity : Double? = 0.0, var room : String = "", var user : String = "", var id: String = "") {

    override fun toString(): String {
        return "$type $quantity $room $user"
    }
}