package com.example.smsish_guard

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val SMS_CHANNEL = "com.example.smsish_guard/smsStream"
    private val METHOD_CHANNEL = "com.example.smsish_guard/smsMethods"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // 🔹 Stream for live SMS
        EventChannel(flutterEngine.dartExecutor.binaryMessenger, SMS_CHANNEL)
            .setStreamHandler(object : EventChannel.StreamHandler {
                override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                    SmsReceiver.setEventSink(events)
                }

                override fun onCancel(arguments: Any?) {
                    SmsReceiver.setEventSink(null)
                }
            })

        // 🔹 Method call for reading inbox
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, METHOD_CHANNEL)
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "getInboxMessages" -> {
                        val messages = getInboxMessages()
                        result.success(messages)
                    }
                    else -> result.notImplemented()
                }
            }
    }

    // 📩 Fetch SMS from inbox
    private fun getInboxMessages(): List<String> {
        val smsList = mutableListOf<String>()
        val uriSms: Uri = Uri.parse("content://sms/inbox")
        val resolver: ContentResolver = contentResolver
        val cursor: Cursor? = resolver.query(uriSms, null, null, null, "date DESC")

        cursor?.use {
            val indexAddress = it.getColumnIndex("address")
            val indexBody = it.getColumnIndex("body")

            while (it.moveToNext()) {
                val address = it.getString(indexAddress)
                val body = it.getString(indexBody)
                smsList.add("$address: $body")
            }
        }
        return smsList
    }
}
