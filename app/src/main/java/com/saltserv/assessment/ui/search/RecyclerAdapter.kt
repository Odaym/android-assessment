package com.saltserv.assessment.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.saltserv.assessment.R
import com.saltserv.assessment.responses.ArtistItem

class RecyclerAdapter(
    private val onItemClickListener: (ArtistItem) -> Unit
) : RecyclerView.Adapter<RecyclerAdapter.ArtistsViewHolder>(),
    Observer<List<ArtistItem>?> {

    private var data = emptyList<ArtistItem>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ArtistsViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ArtistsViewHolder(
            inflater.inflate(R.layout.item, parent, false),
            onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: ArtistsViewHolder, position: Int) {
        holder.bindListItem(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onChanged(newData: List<ArtistItem>?) {
        if (newData != null) {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = data[oldItemPosition]
                    val newItem = newData[newItemPosition]
                    return oldItem::class.java == newItem::class.java && oldItem.name == newItem.name
                }

                override fun getOldListSize() = data.size

                override fun getNewListSize() = newData.size

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ) = true

            })
            data = newData
            diff.dispatchUpdatesTo(this)
        }
    }

    inner class ArtistsViewHolder(itemView: View, val onItemClickListener: (ArtistItem) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.name)
        private val image = itemView.findViewById<ImageView>(R.id.image)

        fun bindListItem(listItem: ArtistItem) {
            itemView.setOnClickListener { onItemClickListener(listItem) }
            name.text = listItem.name
            Picasso.get()
                .load(listItem.images.firstOrNull()?.url)
                .resize(200, 200)
                .centerCrop()
                .into(image)
        }
    }
}