package com.example.a7minutesworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.flStart?.setOnClickListener{
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

//        val fLStartButton:FrameLayout = findViewById(R.id.flStart)
//        fLStartButton.setOnClickListener {
//            Toast.makeText(
//                this@MainActivity,
//                "Here we will start the exercise.",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}