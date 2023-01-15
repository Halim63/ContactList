package com.example.contactlistyt

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactlistyt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val REQ_PERMISSION_READ_CONTACT = 1
    private lateinit var binding: ActivityMainBinding
    private var arrayList: ArrayList<ContactModel> = arrayListOf()
    private var rcvAdapter: RCVAdapter = RCVAdapter(arrayList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),REQ_PERMISSION_READ_CONTACT)
        }
        else{
            getContacts()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==REQ_PERMISSION_READ_CONTACT && grantResults.isNotEmpty()){
            getContacts()
        }
    }

    override fun onResume() {
        super.onResume()
        val contentObserver = ContObserver(Handler(), this)
//        contentResolver.notifyChange(ContactsContract.Contacts.CONTENT_URI,contentObserver)
        applicationContext.contentResolver.registerContentObserver(
            ContactsContract.Contacts.CONTENT_URI,
            true,
            contentObserver)
        binding.apply {
            rcvContact.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = RCVAdapter(arrayList)
            }
        }
        getContacts()
    }

    override fun onDestroy() {
        super.onDestroy()


    }

    private fun getContacts() {
        arrayList.clear()

        val cursor = this.contentResolver
            .query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ), null, null ,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
        while (cursor!!.moveToNext()) {
            val contactName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val contactNumber =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val contactModel = ContactModel(contactName, contactNumber)
            arrayList.add(contactModel)
        }
        rcvAdapter.notifyDataSetChanged()
        cursor.close()
    }

}
