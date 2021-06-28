package com.freelansoft.dynasoft.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.freelansoft.dynasoft.MainActivity
import com.freelansoft.dynasoft.R
import com.freelansoft.dynasoft.dto.Event
import com.freelansoft.dynasoft.dto.Service
import com.freelansoft.dynasoft.dto.Work
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.event_fragment.*
import kotlinx.android.synthetic.main.event_fragment.edtDescription
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.newservicedialog.*
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : DiaryFragment(), DateSelected, NewServiceCreated{

    private val AUTH_REQUEST_CODE = 2002
    private var user : FirebaseUser? = null
    private var work = Work()
    private var service = Service()
    override lateinit var viewModel: MainViewModel
    var selectedService: Service = Service("", "", "")
    private var _serviceId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        /**
         * An existing item was clicked from the predefined autocomplete list.
         */

        actServiceName.setOnItemClickListener { parent, view, position, id ->
            selectedService = parent.getItemAtPosition(position) as Service
            _serviceId = selectedService.servId
        }

        viewModel.services.observe(viewLifecycleOwner, Observer {
            service -> spnServices.setAdapter(ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, service))
        })

        actServiceName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedService = Service("", "", "")
            }

            /**
             * An existing item was clicked from the predefined autocomplete list.
             */
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedService = parent!!.getItemAtPosition(position) as Service
                _serviceId = selectedService.servId
            }
        }

        btnNewServiceName.setOnClickListener {
                // we have a new Service.
                // we want to ask the user to create a new entry.
                val newServiceDialogFragment = NewServiceDialogFragment()
                newServiceDialogFragment.show(requireFragmentManager(), "New Service")

        }

        btnDateField.setOnClickListener {
            showDatePicker()
        }

        btnForward.setOnClickListener {
            saveWork()
            (activity as MainActivity).onLeftSwipe()
        }

        btnSave.setOnClickListener {
            saveWork()
            clearAll()
        }

        btnLogon.setOnClickListener {
            logon()
        }

        btnReport.setOnClickListener {
            (activity as MainActivity).onOpenReport()
        }

        spnServices.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            /**
             * Callback method to be invoked when the selection disappears from this
             * view. The selection can disappear for instance when touch is activated
             * or when the adapter becomes empty.
             *
             * @param parent The AdapterView that now contains no selected item.
             */
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            /**
             *
             * Callback method to be invoked when an item in this view has been
             * selected. This callback is invoked only when the newly selected
             * position is different from the previously selected position or if
             * there was no selected item.
             *
             * Implementers can call getItemAtPosition(position) if they need to access the
             * data associated with the selected item.
             *
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                service = parent?.getItemAtPosition(position) as Service
                // use this work object to populate our UI fields
                actServiceName.setText(service.servicename)
                txtServiceId.setText(Integer.toString(service.servId))
//                btnDateField.setText(work.dateWorking)
                viewModel.service = service
                // trigger an update of the events for this work.
                viewModel.fetchEvents()
            }

        }

    }

    private fun logon() {
        var providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), AUTH_REQUEST_CODE
        )
    }

    /**
     * Persist our Work to long term storage.
     */

    internal fun saveWork() {
        storeWork()
        viewModel.save(work)
        work = Work()
    }

    /**
     * Populate a Work object based on the details entered into the user interface.
     */

    internal fun storeWork() {
        work.apply {
            serviceName = actServiceName.text.toString()
            dateWorking = btnDateField.text.toString()
            location = txtLocation.text.toString()
            supervisor = txtSupervisor.text.toString()
            serviceId = txtServiceId.text.toString().toInt()
        }
        viewModel.work = work
        viewModel.save(work)
    }

    internal fun asignedWork() {

        viewModel.work = work
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

    class NewServiceDialogFragment() : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                var viewModel = MainViewModel()
                var service = Service()
                val builder = AlertDialog.Builder(it)
                val inflater = requireActivity().layoutInflater
                var newServiceView = inflater.inflate(R.layout.newservicedialog, null)
                val txtCommon = newServiceView.findViewById<EditText>(R.id.edtCommon)
                val txtGenus = newServiceView.findViewById<EditText>(R.id.edtServiceName)
                val txtSpecies = newServiceView.findViewById<EditText>(R.id.edtDescription)
//                txtCommon.setText(enteredService)
                builder.setView(newServiceView)
                        .setPositiveButton(getString(R.string.save), DialogInterface.OnClickListener{ dialog, which ->
                            service.apply {
                                servId = txtCommon.text.toString().toInt()
                                servicename = txtGenus.text.toString()
                                description = txtSpecies.text.toString()
                            }
                            viewModel.save(service)
                            getDialog()?.cancel()
                        })
                        .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which ->
                            getDialog()?.cancel()
                        })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    override fun receiveService(service: Service) {

    }

    private fun clearAll() {
//        edtServiceName.setText("")
        txtLocation.setText("")
        txtSupervisor.setText("")
    }

}

interface DateSelected {
    fun receiveDate(year: Int, month: Int, dayOfMonth: Int)
}

interface NewServiceCreated {
    fun receiveService(service: Service)
}