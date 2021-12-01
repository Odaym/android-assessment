package com.saltserv.assessment.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.saltserv.assessment.R

class ErrorDialog(private val errorMessage: String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_error, null)

        val positiveButton = view.findViewById<Button>(R.id.positive_button)
        val negativeButton = view.findViewById<Button>(R.id.negative_button)

        val errorText = view.findViewById<TextView>(R.id.dialog_message)

        with(view) {
            errorText.text = errorMessage

            positiveButton.setOnClickListener {
                (context as Listener).onRetryClicked()
                dismiss()
            }

            negativeButton.setOnClickListener {
                (context as Listener).onCancelClicked(this@ErrorDialog)
            }
        }

        return AlertDialog.Builder(context).setView(view).create()
    }

    interface Listener {
        fun onRetryClicked()
        fun onCancelClicked(dialog: ErrorDialog)
    }

    companion object {
        fun newInstance(errorMessage: String): ErrorDialog {
            return ErrorDialog(errorMessage)
        }
    }
}