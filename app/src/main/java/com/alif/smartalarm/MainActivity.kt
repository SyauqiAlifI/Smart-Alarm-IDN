package com.alif.smartalarm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alif.smartalarm.adapter.AlarmAdapter
import com.alif.smartalarm.data.local.AlarmDB
import com.alif.smartalarm.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null   // adalah class yang menampung layout
    private val binding get() = _binding as ActivityMainBinding

    private var alarmAdapter: AlarmAdapter? = null
    private val db by lazy { AlarmDB(this) }    //lazy adalah mempermudah dalam inisialisasi

    override fun onResume() {
        super.onResume()

        db.alarmDao().getAlarm().observe(this){
            alarmAdapter?.setData(it)
            Log.i("GetAlarm", "setupRecyclerView: with this data $it")
        }

        /*CoroutineScope(Dispatchers.IO).launch {
            val alarm = db.alarmDao().getAlarm()
            withContext(Dispatchers.Main) {
                alarmAdapter?.setData(alarm)
            }
            Log.i("GetAlarm", "setupRecyclerView: with this data $alarm")//background thread
        }*/
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.apply {
            alarmAdapter = AlarmAdapter()
            rvReminderAlarm.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = alarmAdapter
            }
            swipeToDelete(rvReminderAlarm)
        }
    }

    private fun initView() {
        binding.apply {
            viewSetOneTimeAlarm.setOnClickListener {
                startActivity(Intent(this@MainActivity, OneTimeAlarmActivity::class.java))
            }

            viewSetRepeatingAlarm.setOnClickListener {
                startActivity(Intent(this@MainActivity, RepeatingAlarmActivity::class.java))
            }
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = alarmAdapter?.listAlarm?.get(viewHolder.adapterPosition)
                CoroutineScope(Dispatchers.IO).launch {
                    deletedItem?.let { db.alarmDao().deleteAlarm(it) }
                    Log.i("DeleteAlarm", "onSwiped: succes deleted alarm with $deletedItem")
                }
//                alarmAdapter?.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        ).attachToRecyclerView(recyclerView)
    }

        /*dinonaktifkan karena sudah mendapatkan live tanggal nya di xml
    private fun initDateToday(){
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E, dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        binding.tvDateToday.text = formattedDate
    }

    private fun initTimeToday() {
        //calendar untuk mendapatkan segala hal yang berhubungan dengan waktu di android
        val calendar = Calendar.getInstance()
        // menentukan format jam yang akan digunakan, contohnya 13:36 atau 01:36 p.m atau 13:36:60
        val timeFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        val formattedTime = timeFormat.format(calendar.time)

        binding.tvTimeToday.text = formattedTime
    }*/
}