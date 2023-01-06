/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ebookfrenzy.lab2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader

import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/** The main activity to provide interactions with users.  */
class SecondActivity : AppCompatActivity() {

    private var resultTextView: TextView? = null
    private var inputEditText: EditText? = null
    private var executorService: ExecutorService? = null
    private var scrollView: ScrollView? = null
    private var predictButton: Button? = null

    // TODO 5: Define a NLClassifier variable
    private var textClassifier: NLClassifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        Log.v(TAG, "onCreate")

        executorService = Executors.newSingleThreadExecutor()
        resultTextView = findViewById(R.id.result_text_view)
        inputEditText = findViewById(R.id.input_text)
        scrollView = findViewById(R.id.scroll_view)

        predictButton = findViewById(R.id.predict_button)
        predictButton!!.setOnClickListener { v: View -> classify(inputEditText!!.text.toString()) }

        // TODO 3: Call the method to download TFLite model
        downloadModel("sentiment_analysis")
    }

    /** Send input text to TextClassificationClient and get the classify messages.  */
    private fun classify(text: String) {
        executorService!!.execute {
            // TODO 7: Run sentiment analysis on the input text
            val results = textClassifier!!.classify(text)

            // TODO 8: Convert the result to a human-readable text
            var textToShow = "Input: $text\n\nOutput:\n"
            for (i in results.indices) {
                val result = results[i]
                textToShow += String.format("    %s: %s\n", result.label, result.score)

            }
            textToShow += "-----------\n"
            // Show classification result on screen
            showResult(textToShow)
        }
    }

    /** Show classification result on the screen.  */
    private fun showResult(textToShow: String) {
        // Run on UI thread as we'll updating our app UI
        runOnUiThread {
            // Append the result to the UI.
            resultTextView!!.append(textToShow)

            // Clear the input text.
            inputEditText!!.text.clear()

            // Scroll to the bottom to show latest entry's classification result.
            scrollView!!.post { scrollView!!.fullScroll(View.FOCUS_DOWN) }
        }
    }

    // TODO 2: Implement a method to download TFLite model from Firebase

    /** Download model from Firebase ML.  */
    @Synchronized
    private fun downloadModel(modelName: String) {
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("sentiment_analysis", DownloadType.LOCAL_MODEL, conditions)
            .addOnSuccessListener { model ->
                try {
                    // TODO 6: Initialize the NLClassifier
                    textClassifier = NLClassifier.createFromFile(model.file)
                    predictButton!!.isEnabled = true
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to initialize the model.", e)
                    Toast.makeText(
                        this@SecondActivity,
                        "Model initialization failed.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    predictButton!!.isEnabled = false
                }


            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to download the model.", e)
                Toast.makeText(
                    this@SecondActivity,
                    "Model download failed.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

    }

    companion object {
        private val TAG = "TextClassificationDemo"
    }

}
