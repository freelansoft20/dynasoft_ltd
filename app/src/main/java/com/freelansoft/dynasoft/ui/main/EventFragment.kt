package com.freelansoft.dynasoft.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.freelansoft.dynasoft.MainActivity
import com.freelansoft.dynasoft.R
import com.freelansoft.dynasoft.dto.Event
import kotlinx.android.synthetic.main.event_fragment.*
import kotlinx.android.synthetic.main.newservicedialog.edtDescription

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

        btnEventReport.setOnClickListener {
            (activity as MainActivity).onOpenReport()
        }

        btnSaveEvent.setOnClickListener {
            saveEvent()
        }

        btnBackToWork.setOnClickListener {
            (activity as MainActivity).onSwipeRight()
        }

    }

    private fun saveEvent() {
        var event = Event()
        with (event) {
            type = actEventType.text.toString()
            user = edtDescription.text.toString()
            var quantityString = edtQuanity.text.toString();
            if (quantityString.length > 0) {
                quantity = quantityString.toDouble()
            }
            room = actUnits.text.toString()

        }
        viewModel.save(event)
        clearAll()
        rcyEvents.adapter?.notifyDataSetChanged()
    }

    private fun saveEvents() {
        var event = Event()
        with (event) {
            type = actEventType.text.toString()
            user = edtDescription.text.toString()
            var quantityString = edtQuanity.text.toString();
            if (quantityString.length > 0) {
                quantity = quantityString.toDouble()
            }
            room = actUnits.text.toString()

        }
        viewModel.work.events.add(event)
        viewModel.save(event)
        clearAll()
        rcyEvents.adapter?.notifyDataSetChanged()
    }

    private fun clearAll() {
//        edtEventDate.setText("")
        actEventType.setText("")
        edtQuanity.setText("")
        actUnits.setText("")
        edtDescription.setText("")
    }

}