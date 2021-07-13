package com.freelansoft.dynasoft

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.freelansoft.dynasoft.dao.TodoListDAO
import com.freelansoft.dynasoft.dto.Event
import com.freelansoft.dynasoft.dto.Service
import com.freelansoft.dynasoft.dto.Work
import com.freelansoft.dynasoft.ui.main.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.event_fragment.*
import kotlinx.android.synthetic.main.event_fragment.actEventType
import kotlinx.android.synthetic.main.event_fragment.edtDescription
import kotlinx.android.synthetic.main.evententrylayout.*

class Task : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        val viewPager = findViewById<ViewPager>(R.id.viewerpage)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val back = findViewById<FloatingActionButton>(R.id.fab_btn)

        //so these lines will set our toolbar title
        supportActionBar!!.title = "Tasks"

        //these lines set our adapter
        val  fragmentAdapter = TodoListDAO(supportFragmentManager)
        viewPager.adapter = fragmentAdapter

        //our tablaout with viewrpager
        tabLayout.setupWithViewPager(viewPager)

        back.setOnClickListener {
            newJob()
        }

    }


    private fun newJob() {
        var viewModel = MainViewModel()
        var event = Event()

        var newServiceView = layoutInflater.inflate(R.layout.evententrylayout, null)
        val actEventEntryType = newServiceView.findViewById<EditText>(R.id.actEventEntryType)
        val edtEntryDescription = newServiceView.findViewById<EditText>(R.id.edtEntryDescription)
        val edtEntryQuanity = newServiceView.findViewById<EditText>(R.id.edtEntryQuanity)
        val actEntryUnits = newServiceView.findViewById<EditText>(R.id.actEntryUnits)
        val actServiceName = newServiceView.findViewById<AutoCompleteTextView>(R.id.actServiceName)

        val builder = AlertDialog.Builder(this)
                .setView(newServiceView)
                .setTitle("New Job")
                .setPositiveButton(getString(R.string.save), DialogInterface.OnClickListener{ dialog, which ->
                    with (event) {
                        type = actEventEntryType.text.toString()
                        user = edtEntryDescription.text.toString()
                        var quantityString = edtEntryQuanity.text.toString();
                        if (quantityString.length > 0) {
                            quantity = quantityString.toDouble()
                        }
                        room = actEntryUnits.text.toString()

                    }
                    viewModel.work.events.add(event)
                    viewModel.save(event)
                    clearAll()
                    rcyEvents.adapter?.notifyDataSetChanged()
                })
                .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                })
                .show()
    }
    private fun saveEvents() {
        var event = Event()
        with (event) {
            type = actEventEntryType.text.toString()
            user = edtEntryDescription.text.toString()
            var quantityString = edtEntryQuanity.text.toString();
            if (quantityString.length > 0) {
                quantity = quantityString.toDouble()
            }
            room = actEntryUnits.text.toString()

        }
        viewModel.work.events.add(event)
        viewModel.save(event)
        clearAll()
        rcyEvents.adapter?.notifyDataSetChanged()
    }

    private fun clearAll() {
        actEventEntryType.setText("")
        edtEntryQuanity.setText("")
        actEntryUnits.setText("")
        edtEntryDescription.setText("")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun moveback(){
        startActivity(Intent(this, MainActivity::class.java))

    }

}