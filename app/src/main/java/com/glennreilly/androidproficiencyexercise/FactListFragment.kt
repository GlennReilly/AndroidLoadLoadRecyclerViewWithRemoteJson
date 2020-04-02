package com.glennreilly.androidproficiencyexercise

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.glennreilly.androidproficiencyexercise.models.FactRow
import com.glennreilly.androidproficiencyexercise.models.Facts
import com.google.gson.Gson
import org.json.JSONObject

class FactListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var canAdapter: CanAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var queue: RequestQueue

    companion object {
        val TAG = FactListFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "getting title.. getting title.."
        setupRecyclerView(view)
    }

    private fun setupRecyclerView(view: View) {
        linearLayoutManager = LinearLayoutManager(activity)
        recyclerView = view.findViewById(R.id.recyclerview)
        canAdapter = CanAdapter(emptyList(), activity)

        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = canAdapter
        }
        requestFacts()
    }

    private fun updateRecyclerView(facts: Facts) {
        facts.factRows?.let {
            canAdapter = CanAdapter(it, activity)
        }

        recyclerView.apply {
            adapter = canAdapter.apply { notifyDataSetChanged() }
        }
    }

    private fun requestFacts() {
        val url = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json"
        queue = Volley.newRequestQueue(activity)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response: JSONObject ->
                val facts: Facts = Gson().fromJson(response.toString(), Facts::class.java)
                toolbar.title = facts.title
                updateRecyclerView(facts)
            },
            Response.ErrorListener { error ->
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            }
        )

        queue.add(jsonObjectRequest)

    }

    class CanAdapter(
        private val itemList: List<FactRow>,
        private val activity: FragmentActivity?
    ) :
        RecyclerView.Adapter<CanAdapter.ItemOfInterestViewHolder>() {

        private lateinit var context: Context

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ItemOfInterestViewHolder {
            context = parent.context

            val rowView: View =
                LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
            return ItemOfInterestViewHolder(rowView)
        }

        override fun getItemCount() = itemList.size

        override fun onBindViewHolder(holder: ItemOfInterestViewHolder, position: Int) {
            holder.rowView.findViewById<AppCompatTextView>(R.id.title).apply {
                text = itemList[position].title
            }

            holder.rowView.findViewById<AppCompatTextView>(R.id.description).apply {
                text = itemList[position].description
            }

            val imageView: ImageView = holder.rowView.findViewById<AppCompatImageView>(R.id.image)

            activity?.let {
                Glide.with(it)
                    .load(itemList[position].imageHref)
                    .into(imageView)
            }
        }

        data class ItemOfInterestViewHolder(val rowView: View) : RecyclerView.ViewHolder(rowView)
    }
    
    override fun onStop() {
        queue.cancelAll(TAG)
        super.onStop()
    }
}