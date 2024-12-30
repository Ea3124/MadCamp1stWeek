package com.example.madcamp1stweek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryAdapter(
    val dashboardItems: MutableList<DashboardItem>,
    private val onItemClick: (String, String, Float, String, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_GALLERY = 1
    }

    override fun getItemCount(): Int = dashboardItems.size

    override fun getItemViewType(position: Int): Int {
        return when (dashboardItems[position]) {
            is DashboardItem.Header -> VIEW_TYPE_HEADER
            is DashboardItem.GalleryCard -> VIEW_TYPE_GALLERY
        }
    }

    // 여러 ViewHolder를 반환하므로, return 타입은 RecyclerView.ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_gallery, parent, false)
                GalleryViewHolder(view)
            }
        }
    }

    // onBindViewHolder에 들어오는 holder는 RecyclerView.ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = dashboardItems[position]) {
            is DashboardItem.Header -> {
                // (holder as HeaderViewHolder).bind(...) 필요시
            }
            is DashboardItem.GalleryCard -> {
                (holder as GalleryViewHolder).bind(item.galleryItem, onItemClick)
            }
        }
    }

    // Header 전용 ViewHolder
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // item_header.xml에 대한 bind 로직이 필요하다면 여기에
    }

    // Gallery 전용 ViewHolder
    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.galleryImageView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        private val hairshopNameTextView: TextView = itemView.findViewById(R.id.hairshopNameTextView)

        fun bind(
            galleryItem: GalleryItem,
            onItemClick: (String, String, Float, String, Int) -> Unit
        ) {
            hairshopNameTextView.text = galleryItem.hairshopName
            descriptionTextView.text = galleryItem.description
            ratingTextView.text = "⭐ ${galleryItem.rating}"

            Glide.with(imageView.context)
                .load(galleryItem.imageUrl)
                .into(imageView)

            itemView.setOnClickListener {
                onItemClick(
                    galleryItem.imageUrl,
                    galleryItem.description,
                    galleryItem.rating,
                    galleryItem.hairshopName,
                    galleryItem.index
                )
            }
        }
    }

    fun addImage(item: GalleryItem) {
        dashboardItems.add(DashboardItem.GalleryCard(item))
        notifyItemInserted(dashboardItems.size - 1)
    }
}

