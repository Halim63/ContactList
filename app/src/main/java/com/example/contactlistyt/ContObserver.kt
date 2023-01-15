package com.example.contactlistyt

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.util.Log

import android.widget.Toast


class ContObserver(handler: Handler?, val context: Context) : ContentObserver(handler) {

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        showAlertDialog()
        Toast.makeText(context,"changed",Toast.LENGTH_LONG).show()
        Log.d(ContObserver::class.java.simpleName, "Chaged")

    }
    fun showAlertDialog(){
        val builder:AlertDialog.Builder=AlertDialog.Builder(context)
        builder.setTitle("Alert Dialog")
        builder.setMessage("A change has happened ")
        builder.setIcon(R.drawable.ic_baseline_notifications_active_24)
        builder.setPositiveButton("OK",DialogInterface.OnClickListener{ dialog, _ ->
            dialog.dismiss()
        })
        val alertDialog:AlertDialog=builder.create()
        alertDialog.show()
    }




}

