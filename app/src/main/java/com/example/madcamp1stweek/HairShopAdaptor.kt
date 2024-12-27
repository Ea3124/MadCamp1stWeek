package com.example.madcamp1stweek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HairShopAdapter(private val shopList: List<HairShop>) : RecyclerView.Adapter<HairShopAdapter.HairShopViewHolder>() {

    // ViewHolder 클래스
    class HairShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPhone: TextView = itemView.findViewById(R.id.textViewPhone)
    }

    // 아이템 레이아웃을 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HairShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return HairShopViewHolder(view)
    }

    // 데이터와 뷰를 바인딩
    override fun onBindViewHolder(holder: HairShopViewHolder, position: Int) {
        val shop = shopList[position]
        holder.textViewName.text = shop.name
        holder.textViewPhone.text = shop.phone
    }

    // 아이템의 총 개수 반환
    override fun getItemCount(): Int = shopList.size
}
