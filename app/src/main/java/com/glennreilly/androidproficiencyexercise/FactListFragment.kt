package com.glennreilly.androidproficiencyexercise

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FactListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var canAdapter: CanAdapter
    private lateinit var toolbar: Toolbar

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
        toolbar.title = "Get Title from Json.."
        setupRecyclerView(view)
    }

    private fun setupRecyclerView(view: View) {
        val itemList = getDataToDisplay()
        linearLayoutManager = LinearLayoutManager(activity)
        canAdapter = CanAdapter(itemList, activity)
        recyclerView = view.findViewById(R.id.recyclerview)

        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = canAdapter
        }
    }

    private fun getDataToDisplay(): List<ItemOfInterest> {
        val url = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json"
        
        val itemList = listOf(
            ItemOfInterest(
                "Beavers",
                "Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony",
                "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg"
            ),
            ItemOfInterest(
                "Flag",
                null,
                "http://images.findicons.com/files/icons/662/world_flag/128/flag_of_canada.png"
            ),
            ItemOfInterest(
                "titleThree",
                "descriptionThree",
                "http://1.bp.blogspot.com/_VZVOmYVm68Q/SMkzZzkGXKI/AAAAAAAAADQ/U89miaCkcyo/s400/the_golden_compass_still.jpg"
            ),
            ItemOfInterest(
                "Hockey Night in Canada",
                "These Saturday night CBC broadcasts originally aired on radio in 1931. In 1952 they debuted on television and continue to unite (and divide) the nation each week.",
                "http://fyimusic.ca/wp-content/uploads/2008/06/hockey-night-in-canada.thumbnail.jpg"
            ),
            ItemOfInterest(
                "Housing",
                "Warmer than you might think.",
                "http://icons.iconarchive.com/icons/iconshock/alaska/256/Igloo-icon.png"
            ),
            ItemOfInterest(null, null, null),
            ItemOfInterest(
                "Beavers",
                "Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony",
                "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg"
            ),
            ItemOfInterest(
                "Flag",
                null,
                "http://images.findicons.com/files/icons/662/world_flag/128/flag_of_canada.png"
            ),
            ItemOfInterest(
                "titleThree",
                "descriptionThree",
                "http://1.bp.blogspot.com/_VZVOmYVm68Q/SMkzZzkGXKI/AAAAAAAAADQ/U89miaCkcyo/s400/the_golden_compass_still.jpg"
            ),
            ItemOfInterest(
                "Hockey Night in Canada",
                "These Saturday night CBC broadcasts originally aired on radio in 1931. In 1952 they debuted on television and continue to unite (and divide) the nation each week.",
                "http://fyimusic.ca/wp-content/uploads/2008/06/hockey-night-in-canada.thumbnail.jpg"
            ),
            ItemOfInterest(
                "Housing",
                "Warmer than you might think.",
                "http://icons.iconarchive.com/icons/iconshock/alaska/256/Igloo-icon.png"
            )
        )
        return itemList
    }

    class CanAdapter(
        private val itemList: List<ItemOfInterest>,
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
                    .load(itemList[position].imageUrl)
                    .into(imageView)
            }
        }

        data class ItemOfInterestViewHolder(val rowView: View) : RecyclerView.ViewHolder(rowView)
    }

    data class ItemOfInterest(val title: String?, val description: String?, val imageUrl: String?)
}