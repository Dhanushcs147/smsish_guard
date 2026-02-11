package com.example.smsish_guard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import io.flutter.plugin.common.EventChannel

class SmsReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "SmsReceiver"
        const val SMS_STREAM = "com.example.smsish_guard/smsStream"

        private var eventSink: EventChannel.EventSink? = null

        fun setEventSink(sink: EventChannel.EventSink?) {
            eventSink = sink
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle: Bundle? = intent.extras
            bundle?.let {
                val pdus = it["pdus"] as? Array<*>
                pdus?.forEach { pdu ->
                    val sms = SmsMessage.createFromPdu(pdu as ByteArray)
                    val sender = sms.displayOriginatingAddress
                    val body = sms.messageBody

                    Log.d(TAG, "📩 SMS Received: $sender : $body")

                    // Send to Flutter via EventChannel
                    eventSink?.success("$sender: $body")
                }
            }
        }
    }
}
