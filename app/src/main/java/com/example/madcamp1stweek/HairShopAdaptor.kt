// HairShopAdapter.kt
package com.example.madcamp1stweek

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.Filter
import android.widget.Filterable
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.madcamp1stweek.databinding.ItemShopBinding

class HairShopAdapter(
    private var shopList: MutableList<HairShop>,
    private val context: Context
) : RecyclerView.Adapter<HairShopAdapter.HairShopViewHolder>(), Filterable {

    private var filteredShopList: MutableList<HairShop> = shopList

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)

    // 키 이름: "liked_shops"
    private val likedShopsKey = "liked_shops"

    // ViewHolder 클래스 - View Binding 사용
    class HairShopViewHolder(val binding: ItemShopBinding) : RecyclerView.ViewHolder(binding.root)

    // 아이템 레이아웃을 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HairShopViewHolder {
        val binding = ItemShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HairShopViewHolder(binding)
    }

    // 데이터와 뷰를 바인딩
    override fun onBindViewHolder(holder: HairShopViewHolder, position: Int) {
        val shop = filteredShopList[position] // filteredShopList 사용
        holder.binding.textViewName.text = shop.name
        holder.binding.textViewPhone.text = shop.phoneNumber

        // 항목의 배경을 투명하게 설정
        holder.itemView.setBackgroundColor(Color.TRANSPARENT)

        // 모서리 반경을 픽셀 단위로 가져오기
        val radiusInPixels = context.resources.getDimensionPixelSize(R.dimen.image_corner_radius)

        // RequestOptions에 CenterCrop과 RoundedCorners 변환 적용
        val requestOptions = RequestOptions()
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))

        // Glide를 사용하여 이미지 로드 및 변환 적용
        Glide.with(holder.itemView.context)
            .load(shop.imageResId) // 또는 shop.imageUrl
            .apply(requestOptions)
            .into(holder.binding.imageViewHairShop) // 올바른 ImageView 참조

        // 하트 아이콘 상태 설정
        if (shop.myshop) {
            holder.binding.imageViewHeart.setImageResource(R.drawable.ic_heart_filled) // 빨간 하트
        } else {
            holder.binding.imageViewHeart.setImageResource(R.drawable.ic_heart_empty) // 빈 하트
        }

        // 전화 아이콘 클릭 리스너 설정
        holder.binding.imageViewCall.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:${shop.phoneNumber}")
            context.startActivity(dialIntent)
        }

        // 하트 아이콘 클릭 리스너
        holder.binding.imageViewHeart.setOnClickListener {
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim()?.lowercase() ?: ""
                val results = if (query.isEmpty()) {
                    shopList
                } else {
                    shopList.filter {
                        it.name.lowercase().contains(query) ||
                                it.phoneNumber.contains(query)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = results
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredShopList = (results?.values as? List<HairShop>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    fun updateShops(newShops: MutableList<HairShop>) {
        shopList = newShops
        filteredShopList = shopList
        notifyDataSetChanged()
    }

    // 아이템의 총 개수 반환
    override fun getItemCount(): Int = filteredShopList.size
}
