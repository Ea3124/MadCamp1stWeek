package com.example.madcamp1stweek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.TextView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners



class GalleryAdapter(
    private val imageUrls: MutableList<String>,
    private val descriptions: MutableList<String>,
    private val ratings: MutableList<Float>,
    private val onItemClick: (String, String, Float) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.galleryImageView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        Glide.with(holder.imageView.context)
            .load(imageUrls[position])
            .into(holder.imageView)

        holder.descriptionTextView.text = descriptions[position]
        holder.ratingTextView.text = "⭐ ${ratings[position]}"

        holder.itemView.setOnClickListener {
            onItemClick(imageUrls[position], descriptions[position], ratings[position])
        }
    }

    override fun getItemCount(): Int = imageUrls.size

    fun addImage(imageUrl: String, description: String, rating: Float) {
        imageUrls.add(0, imageUrl)
        descriptions.add(0, description)
        ratings.add(0, rating)
        notifyItemInserted(0)
    }
}
