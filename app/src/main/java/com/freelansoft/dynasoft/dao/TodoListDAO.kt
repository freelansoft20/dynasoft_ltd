package com.freelansoft.dynasoft.dao

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.freelansoft.dynasoft.ui.main.DoingFragment
import com.freelansoft.dynasoft.ui.main.DoneFragment
import com.freelansoft.dynasoft.ui.main.PendingFragment
import com.freelansoft.dynasoft.ui.main.TodoFragment


class TodoListDAO (fm: FragmentManager): FragmentPagerAdapter(fm){
    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                //now create three fragment
                TodoFragment()
            }
            1 -> {
                DoingFragment()
            }
            2 -> {
                DoneFragment()
            }
            else -> {
                PendingFragment()
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0->"todo"
            1->"doing"
            2->"done"
            else->{
                return "pending"
            }
        }
    }

}