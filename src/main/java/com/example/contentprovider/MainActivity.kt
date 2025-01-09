package com.example.contentprovider

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contentprovider.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactsAdapter: ContactsAdapter
    private val contactsList = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = "Контакты"

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        requestPermissions()
        loadContacts()

        contactsAdapter = ContactsAdapter(contactsList, { phoneNumber -> makeCall(phoneNumber) }, { phoneNumber -> openMessageActivity(phoneNumber) })
        binding.recyclerView.adapter = contactsAdapter
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 2)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 3)
        }
    }

    @SuppressLint("Range")
    private fun loadContacts() {
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactsList.add(Contact(name, phoneNumber))
            }
        }
    }

    private fun makeCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)
        } else {
            Toast.makeText(this, "Permissions not granted to make calls", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openMessageActivity(phoneNumber: String) {
        val intent = Intent(this, MessageActivity::class.java)
        intent.putExtra("PHONE_NUMBER", phoneNumber)
        startActivity(intent)
    }
}