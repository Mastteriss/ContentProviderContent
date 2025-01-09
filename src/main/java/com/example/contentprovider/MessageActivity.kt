package com.example.contentprovider

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import com.example.contentprovider.databinding.ActivityMessageBinding

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.getStringExtra("PHONE_NUMBER") ?: ""

        val toolbar: Toolbar = binding.toolBar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Отправка сообщения"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val message = binding.messageEditText.text.toString()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= 23) {
                    getSystemService(SmsManager::class.java)
                } else {
                    SmsManager.getDefault()
                }
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Toast.makeText(applicationContext, "Сообщение отправлено", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Пожалуйста, введите все данные.. ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 3)
        }
    }
}