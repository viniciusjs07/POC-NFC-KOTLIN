package com.example.pocnfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.widget.Button
import android.widget.Toast


class MainActivity : Activity() {

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var writeMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val writeTagButton = findViewById<Button>(R.id.writeButton)
        val readTagButton = findViewById<Button>(R.id.readButton)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC não disponível neste dispositivo", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )

        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

        if (tag == null){
            Toast.makeText(this, "Tag inválida", Toast.LENGTH_LONG).show()
            return
        }

        writeTagButton.setOnClickListener {
            writeTag(this, tag, "teste tag")
        }

        readTagButton.setOnClickListener {
            readTag(tag)
        }
    }

    override fun onResume() {
        super.onResume()
        enableForegroundDispatchSystem()
    }

    override fun onPause() {
        super.onPause()
        disableForegroundDispatchSystem()
    }

    private fun enableForegroundDispatchSystem() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED)
        intentFilter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        intentFilter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED)

        val techList = arrayOf(arrayOf(Ndef::class.java.name))
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, arrayOf(intentFilter), techList)
    }

    private fun disableForegroundDispatchSystem() {
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

            if (tag == null){
                Toast.makeText(this, "Tag inválida", Toast.LENGTH_LONG).show()
                return
            }

            if (writeMode) {
                writeTag(this, tag, "teste tag")
            } else {
                readTag(tag)
            }
        }
    }

    private fun writeTag(context: Context, tag: Tag, data: String) {
        val ndefRecord = NdefRecord.createMime("text/plain", data.toByteArray())
        val ndefMessage = NdefMessage(arrayOf(ndefRecord))
        val ndef = Ndef.get(tag)
        ndef.connect()
        if (ndef.maxSize < ndefMessage.toByteArray().size) {
            Toast.makeText(context, "A tag não tem espaço suficiente para armazenar esses dados", Toast.LENGTH_LONG).show()
            return
        }
        ndef.writeNdefMessage(ndefMessage)
        ndef.close()
        Toast.makeText(context, "Dados escritos com sucesso na tag", Toast.LENGTH_LONG).show()
    }

    private fun readTag(tag: Tag) {
        val ndef = Ndef.get(tag)
        ndef?.connect()

        val message = ndef?.ndefMessage
        message?.let {
            val payload = String(it.records[0].payload)
            Toast.makeText(this, "Conteúdo da tag: $payload", Toast.LENGTH_LONG).show()
        }

        ndef?.close()
    }

//    private fun cloneTag(tag: Tag) {
//        val ndef = Ndef.get(tag)
//        ndef?.connect()
//        val message = ndef?.ndefMessage
//        message?.let {
//            val payload = it.toByteArray()
//            val clonedTag = TagUtil.createTag(payload)
//            writeTag(clonedTag)
//            Toast.makeText(this, "Tag clonada com sucesso", Toast.LENGTH_LONG).show()
//        }
//        ndef?.close()
//    }


    private fun cloneTag(context: Context, tag: Tag) {
        // TODO - implementar aqui
    }




}