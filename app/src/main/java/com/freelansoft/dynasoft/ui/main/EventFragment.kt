package com.freelansoft.dynasoft.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.freelansoft.dynasoft.MainActivity
import com.freelansoft.dynasoft.R
import com.freelansoft.dynasoft.dto.Event
import kotlinx.android.synthetic.main.event_fragment.*
import kotlinx.android.synthetic.main.newjobdialog.*
import kotlinx.android.synthetic.main.newjobdialog.edtDescription

class EventFragment : DiaryFragment() {

    companion object {
        fun newInstance() = EventFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.event_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnSaveEvent.setOnClickListener {
            saveEvent()
        }

        btnBackToWork.setOnClickListener {
            (activity as MainActivity).onSwipeRight()
        }
        // wire up our recycler view.
        rcyEvents.hasFixedSize()
        rcyEvents.layoutManager = LinearLayoutManager(context)
        rcyEvents.itemAnimator = DefaultItemAnimator()
        rcyEvents.adapter = EventsAdapter(viewModel.work.events, R.layout.rowlayout)

    }

    private fun saveEvent() {
        var event = Event()
        with (event) {
            type = actEventType.text.toString()
            description = edtDescription.text.toString()
            var quantityString = edtQuanity.text.toString();
            if (quantityString.length > 0) {
                quantity = quantityString.toDouble()
            }
            units = actUnits.text.toString()
            date = edtEventDate.text.toString()

        }
        viewModel.work.events.add(event)
        viewModel.save(event)
        clearAll()
        rcyEvents.adapter?.notifyDataSetChanged()
    }

    private fun clearAll() {
        edtEventDate.setText("")
        actEventType.setText("")
        edtQuanity.setText("")
        actUnits.setText("")
        edtDescription.setText("")
    }

}