package com.freelansoft.dynasoft.ui.main

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.freelansoft.dynasoft.R
import com.freelansoft.dynasoft.dto.Job
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : DiaryFragment(), DateSelected, NewJobCreated{

    private lateinit var viewModel: MainViewModel
    var selectedJob: Job = Job("", "", "")
    private var _jobId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        /**
         * An existing item was clicked from the predefined autocomplete list.
         */
        actJobName.setOnItemClickListener { parent, view, position, id ->
            selectedJob = parent.getItemAtPosition(position) as Job
            _jobId = selectedJob.jobId
        }
        actJobName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedJob = Job("", "", "")
            }

            /**
             * An existing item was clicked from the predefined autocomplete list.
             */
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedJob = parent!!.getItemAtPosition(position) as Job
                _jobId = selectedJob.jobId
            }
        }
        actJobName.setOnFocusChangeListener { view, hasFocus ->
            var enteredJob = actJobName.text.toString()
            if (selectedJob != null && enteredJob.isNotEmpty() && !enteredJob.equals(selectedJob.toString())) {
                // we have a new Job.
                // we want to ask the user to create a new entry.
                val newJobDialogFragment = NewJobDialogFragment(enteredJob, this)
                newJobDialogFragment.show(requireFragmentManager(), "New Job")
            }

        }

        btnDateField.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment(this)
        datePickerFragment.show(requireFragmentManager(), "datePicker")

    }

    class DatePickerFragment(val dateSelected : DateSelected) : DialogFragment(), DatePickerDialog.OnDateSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month  = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            return DatePickerDialog(requireContext(), this, year, month, dayOfMonth)
        }

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            dateSelected.receiveDate(year, month, dayOfMonth)
            Log.d(ContentValues.TAG, "Got the date")

        }
    }

    override fun receiveDate(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = GregorianCalendar()
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        val viewFormatter = SimpleDateFormat("dd-MMM-yyyy")
        var viewFormattedDate = viewFormatter.format(calendar.getTime())
        btnDateField.setText(viewFormattedDate)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    class NewJobDialogFragment(val enteredJob:String, val newJobCreated:NewJobCreated) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val inflater = requireActivity().layoutInflater
                var newJobView = inflater.inflate(R.layout.newjobdialog, null)
                val txtCommon = newJobView.findViewById<EditText>(R.id.edtCommon)
                val txtGenus = newJobView.findViewById<EditText>(R.id.edtJobName)
                val txtSpecies = newJobView.findViewById<EditText>(R.id.edtDescription)
                txtCommon.setText(enteredJob)
                builder.setView(newJobView)
                        .setPositiveButton(getString(R.string.save), DialogInterface.OnClickListener{ dialog, which ->
                            val common = txtCommon.text.toString()
                            val jobname = txtGenus.text.toString()
                            val description = txtSpecies.text.toString()
                            val newJob = Job(jobname, description, common)
                            newJobCreated.receiveJob(newJob)
                            getDialog()?.cancel()
                        })
                        .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which ->
                            getDialog()?.cancel()
                        })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    override fun receiveJob(job: Job) {
        TODO("Not yet implemented")
    }


}

interface DateSelected {
    fun receiveDate(year: Int, month: Int, dayOfMonth: Int)
}

interface NewJobCreated {
    fun receiveJob(job: Job)
}