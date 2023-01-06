package com.ebookfrenzy.lab2.answerActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            data.putExtra("sleepAnswer", answer)
            setResult(RESULT_OK, data)
            finish()
        }

    }

}