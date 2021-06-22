package com.freelansoft.dynasoft.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName="job")
data class Job(var jobname: String, var description : String, var common :String, @PrimaryKey @SerializedName("id") var jobId:Int = 0) {
    override fun toString(): String {
        return common
    }
}