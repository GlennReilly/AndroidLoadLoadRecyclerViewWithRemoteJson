package com.glennreilly.androidproficiencyexercise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glennreilly.androidproficiencyexercise.R
import com.glennreilly.androidproficiencyexercise.models.FactRow

class CanAdapter(
    private val itemList: List<FactRow>,
    private val activity: FragmentActivity?
) : RecyclerView.Adapter<CanAdapter.ItemOfInterestViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ItemOfInterestViewHolder {
        context = parent.context

        val rowView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_layout, parent, false)

        return ItemOfInterestViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ItemOfInterestViewHolder, position: Int) {
        holder.rowView.findViewById<AppCompatTextView>(R.id.title).apply {
            if (!itemList[position].title.isNullOrEmpty()) {
                visibility = View.VISIBLE
                text = itemList[position].title
            } else visibility = View.GONE
        }

        holder.rowView.findViewById<AppCompatTextView>(R.id.description).apply {
            if (!itemList[position].description.isNullOrEmpty()) {
                visibility = View.VISIBLE
                text = itemList[position].description
            } else visibility = View.GONE
        }

        val imageView: ImageView = holder.rowView.findViewById<AppCompatImageView>(R.id.image)

        activity?.let {
            Glide.with(it)
                .load(itemList[position].imageHref)
                .into(imageView)
        }
    }

    override fun getItemCount() = itemList.size

    data class ItemOfInterestViewHolder(val rowView: View) : RecyclerView.ViewHolder(rowView)
}