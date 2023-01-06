package com.ebookfrenzy.lab2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ebookfrenzy.lab2.answerActivities.AlcoholAnswerActivity
import com.ebookfrenzy.lab2.answerActivities.NutritionAnswerActivity
import com.ebookfrenzy.lab2.answerActivities.SleepAnswerActivity
import com.ebookfrenzy.lab2.answerActivities.StressAnswerActivity

class RecyclerAdapter(val fragment: Fragment) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val titles = fragment.resources.getStringArray(R.array.cardItemTitles)
    private val details = fragment.resources.getStringArray(R.array.cardItemDetails)

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView
        init {
            itemImage = itemView.findViewById(R.id.itemImage)
            itemTitle = itemView.findViewById(R.id.itemTitle)
            itemDetail = itemView.findViewById(R.id.itemDetail)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int):
            ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text = titles[i]
        viewHolder.itemDetail.text = details[i]
        viewHolder.itemImage.setImageResource(R.drawable.mental_health)

        val answerActivities = arrayOf(
            SleepAnswerActivity::class.java,
            NutritionAnswerActivity::class.java,
            StressAnswerActivity::class.java,
            AlcoholAnswerActivity::class.java
        )

        viewHolder.itemView.setOnClickListener { v ->
            val context = v.context
            val intent = Intent(context, answerActivities[i])
            fragment.startActivityForResult(intent, i)

        }
    }


    override fun getItemCount(): Int {
        return titles.size

    }

}
