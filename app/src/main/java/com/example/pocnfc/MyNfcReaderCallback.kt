package com.example.pocnfc

import android.nfc.NfcAdapter
import android.nfc.Tag

class MyNfcReaderCallback : NfcAdapter.ReaderCallback {
    override fun onTagDiscovered(tag: Tag) {
        // Manipula a leitura da tag NFC
    }
}