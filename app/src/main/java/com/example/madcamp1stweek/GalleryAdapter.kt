package com.example.madcamp1stweek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryAdapter(
    private val galleryItems: MutableList<GalleryItem>,
    private val onItemClick: (String, String, Float, String) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.galleryImageView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        val hairshopNameTextView: TextView = itemView.findViewById(R.id.hairshopNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = galleryItems[position]
        holder.hairshopNameTextView.text = item.hairshopName
        holder.descriptionTextView.text = item.description
        holder.ratingTextView.text = "⭐ ${item.rating}"
        Glide.with(holder.imageView.context)
            .load(item.imageUrl)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClick(item.imageUrl, item.description, item.rating, item.hairshopName)
        }
    }

    override fun getItemCount(): Int = galleryItems.size

    fun addImage(item: GalleryItem) {
        galleryItems.add(0, item)
        notifyItemInserted(0)
    }
}
