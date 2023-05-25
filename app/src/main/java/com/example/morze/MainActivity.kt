package com.example.morze

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var morze: TextView
    private lateinit var vod: EditText
    private lateinit var switch1: Switch
    private lateinit var switch4: Switch
    private lateinit var radio8: RadioButton
    private lateinit var radio9: RadioButton
    private lateinit var radio10: RadioButton

    private var skorostPalki = 0
    private var skorostTochki = 0
    private var delay = 0

    private var threadCheck: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        morze = findViewById(R.id.morze)
        vod = findViewById(R.id.vod)
        val exit = findViewById<Button>(R.id.exit)
        val play = findViewById<Button>(R.id.play)
        val stop = findViewById<Button>(R.id.stop)
        val delete = findViewById<Button>(R.id.delete)
        switch1 = findViewById(R.id.switch1)
        switch4 = findViewById(R.id.switch4)
        radio8 = findViewById(R.id.radioButton8)
        radio9 = findViewById(R.id.radioButton9)
        radio10 = findViewById(R.id.radioButton10)
        morze.movementMethod = ScrollingMovementMethod()
        vod.filters = arrayOf<InputFilter>(AllCaps())

        exit.setOnClickListener {
            finishAffinity()
        }

        vod.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                convertToMorse(s.toString())
            }
        })

        delete.setOnClickListener {
            morze.text = ""
            vod.text.clear()
        }

        stop.setOnClickListener {
            threadCheck?.interrupt()
        }

        play.setOnClickListener {
            threadCheck?.interrupt()
            threadCheck = thread {
                readMorse()
            }
        }
    }

    private fun convertToMorse(text: String) {
        val word = text.toCharArray().map { it.toString() }.toTypedArray()
        val listok: MutableList<String> = ArrayList()

        for (char in word) {
            when (char) {
                " " -> listok.add(" ")
                "A" -> listok.add("·−")
                "B" -> listok.add("−···")
                "C" -> listok.add("−·−·")
                "D" -> listok.add("−··")
                "E" -> listok.add("·")
                "F" -> listok.add("··−·")
                "G" -> listok.add("−−·")
                "H" -> listok.add("····")
                "I" -> listok.add("··")
                "J" -> listok.add("·−−−")
                "K" -> listok.add("−·−")
                "L" -> listok.add("·−··")
                "M" -> listok.add("−−")
                "N" -> listok.add("−·")
                "O" -> listok.add("−−−")
                "P" -> listok.add("·−−·")
                "Q" -> listok.add("−−·−")
                "R" -> listok.add("·−·")
                "S" -> listok.add("···")
                "T" -> listok.add("−")
                "U" -> listok.add("··−")
                "V" -> listok.add("···−")
                "W" -> listok.add("·−−")
                "X" -> listok.add("−··−")
                "Y" -> listok.add("−·−−")
                "Z" -> listok.add("−−··")
                "1" -> listok.add("·−−−−")
                "2" -> listok.add("··−−−")
                "3" -> listok.add("···−−")
                "4" -> listok.add("····−")
                "5" -> listok.add("·····")
                "6" -> listok.add("−····")
                "7" -> listok.add("−−···")
                "8" -> listok.add("−−−··")
                "9" -> listok.add("−−−−·")
                "0" -> listok.add("−−−−−")
            }
        }

        morze.text = listok.joinToString("", "", "")
        val vivod = Array(morze.text.length) { morze.text[it].toString() }
        print(vivod)
    }

    private fun showLight() {
        try {
            val camManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId: String = camManager.cameraIdList[0]
            camManager.setTorchMode(cameraId, true)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun offLight() {
        try {
            val camManager1 = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId1: String = camManager1.cameraIdList[0]
            camManager1.setTorchMode(cameraId1, false)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun vibrate(duration: Long) {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun longDelayStartWorking() {
        if (switch4.isChecked) {
            showLight()
        }
        if (switch1.isChecked) {
            vibrate(skorostPalki.toLong())
        }
        Thread.sleep(skorostPalki.toLong())
        offLight()
        Thread.sleep(delay.toLong())
    }
    private fun smallDelayStartWorking() {
        if (switch4.isChecked) {
            showLight()
        }
        if (switch1.isChecked) {
            vibrate(skorostTochki.toLong())
        }
        Thread.sleep(skorostTochki.toLong())
        offLight()
        Thread.sleep(delay.toLong())
    }

    private fun readMorse() {
        val vivod = Array(morze.text.length) { morze.text[it].toString() }
        print(vivod)

        when {
            radio8.isChecked -> {
                skorostPalki = 920
                skorostTochki = 480
                delay = 480
            }
            radio9.isChecked -> {
                skorostPalki = 480
                skorostTochki = 240
                delay = 240
            }
            radio10.isChecked -> {
                skorostPalki = 240
                skorostTochki = 120
                delay = 120
            }
        }

        val arraySize = vivod.size
        var z = 0
        while (z < arraySize) {
            try {
                when (vivod[z]) {
                    "−" -> {
                        longDelayStartWorking()
                            z++
                        }
                    "·" -> {
                        smallDelayStartWorking()
                        z++
                    }
                }
            } catch (e: InterruptedException) {
                offLight()
                vibrate(1)
                break
            }
        }
    }
}




