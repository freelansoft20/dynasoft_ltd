package com.freelansoft.dynasoft.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.freelansoft.dynasoft.R
import com.freelansoft.dynasoft.dto.Event
import com.freelansoft.dynasoft.dto.Work
import kotlinx.android.synthetic.main.event_fragment.*
import kotlinx.android.synthetic.main.eventlayout.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.newservicedialog.*
import kotlinx.android.synthetic.main.newservicedialog.view.*
import java.util.ArrayList

open class DiaryFragment: Fragment() {

    open lateinit var viewModel: MainViewModel
    open var _events = ArrayList<Event>()
    open var _works = ArrayList<Work>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity.let {
            viewModel = ViewModelProviders.of(it!!).get(MainViewModel::class.java)
        }
    }

    inner class EventsAdapter(var works: List<Work>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        inner class EventViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

            private var location: TextView = itemView.findViewById(R.id.txtDoingLocation)
            private var service: TextView = itemView.findViewById(R.id.txtDoingTask)
            private var date: TextView = itemView.findViewById(R.id.txtWorkDoingDate)
            private var supervisor: TextView = itemView.findViewById(R.id.txtDoingSupervisor)
            private var btnPendingEvent: ImageButton = itemView.findViewById(R.id.btnDoingArchive)
            private var backward: ImageButton = itemView.findViewById(R.id.btnDoingBackward)
            private var forward: ImageButton = itemView.findViewById(R.id.btnDoingForward)

            fun bind(work: Work) {
                service.text = work.serviceName
                location.text = work.location
                date.text = work.dateWorking
                supervisor.text = work.supervisor
            }

            fun updateEvent (work : Work) {
                backward.setOnClickListener {
                    work.apply {
                        description = ""
                    }
                    viewModel.work = work
                    viewModel.save(work)
                }

            }

            fun done (work: Work) {
                forward.setOnClickListener {
                    work.apply {
                        description = "done"
                    }
                    viewModel.work = work
                    viewModel.save(work)

                }

            }

            fun pending (work: Work) {
                btnPendingEvent.setOnClickListener {
                    work.apply {
                        description = "pending"
                    }
                    viewModel.work = work
                    viewModel.save(work)

                }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.eventlayout, parent, false)
            return EventViewHolder(view)
        }

        override fun getItemCount(): Int {
            return works.size
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val work = works.get(position)
            (holder as EventViewHolder).updateEvent(work)
            holder.bind(works[position])
            holder.done(work)
            holder.pending(work)
        }

    }

    inner class DonesAdapter(var works: List<Work>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        inner class EventViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

            private var location: TextView = itemView.findViewById(R.id.txtDoneLocation)
            private var service: TextView = itemView.findViewById(R.id.txtDoneTask)
            private var date: TextView = itemView.findViewById(R.id.txtWorkDoneDate)
            private var supervisor: TextView = itemView.findViewById(R.id.txtDoneSupervisor)
            private var btnPendingEvent: ImageButton = itemView.findViewById(R.id.btnDoneArchive)
            private var backward: ImageButton = itemView.findViewById(R.id.btnDoneBackward)

            fun bind(work: Work) {
                service.text = work.serviceName
                location.text = work.location
                date.text = work.dateWorking
                supervisor.text = work.supervisor
            }

            fun updateEvent (work : Work) {
                backward.setOnClickListener {
                    work.apply {
                        description = "assigned"
                    }
                    viewModel.work = work
                    viewModel.save(work)
                }

            }


            fun approuved (work: Work) {
                btnPendingEvent.setOnClickListener {
                    work.apply {
                        description = "save"
                    }
                    viewModel.work = work
                    viewModel.save(work)

                }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.donelayout, parent, false)
            return EventViewHolder(view)
        }

        override fun getItemCount(): Int {
            return works.size
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val work = works.get(position)
            (holder as EventViewHolder).updateEvent(work)
            holder.bind(works[position])
            holder.approuved(work)
        }

    }

    inner class PendingsAdapter(var works: List<Work>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        inner class EventViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

            private var location: TextView = itemView.findViewById(R.id.txtPendingLocation)
            private var service: TextView = itemView.findViewById(R.id.txtPendingTask)
            private var date: TextView = itemView.findViewById(R.id.txtWorkPendingDate)
            private var supervisor: TextView = itemView.findViewById(R.id.txtPendingSupervisor)
            private var btnPendingEvent: ImageButton = itemView.findViewById(R.id.btnAssignement)
            private var backward: ImageButton = itemView.findViewById(R.id.btnPendingBackward)

            fun bind(work: Work) {
                service.text = work.serviceName
                location.text = work.location
                date.text = work.dateWorking
                supervisor.text = work.supervisor
            }

            fun updateEvent (work : Work) {
                backward.setOnClickListener {
                    work.apply {
                        description = "assigned"
                    }
                    viewModel.work = work
                    viewModel.save(work)
                }

            }

            fun done (work: Work) {
                btnPendingEvent.setOnClickListener {
                    work.apply {
                        description = ""
                    }
                    viewModel.work = work
                    viewModel.save(work)

                }

            }

            fun pending (work: Work) {
                btnPendingEvent.setOnClickListener {
                    work.apply {
                        description = "pending"
                    }
                    viewModel.work = work
                    viewModel.save(work)

                }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.pendinglayout, parent, false)
            return EventViewHolder(view)
        }

        override fun getItemCount(): Int {
            return works.size
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val work = works.get(position)
            (holder as EventViewHolder).updateEvent(work)
            holder.bind(works[position])
            holder.done(work)
//            holder.pending(work)
        }

    }

    inner class WorksAdapter(var works: List<Work>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

        inner class WorkViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
            private var location: TextView = itemView.findViewById(R.id.txtWorkLocation)
            private var service: TextView = itemView.findViewById(R.id.txtWorkTask)
            private var date: TextView = itemView.findViewById(R.id.txtWorkdate)
            private var supervisor: TextView = itemView.findViewById(R.id.txtWorkSupervisor)
            private var btnDeleteEvent: ImageButton = itemView.findViewById(R.id.btnDeleteWork)
//            private var btnUpdate: ImageButton = itemView.findViewById(R.id.btnBackwardEvent)
            private var cardTodo: CardView = itemView.findViewById(R.id.cardViewTodo)


            fun bind(work: Work) {
                service.text = work.serviceName
                location.text = work.location
                date.text = work.dateWorking
                supervisor.text = work.supervisor
            }

            fun updateWork (work : Work) {
                btnDeleteEvent.setOnClickListener {
                    deleteWork(work)
                }

            }

            fun postAssigned (work: Work) {
                cardTodo.setOnClickListener {
                    updateTaskWork(work)

                }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
            LayoutInflater.from(parent.context).inflate(R.layout.eventlayout, parent, false)

            val view = LayoutInflater.from(parent.context).inflate(R.layout.worklayout, parent, false)
            return WorkViewHolder(view)
        }

        override fun getItemCount(): Int {
            return works.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val work = works.get(position)
            (holder as WorkViewHolder).bind(works[position])
            holder.updateWork(work)
            holder.postAssigned (work)

        }

    }

    private fun updateTaskWork(work: Work) {
        var viewModel = MainViewModel()
        var event = Event()
        var builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        var newServiceView = inflater.inflate(R.layout.newservicedialog, null)
        val txtCommon = newServiceView.edtCommon
        val txtGenus = newServiceView.edtServiceName
        val txtSpecies = newServiceView.edtDescription
        builder.setView(newServiceView)
        builder.setPositiveButton(getString(R.string.Add), DialogInterface.OnClickListener { dialog, id ->
            work.apply {
                description = "assigned"
                type=txtCommon.text.toString()
                room=txtGenus.text.toString()
                user=txtSpecies.text.toString()
            }
            viewModel.work = work
            viewModel.save(work)

        })
        builder.setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        var alert = builder.create()
        alert.show()

    }

    private fun updateTaskEvent(event: Event) {
        var viewModel = MainViewModel()
        var builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        var newServiceView = inflater.inflate(R.layout.newservicedialog, null)
        val txtCommon = newServiceView.edtCommon
        val txtGenus = newServiceView.edtServiceName
        val txtSpecies = newServiceView.edtDescription
        builder.setView(newServiceView)
        builder.setPositiveButton(getString(R.string.Add), DialogInterface.OnClickListener { dialog, id ->

            event.apply {
                type=txtCommon.text.toString()
                room=txtGenus.text.toString()
                user=txtSpecies.text.toString()
            }
            viewModel.work.events.add(event)
            viewModel.save(event)
            dialog.cancel()
        })
        builder.setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        var alert = builder.create()
        alert.show()

    }

    private fun deleteWork(work: Work) {
        var builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.confirm_delete))
        builder.setMessage(getString(R.string.delete_confirmation_message))
        builder.setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
            viewModel.delete(work)
            dialog.cancel()
        })
        builder.setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        var alert = builder.create()
        alert.show()

    }

    private fun deleteEvent(event: Event) {
        var builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.confirm_delete))
        builder.setMessage(getString(R.string.delete_confirmation_message))
        builder.setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
            viewModel.delete(event)
            dialog.cancel()
        })
        builder.setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        var alert = builder.create()
        alert.show()

    }
}

