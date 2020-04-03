package com.glennreilly.androidproficiencyexercise.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.glennreilly.androidproficiencyexercise.R
import com.glennreilly.androidproficiencyexercise.adapters.CanAdapter
import com.glennreilly.androidproficiencyexercise.databinding.FragmentListBinding
import com.glennreilly.androidproficiencyexercise.models.Facts
import com.google.gson.Gson
import org.json.JSONObject

class FactListFragment : Fragment() {
    companion object {
        val TAG = FactListFragment::class.java.simpleName
        const val url = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var canAdapter: CanAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var queue: RequestQueue
    private lateinit var loadingSpinner: ProgressBar
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupPullToRefresh()
        setupRecyclerView()
    }

    private fun setupPullToRefresh() {
        val pullToRefresh: SwipeRefreshLayout = binding.pullToRefresh
        pullToRefresh.setOnRefreshListener {
            requestFacts()
            pullToRefresh.isRefreshing = false
        }
    }

    private fun setupToolbar() {
        toolbar = binding.toolbar
        toolbar.title = getString(R.string.temporaryTitle)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity)
        recyclerView = binding.recyclerview
        loadingSpinner = binding.loadingSpinner
        canAdapter = CanAdapter(emptyList(), activity)

        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = canAdapter
        }
        requestFacts()
    }

    private fun requestFacts() {
        loadingSpinner.visibility = View.VISIBLE
        activity?.let { activity ->
            val cache = DiskBasedCache(activity.cacheDir, 1024 * 1024)
            val network = BasicNetwork(HurlStack())
            val queue = RequestQueue(cache, network).apply {
                start()
            }

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
                    loadingSpinner.visibility = View.INVISIBLE
                    Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
                }
            )

            queue.add(jsonObjectRequest)
        }
    }

    private fun updateRecyclerView(facts: Facts) {
        facts.factRows?.filter {
            (it.description.isNullOrBlank() &&
                    it.title.isNullOrBlank() &&
                    it.imageHref.isNullOrBlank()).not()
        }?.let {
            canAdapter = CanAdapter(it, activity)
        }

        recyclerView.apply {
            adapter = canAdapter.apply { notifyDataSetChanged() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        if (::queue.isInitialized) queue.cancelAll(TAG)
        super.onStop()
    }
}