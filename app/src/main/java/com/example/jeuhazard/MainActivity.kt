package com.example.jeuhazard

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var buttonOK: Button
    private lateinit var editTextNumber: EditText
    private lateinit var listViewHisto: ListView
    private lateinit var textViewIndication: TextView
    private lateinit var progressBarScore: ProgressBar
    private lateinit var textViewScore: TextView
    private lateinit var textViewScoreCumul: TextView

    private var secret: Int = 0
    private var nombreEssais = 1
    private val nombreMaxEssais = 6
    private val historique = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private var scoreCumule = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ Important : configure la toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        // ✅ Lier tous les composants
        editTextNumber = findViewById(R.id.editTextNumber)
        listViewHisto = findViewById(R.id.listViewHisto)
        textViewIndication = findViewById(R.id.textViewIndication)
        textViewScore = findViewById(R.id.textViewScore)
        progressBarScore = findViewById(R.id.progressBarScore)
        buttonOK = findViewById(R.id.buttonOK)
        textViewScoreCumul = findViewById(R.id.textViewScoreCumul)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, historique)
        listViewHisto.adapter = adapter

        initialisation()

        buttonOK.setOnClickListener {
            val str = editTextNumber.text.toString()
            val number = str.toIntOrNull()

            if (number == null) {
                Toast.makeText(this, getString(R.string.entree_invalide), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            historique.add("$nombreEssais => $number")
            adapter.notifyDataSetChanged()

            Log.i("MyInfos", getString(R.string.essai_numero) + "$nombreEssais => $number")

            textViewScore.text = nombreEssais.toString()
            progressBarScore.progress = nombreEssais

            when {
                number > secret -> textViewIndication.text = getString(R.string.nombre_plus_grand)
                number < secret -> textViewIndication.text = getString(R.string.nombre_plus_petit)
                else -> {
                    textViewIndication.text = getString(R.string.bravo)
                    scoreCumule += 5
                    retry()
                    return@setOnClickListener
                }
            }

            editTextNumber.setText("")

            if (nombreEssais >= nombreMaxEssais && number != secret) {
                retry()
            }

            nombreEssais++
        }
    }

    private fun retry() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.str_nouvel_essai))
            .setPositiveButton("OK") { _, _ ->
                initialisation()
            }
            .setNegativeButton("Finish") { _, _ ->
                finish()
            }
            .create()

        alertDialog.show()
    }

    private fun initialisation() {
        nombreEssais = 1
        secret = (1..100).random()
        editTextNumber.requestFocus()
        progressBarScore.progress = nombreEssais
        textViewIndication.text = ""
        editTextNumber.setText("")
        textViewScore.text = nombreEssais.toString()
        textViewScoreCumul.text = scoreCumule.toString()
        historique.clear()
        adapter.notifyDataSetChanged()
        //Toast.makeText(this, "Nombre secret = $secret", Toast.LENGTH_SHORT).show()
    }
}
