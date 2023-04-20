//package com.example.pocnfc
//
//import android.nfc.NdefMessage
//import android.nfc.NdefRecord
//import android.nfc.Tag
//import android.nfc.tech.Ndef
//import java.nio.charset.Charset
//
//object TagUtil {
//    fun createTag(payload: ByteArray): Tag {
//        val record = NdefRecord.createMime("application/vnd.com.example.android.beam", payload)
//        val message = NdefMessage(record)
////        val tag = Tag(null, arrayOf(Ndef::class.java.name), null)
////        val ndef = Ndef.get(tag)
////        ndef.connect()
////        ndef.writeNdefMessage(message)
////        ndef.close()
////        return tag
//    }
//}