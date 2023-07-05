package com.example.dicerollcw

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class WinPopup :DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view=activity?.layoutInflater?.inflate(R.layout.win_popup,null)

        val builder= AlertDialog.Builder(activity)
        builder.setView(view)

        return builder.create()
    }
}