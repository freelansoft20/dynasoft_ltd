package com.freelansoft.dynasoft.ui.main

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freelansoft.dynasoft.R
import com.freelansoft.dynasoft.dto.Work
import com.freelansoft.dynasoft.service.DiaryFirebase

class PendingFragment : DiaryFragment()  {

    private val firebasePending: DiaryFirebase = DiaryFirebase()

    private var works: List<Work> = ArrayList()

    private val myAdapter: WorksAdapter = WorksAdapter(works)

    private var rcyWorks: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_todo, container, false)

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
        firebasePending.getPostList().addOnCompleteListener {
            if (it.isSuccessful){
                var work = it.result!!.toObjects(Work::class.java)
                myAdapter.works = work as MutableList<Work>
                myAdapter.notifyDataSetChanged()
            }else{
                Log.d(ContentValues.TAG, "Error:${it.exception!!.message}")
            }
        }
    }
}