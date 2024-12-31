package com.example.madcamp1stweek

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp1stweek.databinding.ItemFilterOptionBinding

class FilterOptionsRVAdapter: RecyclerView.Adapter<FilterOptionsRVAdapter.ViewHolder>(){

    private var optionList = listOf<FilterOption>()
    // '선택된 옵션'을 저장할 Set
    private val selectedOptions = mutableSetOf<FilterOption>() // 선택 상태 저장 (예시)

    // 이 함수를 통해 외부에서 '선택된 옵션들'을 얻어갈 수 있도록
    fun getSelectedOptions(): Set<FilterOption> {
        return selectedOptions
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addOption(optionList: List<FilterOption>) {
        this.optionList = optionList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFilterOptionBinding = ItemFilterOptionBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = optionList[position]
        holder.bind(option, selectedOptions.contains(option))

        holder.itemView.setOnClickListener {
            // 클릭 시 선택 토글
            if (selectedOptions.contains(option)) {
                selectedOptions.remove(option)
            } else {
                selectedOptions.add(option)
            }
            // 선택 상태 갱신
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = optionList.size

    inner class ViewHolder(private val binding: ItemFilterOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: FilterOption, isSelected: Boolean) {
            binding.option = option
            // 선택 여부에 따라 배경색/텍스트색 등 바꾸는 식으로 UI 업데이트
            if (isSelected) {
                // 예: 배경 강조
                binding.root.setBackgroundResource(R.drawable.shape_filter_selected)
            } else {
                binding.root.setBackgroundResource(R.drawable.shape_filter_normal)
            }
        }
    }
}
