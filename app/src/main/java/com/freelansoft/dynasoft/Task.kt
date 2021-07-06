package com.freelansoft.dynasoft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.freelansoft.dynasoft.dao.TodoListDAO
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import io.grpc.InternalChannelz.id

class Task : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)



        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        val viewPager = findViewById<ViewPager>(R.id.viewerpage)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val back = findViewById<FloatingActionButton>(R.id.fab_btn)

        //so these lines will set our toolbar title
        supportActionBar!!.title = "Tasks"
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        setSupportActionBar(toolBar)



        //these lines set our adapter
        val  fragmentAdapter = TodoListDAO(supportFragmentManager)
        viewPager.adapter = fragmentAdapter

        //our tablaout with viewrpager
        tabLayout.setupWithViewPager(viewPager)

        back.setOnClickListener {
            moveback()
        }

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