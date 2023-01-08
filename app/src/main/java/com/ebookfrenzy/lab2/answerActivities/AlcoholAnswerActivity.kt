package com.ebookfrenzy.lab2.answerActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ebookfrenzy.lab2.R
import com.ebookfrenzy.lab2.databinding.ActivityAlcoholAnswerBinding
import com.ebookfrenzy.lab2.databinding.ActivitySleepAnswerBinding

class AlcoholAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlcoholAnswerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alcohol_answer)
        binding = ActivityAlcoholAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonForResult.setOnClickListener {
            val data = Intent()
            val answer = binding.alcoholAnswer.text.toString()
            if (answer != "") {
                data.putExtra("alcoholAnswer", answer)
                setResult(RESULT_OK, data)
                finish()
            } else {
                Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show()
            }


        }
    }
}