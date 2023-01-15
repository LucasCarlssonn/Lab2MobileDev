package com.ebookfrenzy.lab2.tabFragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ebookfrenzy.lab2.RecyclerAdapter
import com.ebookfrenzy.lab2.SharedViewModel
import com.ebookfrenzy.lab2.databinding.FragmentQuestionsBinding



class QuestionsFragment: Fragment() {
    private var _binding: FragmentQuestionsBinding? = null
    private val binding get() = _binding!!
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_questions, container, false)
        _binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set a LinearLayoutManager to handle Android
        // RecyclerView behavior
        Log.i("Wack", "OnViewCreated")
        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(this)
        binding.recyclerView.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                0 -> {
                    val result = data?.getStringExtra("sleepAnswer")
                    Log.i("sleepAnswer", "Result: $result")
                    if (result != null) {
                        model.setSleepAnswer(result)
                    }
                }
                1 -> {
                    val result = data?.getStringExtra("nutritionAnswer")
                    Log.i("nutritionAnswer", "Result: $result")
                    if (result != null) {
                        model.setNutritionAnswer(result)
                    }
                }
                2 -> {
                    val result = data?.getStringExtra("stressAnswer")
                    Log.i("stressAnswer", "Result: $result")
                    if (result != null) {
                        model.setStressAnswer(result)
                    }
                }
                3 -> {
                    val result = data?.getStringExtra("alcoholAnswer")
                    Log.i("alcoholAnswer", "Result: $result")
                    if (result != null) {
                        model.setAlcoholAnswer(result)
                    }
                }

            }

        }
    }

}