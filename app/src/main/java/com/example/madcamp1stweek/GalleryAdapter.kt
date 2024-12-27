package com.example.madcamp1stweek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class GalleryAdapter(
    private val imageUrls: MutableList<String>, // MutableList로 설정
    private val descriptions: MutableList<String>,
    private val onItemClick: (String, String) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.galleryImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        Glide.with(holder.imageView.context)
            .load(imageUrls[position])
            .transform(RoundedCorners(24))
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClick(imageUrls[position], descriptions[position])
        }
    }

    override fun getItemCount(): Int = imageUrls.size

    fun addImage(imageUrl: String, description: String) {
        imageUrls.add(imageUrl)
        descriptions.add(description)
        notifyItemInserted(imageUrls.size - 1)
    }
}
