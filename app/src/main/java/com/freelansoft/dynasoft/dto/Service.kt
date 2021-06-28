package com.freelansoft.dynasoft.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName


@Entity(tableName="service")
data class Service(var servicename: String = "", var description : String = "", var common :String = "", @PrimaryKey @SerializedName("id") var servId:Int = 0, var serviceId:String = "") {

    override fun toString(): String {
        return "$servId. $servicename"
    }
}