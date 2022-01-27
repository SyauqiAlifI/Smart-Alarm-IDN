package com.alif.smartalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alif.smartalarm.databinding.ActivityOneTimeAlarmBinding
import com.alif.smartalarm.databinding.ActivityRepeatingAlarmBinding
import com.alif.smartalarm.helper.timeFormatter

class RepeatingAlarmActivity : AppCompatActivity(), TimeDialogFragment.TimeDialogListener {

    private var _binding: ActivityRepeatingAlarmBinding? = null
    private val binding get () = _binding as ActivityRepeatingAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repeating_alarm)

        _binding = ActivityRepeatingAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        binding.apply {
            btnSetTimeRepeating.setOnClickListener {
                val timePickerFragment = TimeDialogFragment()
                timePickerFragment.show(supportFragmentManager, "timePickerDialog")
            }
        }
    }

    override fun onTimeSetListener(tag: String?, hourOfDay: Int, minute: Int) {
        binding.tvOnceTime.text = timeFormatter(hourOfDay, minute)
    }
}