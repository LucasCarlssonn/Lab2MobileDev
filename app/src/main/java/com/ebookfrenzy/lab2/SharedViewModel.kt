package com.ebookfrenzy.lab2

import android.app.Application
import androidx.lifecycle.*
import com.github.mikephil.charting.data.BarEntry

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    // variable to contain message whenever
    // it gets changed/modified(mutable)

    var entries = MutableLiveData<ArrayList<HashMap<String, Float>>>()
    val sleepAnswer = MutableLiveData<String>()
    val nutritionAnswer = MutableLiveData<String>()
    val stressAnswer = MutableLiveData<String>()
    val alcoholAnswer = MutableLiveData<String>()

    // function to send message
    fun setSleepAnswer(text: String) {
        sleepAnswer.value = text
    }

    fun setNutritionAnswer(text: String) {
        nutritionAnswer.value = text
    }

    fun setStressAnswer(text: String) {
        stressAnswer.value = text
    }

    fun setAlcoholAnswer(text: String) {
        alcoholAnswer.value = text
    }
    fun addEntry(key: String, value: Float ) {
        val currentEntries = entries.value ?: ArrayList()
        currentEntries.add(hashMapOf(key to value))
        entries.value = currentEntries
    }
    fun entryExists(key: String): Boolean {
        val currentEntries = entries.value ?: ArrayList()
        for (entry in currentEntries) {
            if (entry.containsKey(key)) {
                return true
            }
        }
        return false
    }
    fun updateEntry(key: String, value: Float) {
        val currentEntries = entries.value ?: ArrayList()
        for (entry in currentEntries) {
            if (entry.containsKey(key)) {
                entry[key] = value
            }
        }
        entries.value = currentEntries
    }
    fun clearEntries() {
        entries.value = ArrayList()
    }

}
