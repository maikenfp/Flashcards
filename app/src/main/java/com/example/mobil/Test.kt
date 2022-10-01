package com.example.mobil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        val goBackBtn = findViewById<Button>(R.id.testButton)
        goBackBtn.setOnClickListener {
            startActivity(Intent(this@Test, MainActivity::class.java))
        }
    }
}