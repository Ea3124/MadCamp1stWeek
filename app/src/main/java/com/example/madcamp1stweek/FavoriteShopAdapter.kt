// FavoriteShopAdapter.kt
package com.example.madcamp1stweek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoriteShopAdapter(private val favoriteNames: List<String>) :
    RecyclerView.Adapter<FavoriteShopAdapter.FavoriteShopViewHolder>() {

    // ViewHolder 클래스
    class FavoriteShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        val name = favoriteNames[position]
        holder.textViewFavoriteName.text = name
    }

    // 아이템의 총 개수 반환
    override fun getItemCount(): Int = favoriteNames.size
}
