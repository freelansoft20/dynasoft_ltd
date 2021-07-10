package com.freelansoft.dynasoft.ui.main

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freelansoft.dynasoft.R
import com.freelansoft.dynasoft.dto.Service
import com.freelansoft.dynasoft.dto.Work
import com.freelansoft.dynasoft.service.DiaryFirebase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_job.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class JobFragment : DiaryFragment(), DateSelected, NewServiceCreated {

    private val firebaseJob: DiaryFirebase = DiaryFirebase()

    private var works: List<Work> = ArrayList()
    override lateinit var viewModel: MainViewModel
    private val myAdapter: WorksAdapter = WorksAdapter(works)
    private var _serviceId = 0
    var selectedService: Service = Service("", "", "")
    private var rcyWorks: RecyclerView? = null
    private var service = Service()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fab_btn_job.setOnClickListener {
            newJob()
        }

        
    }

    private fun newJob() {
        var viewModel = MainViewModel()
        var work = Work()

        val inflater = requireActivity().layoutInflater
        var newServiceView = inflater.inflate(R.layout.joblayout, null)
        val btnDateField = newServiceView.findViewById<Button>(R.id.btnDateField)
        val spnServices = newServiceView.findViewById<Spinner>(R.id.spnServices)
        val txtServiceId = newServiceView.findViewById<EditText>(R.id.txtServiceId)
        val txtLocation = newServiceView.findViewById<EditText>(R.id.txtLocation)
        val txtSupervisor = newServiceView.findViewById<EditText>(R.id.txtSupervisor)
        val actServiceName = newServiceView.findViewById<AutoCompleteTextView>(R.id.actServiceName)
        btnDateField.setOnClickListener {
            showDatePicker()
        }

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

        val builder = AlertDialog.Builder(activity)
                .setView(newServiceView)
                .setTitle("New Service")
                .setPositiveButton(getString(R.string.save), DialogInterface.OnClickListener{ dialog, which ->
                    work.apply {
                        serviceName = actServiceName.text.toString()
                        dateWorking = btnDateField.text.toString()
                        location = txtLocation.text.toString()
                        supervisor = txtSupervisor.text.toString()
                        serviceId = txtServiceId.text.toString().toInt()
                    }
                    viewModel.work = work
                    viewModel.save(work)
                    dialog.cancel()
                })
                .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                })
                .show()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_job, container, false)

        rcyWorks = view.findViewById(R.id.rcyWorks)
        rcyWorks!!.setHasFixedSize(false)
        rcyWorks!!.layoutManager = LinearLayoutManager(context)
        rcyWorks!!.adapter = myAdapter
//        recyclerView!!.smoothScrollBy(20,40)
        works = ArrayList(works)
        loadPostData()

        return view
    }

    private fun loadPostData() {
        firebaseJob.getPostList().addOnCompleteListener {
            if (it.isSuccessful){
                var work = it.result!!.toObjects(Work::class.java)
                myAdapter.works = work as MutableList<Work>
                myAdapter.notifyDataSetChanged()
            }else{
                Log.d(ContentValues.TAG, "Error:${it.exception!!.message}")
            }
        }
    }



    override fun receiveService(service: Service) {

    }

    companion object {
        fun newInstance() = JobFragment()
    }
}

interface DateSelected {
    fun receiveDate(year: Int, month: Int, dayOfMonth: Int)
}

interface NewServiceCreated {
    fun receiveService(service: Service)
}