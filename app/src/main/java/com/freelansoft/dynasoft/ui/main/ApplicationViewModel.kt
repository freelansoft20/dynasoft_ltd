package com.freelansoft.dynasoft.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.freelansoft.dynasoft.service.JobService
import kotlinx.coroutines.launch

class ApplicationViewModel(application: Application) : AndroidViewModel(application) {
    private var _jobService: JobService = JobService(application)


    init {
        fetchJobs("e")
    }

    fun fetchJobs(jobName: String) {
        viewModelScope.launch{
            _jobService.fetchJobs(jobName)
        }
    }

    internal var jobService : JobService
        get() {return _jobService}
        set(value) {_jobService = value}
}