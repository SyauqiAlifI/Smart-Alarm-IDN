package com.alif.smartalarm

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimeDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    var dialogListener: TimeDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as TimeDialogListener
    }

    override fun onDetach() {
        super.onDetach()
        if (dialogListener != null) dialogListener = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(activity as Context, this, hour, minute, false)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        dialogListener?.onTimeSetListener(tag, hourOfDay, minute)
    }

    interface TimeDialogListener {
        fun onTimeSetListener(tag: String?, hour: Int, minute: Int)
    }
}