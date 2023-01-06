package com.ebookfrenzy.lab2.tabFragments

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.ebookfrenzy.lab2.SharedViewModel
import com.ebookfrenzy.lab2.databinding.FragmentAnalyzesBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AnalyzesFragment : Fragment() {
    private var _binding : FragmentAnalyzesBinding? = null
    private val binding get() = _binding!!

    private var resultTextView: TextView? = null
    private var resultTitle: TextView? = null
    private var barChart: BarChart? = null
    private var executorService: ExecutorService? = null
    private var predictButton: Button? = null

    private var viewModel: SharedViewModel? = null

    // TODO 5: Define a NLClassifier variable
    private var textClassifier: NLClassifier? = null
    private var modelDownloaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAnalyzesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        executorService = Executors.newSingleThreadExecutor()
        resultTextView = binding.resultTextView
        barChart = binding.barChart
        resultTitle = binding.resultTitle
        barChart!!.setNoDataText("")

        // Observers for the different answers
        viewModel?.sleepAnswer?.observe(viewLifecycleOwner) { answer ->
            Log.d("Answer", answer)
            checkModelDownloaded(answer, "sleep")
        }
        viewModel?.nutritionAnswer?.observe(viewLifecycleOwner) { answer ->
            Log.d("Answer", answer)
            checkModelDownloaded(answer, "nutrition")
        }
        viewModel?.stressAnswer?.observe(viewLifecycleOwner) { answer ->
            Log.d("Answer", answer)
            checkModelDownloaded(answer, "stress")
        }
        viewModel?.alcoholAnswer?.observe(viewLifecycleOwner) { answer ->
            Log.d("Answer", answer)
            checkModelDownloaded(answer, "alcohol")
        }
        viewModel?.entries?.observe(viewLifecycleOwner) { entries ->
            showEntries(entries)
            showBarChart(entries)
        }

        downloadModel()
    }


    /** Check if model is downloaded, if not Print message*/
    private fun checkModelDownloaded(answer : String, type : String) {
        if (modelDownloaded) {
            classify(answer, type)
        } else {
            Toast.makeText(context, "Model is not downloaded yet", Toast.LENGTH_SHORT).show()
        }
    }

    /** Send input text to TextClassificationClient and get the classify messages.  */
    private fun classify(text: String, type: String) {
        val answersTitle = "According to your answers:"
        resultTitle!!.text = answersTitle
        executorService!!.execute {
            // TODO 7: Run sentiment analysis on the input text
            val results = textClassifier!!.classify(text)
            addResultToEntries(results, type)
        }
    }


    /** Translate Classification results to text */
    private fun addResultToEntries(
        results: MutableList<org.tensorflow.lite.support.label.Category>,
        type: String) {
        for (i in results.indices) {
            val result = results[i]
            if (result.score > 0.5) {
                val label = result.label.toString().toInt()
                Log.i("Label", label.toString())
                if (label == 1) {
                    view?.post {
                        if (viewModel?.entryExists(type) == true) {
                            viewModel!!.updateEntry(type, result.score)
                        } else {
                            viewModel?.addEntry(type, result.score)
                        }
                    }
                } else {
                    view?.post {
                        if (viewModel?.entryExists(type) == true) {
                            viewModel!!.updateEntry(type, -result.score)
                        } else {
                            viewModel?.addEntry(type, -result.score)
                        }
                    }

                }
            }
        }
    }

    private fun showEntries(entries: ArrayList<HashMap<String, Float>>) {

        val resultToText = hashMapOf(
            "sleepPositive" to "You have been sleeping well. Keep it up!",
            "sleepNegative" to "You have been sleeping poorly. Try to get more sleep.",
            "nutritionPositive" to "You have eaten nutritious food today. It's good for your health!",
            "nutritionNegative" to "You have eaten unhealthy food today. Try to eat more nutritious food.",
            "stressPositive" to "You have been feeling stressed today. Try to relax.",
            "stressNegative" to "You have been feeling relaxed today. That is good!",
            "alcoholPositive" to "You have been drinking a lot of alcohol today. Try to drink less.",
            "alcoholNegative" to "You have not been drinking a lot of alcohol today. Good Job!"
        )

        requireActivity().runOnUiThread {
            resultTextView!!.text = ""
            for (entry in entries) {
                for ((key, value) in entry) {
                    if (value > 0) {
                        resultTextView!!.append(resultToText[key + "Positive"]!!)
                    } else {
                        resultTextView!!.append(resultToText[key + "Negative"]!!)
                    }
                    resultTextView!!.append("\n")
                }

            }
        }


    }

    /** Download model from Firebase ML.  */
    @Synchronized
    private fun downloadModel() {
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("sentiment_analysis", DownloadType.LOCAL_MODEL, conditions)
            .addOnSuccessListener { model ->
                try {
                    // TODO 6: Initialize the NLClassifier
                    textClassifier = NLClassifier.createFromFile(model.file)
                    Log.i("Model", "Model downloaded successfully")
                    modelDownloaded = true
                } catch (e: Exception) {
                    Log.e("Model", "Failed to initialize the model.", e)
                    Toast.makeText(
                        this.context,
                        "Model initialization failed.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    predictButton!!.isEnabled = false
                }


            }
            .addOnFailureListener { e ->
                Log.e("Model", "Failed to download the model.", e)
                Toast.makeText(
                    this.context,
                    "Model download failed.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

    }
    /** Generate and show barchart based on answers  */
    private fun showBarChart(entries: ArrayList<HashMap<String, Float>>) {
        Log.d("Entries", entries.toString())
        barChart?.setDrawBarShadow(false)
        barChart?.setDrawValueAboveBar(true)
        barChart?.description?.isEnabled = false
        val colors = listOf(
            Color.parseColor("#0000FF"),
            Color.parseColor("#008000"),
            Color.parseColor("#FFA500"),
            Color.parseColor("#800080")
        )

        val datasets = ArrayList<BarDataSet>()

        var i = 0f
        for (item in entries) {
            val value = BarEntry(i, item.values.first(), item.keys.first())
            val dataSet = BarDataSet(listOf(value), item.keys.first())
            dataSet.color = colors[i.toInt()]
            dataSet.valueTextSize = 12f
            datasets.add(dataSet)
            i++
        }
        val data = BarData(datasets as List<IBarDataSet>?)
        barChart?.data = data
        barChart?.setTouchEnabled(false)
        barChart?.invalidate()
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

}