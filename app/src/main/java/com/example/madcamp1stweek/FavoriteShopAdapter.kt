package com.example.madcamp1stweek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class FavoriteShopAdapter(
    private var favoriteNames: List<HairShop>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<FavoriteShopAdapter.FavoriteShopViewHolder>(), Filterable {

    private var favoriteNamesFull: List<HairShop> = ArrayList(favoriteNames)

    interface OnItemClickListener {
        fun onItemClick(hairShop: HairShop)
    }

    // ViewHolder 클래스
    class FavoriteShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewFavorite: ImageView = itemView.findViewById(R.id.imageViewHairShop)
        val textViewFavoriteName: TextView = itemView.findViewById(R.id.textViewFavoriteName)
    }

    // 아이템 레이아웃을 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_shop, parent, false)
        return FavoriteShopViewHolder(view)
    }

    // 데이터와 뷰를 바인딩
    override fun onBindViewHolder(holder: FavoriteShopViewHolder, position: Int) {
        val favorite = favoriteNames[position]
        holder.textViewFavoriteName.text = favorite.name

        // Glide를 사용하여 원형으로 이미지 로드
        Glide.with(holder.itemView.context)
            .load(favorite.imageResId) // 이미지 리소스 ID 또는 URL
            .transform(CircleCrop()) // 원형 변환 적용
            .into(holder.imageViewFavorite) // 올바른 ImageView 참조

        // 리스트 항목의 배경을 투명하게 설정
        holder.itemView.setBackgroundColor(android.graphics.Color.TRANSPARENT)

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(favorite)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<HairShop>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(favoriteNamesFull)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (shop in favoriteNamesFull) {
                        if (shop.name.lowercase().contains(filterPattern)) {
                            filteredList.add(shop)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                favoriteNames = results?.values as List<HairShop>
                notifyDataSetChanged()
            }
        }
    }

    // 아이템의 총 개수 반환
    override fun getItemCount(): Int = favoriteNames.size
}
