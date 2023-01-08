package com.ebookfrenzy.lab2.answerActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ebookfrenzy.lab2.databinding.ActivitySleepAnswerBinding

class SleepAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySleepAnswerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySleepAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonForResult.setOnClickListener {
            val data = Intent()
            val answer = binding.sleepAnswer.text.toString()
            if (answer != "") {
                data.putExtra("sleepAnswer", answer)
                setResult(RESULT_OK, data)
                finish()
            } else {
                Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show()
            }
        }

    }

}