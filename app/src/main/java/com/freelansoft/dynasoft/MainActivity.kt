package com.freelansoft.dynasoft

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.freelansoft.dynasoft.dto.Service
import com.freelansoft.dynasoft.ui.main.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var detector: GestureDetectorCompat
    private lateinit var eventFragment: EventFragment
    private lateinit var mainFragment: MainFragment
    private lateinit var jobFragment: JobFragment
    private lateinit var activeFragment: Fragment
    private val AUTH_REQUEST_CODE = 2002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        eventFragment = EventFragment.newInstance()
        mainFragment = MainFragment.newInstance()
        jobFragment = JobFragment.newInstance()
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, JobFragment.newInstance())
                    .commitNow()
            activeFragment = mainFragment
        }

        detector = GestureDetectorCompat(this, DiaryGestureListener())

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.btnLogin ->{
                logon()
                true
            }
            R.id.new_service ->{
                // we have a new Service.
                // we want to ask the user to create a new entry.
                var viewModel = MainViewModel()
                var service = Service()
                var newServiceView = LayoutInflater.from(this).inflate(R.layout.newservicedialog, null)
                val builder = AlertDialog.Builder(this)
                    .setView(newServiceView)
                    .setTitle("New Service")
                    .setPositiveButton(getString(R.string.save), DialogInterface.OnClickListener{ dialog, which ->
                        val txtCommon = newServiceView.findViewById<EditText>(R.id.edtCommon)
                        val txtGenus = newServiceView.findViewById<EditText>(R.id.edtServiceName)
                        val txtSpecies = newServiceView.findViewById<EditText>(R.id.edtDescription)
                        service.apply {
                            servId = txtCommon.text.toString().toInt()
                            servicename = txtGenus.text.toString()
                            description = txtSpecies.text.toString()
                        }
                        viewModel.save(service)
                        dialog.cancel()
                    })
                    .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which ->
                        dialog.cancel()
                    })
                    .show()

                true
            }
            else -> super.onOptionsItemSelected(item)
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (detector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    inner class DiaryGestureListener : GestureDetector.SimpleOnGestureListener() {

        private val  SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
                downEvent: MotionEvent?,
                moveEvent: MotionEvent?,
                velocityX: Float,
                velocityY: Float
        ): Boolean {
            var diffX = moveEvent?.x?.minus(downEvent!!.x) ?: 0.0F
            var diffY = moveEvent?.y?.minus(downEvent!!.y) ?: 0.0F

            return if (Math.abs(diffX) > Math.abs(diffY)) {
                // this is a left or right swipe
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0 ) {
                        // right swipe
                        this@MainActivity.onSwipeRight()
                    } else {
                        // left swipe.
                        this@MainActivity.onLeftSwipe()
                    }
                    true
                } else  {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            } else {
                // this is either a bottom or top swipe.
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        this@MainActivity.onSwipeTop()
                    } else {
                        this@MainActivity.onSwipeBottom()
                    }
                    true
                } else {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            }


        }
    }

    private fun onSwipeBottom() {
        Toast.makeText(this, "Bottom Swipe", Toast.LENGTH_LONG).show()
    }

    private fun onSwipeTop() {
        Toast.makeText(this, "Top Swipe", Toast.LENGTH_LONG).show()
    }

    internal fun onLeftSwipe() {
        if (activeFragment == mainFragment) {
//            mainFragment.saveWork()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, eventFragment)
                    .commitNow()
            activeFragment = eventFragment
        }
    }

    internal fun onSwipeRight() {
        if (activeFragment == eventFragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()
            activeFragment = mainFragment
        }
    }

    internal fun onOpenReport(){
        startActivity(Intent(this, Task::class.java))

    }

}

