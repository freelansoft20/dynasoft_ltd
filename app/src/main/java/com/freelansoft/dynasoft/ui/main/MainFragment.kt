package com.freelansoft.dynasoft.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.freelansoft.dynasoft.R
import com.freelansoft.dynasoft.dto.Job
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        btnDateField.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment(this)
        datePickerFragment.show(requireFragmentManager(), "datePicker")

    }

}

interface DateSelected {
    fun receiveDate(year: Int, month: Int, dayOfMonth: Int)
}

interface NewPlantCreated {
    fun receivePlant(job: Job)
}