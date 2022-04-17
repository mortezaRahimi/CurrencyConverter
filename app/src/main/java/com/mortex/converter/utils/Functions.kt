package com.mortex.converter.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

object Functions {

    fun Fragment.showDialog( msg: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Convert Done")
        builder.setMessage(msg)
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            dialog.dismiss()
        }


        builder.show()
    }

    fun Fragment.showToast( msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}