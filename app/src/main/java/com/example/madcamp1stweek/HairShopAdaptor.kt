package com.example.madcamp1stweek

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HairShopAdapter(
    private val shopList: List<HairShop>,
    private val context: Context
) : RecyclerView.Adapter<HairShopAdapter.HairShopViewHolder>() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)

    // 키 이름: "liked_shops"
    private val likedShopsKey = "liked_shops"

    // ViewHolder 클래스
    class HairShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewHairShop)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPhone: TextView = itemView.findViewById(R.id.textViewPhone)
        val imageViewCall: ImageView = itemView.findViewById(R.id.imageViewCall)
        val imageViewHeart: ImageView = itemView.findViewById(R.id.imageViewHeart) // 하트 아이콘 추가
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
        holder.textViewPhone.text = shop.phoneNumber

        Glide.with(holder.itemView.context)
            .load(shop.imageResId)
            .into(holder.imageView)

        // 하트 아이콘 상태 설정
        if (shop.myshop) {
            holder.imageViewHeart.setImageResource(R.drawable.ic_heart_filled) // 빨간 하트
        } else {
            holder.imageViewHeart.setImageResource(R.drawable.ic_heart_empty) // 빈 하트
        }

        // 전화 아이콘 클릭 리스너 설정
        holder.imageViewCall.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:${shop.phoneNumber}")
            holder.itemView.context.startActivity(dialIntent)
        }

        // 하트 아이콘 클릭 리스너
        holder.imageViewHeart.setOnClickListener {
            // 상태 토글
            shop.myshop = !shop.myshop

            // SharedPreferences 업데이트
            val editor = sharedPreferences.edit()
            val likedShops = sharedPreferences.getStringSet(likedShopsKey, mutableSetOf())?.toMutableSet()
                ?: mutableSetOf()

            if (shop.myshop) {
                likedShops.add(shop.phoneNumber)
            } else {
                likedShops.remove(shop.phoneNumber)
            }

            editor.putStringSet(likedShopsKey, likedShops)
            editor.apply()

            // UI 업데이트
            notifyItemChanged(holder.adapterPosition)
        }
    }

    // 아이템의 총 개수 반환
    override fun getItemCount(): Int = shopList.size
}