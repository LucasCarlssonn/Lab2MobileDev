package com.ebookfrenzy.lab2.answerActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ebookfrenzy.lab2.R
import com.ebookfrenzy.lab2.databinding.ActivitySleepAnswerBinding
import com.ebookfrenzy.lab2.databinding.ActivityStressAnswerBinding

class StressAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStressAnswerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStressAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonForResult.setOnClickListener {
            val data = Intent()
            val answer = binding.stressAnswer.text.toString()
            if (answer != "") {
                data.putExtra("stressAnswer", answer)
                setResult(RESULT_OK, data)
                finish()
            } else {
                Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show()
            }
        }
    }
}