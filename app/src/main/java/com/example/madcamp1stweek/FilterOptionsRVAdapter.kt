package com.example.madcamp1stweek

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp1stweek.databinding.ItemFilterOptionBinding

class FilterOptionsRVAdapter(
    private val selectionMode: SelectionMode,
    private val questionIndex: Int // ← "몇 번째 질문"인지를 Adapter에서 알고 있어야 함
) : RecyclerView.Adapter<FilterOptionsRVAdapter.ViewHolder>() {

    // 외부로 알려줄 콜백
    var onFilterSelectionListener: OnFilterSelectionListener? = null

    interface OnFilterSelectionListener {
        /**
         * questionIndex : 몇 번째 질문에서 선택/변경이 발생했는지
         * hasSelection  : 현재 선택된 항목이 한 개 이상인지 (true/false)
         */
        fun onFilterSelected(questionIndex: Int, hasSelection: Boolean)
    }

    private var optionList = listOf<FilterOption>()
    private val selectedOptions = mutableSetOf<FilterOption>() // 선택 상태 저장

    fun getSelectedOptions(): Set<FilterOption> = selectedOptions

    @SuppressLint("NotifyDataSetChanged")
    fun addOption(optionList: List<FilterOption>) {
        this.optionList = optionList
        selectedOptions.clear()
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
            if (selectionMode == SelectionMode.SINGLE) {
                if (selectedOptions.contains(option)) {
                    selectedOptions.remove(option)
                } else {
                    selectedOptions.clear()
                    selectedOptions.add(option)
                }
                notifyDataSetChanged()
            } else {
                // MULTIPLE 모드
                if (selectedOptions.contains(option)) {
                    selectedOptions.remove(option)
                } else {
                    selectedOptions.add(option)
                }
                notifyItemChanged(position)
            }
            // 여기서 콜백을 호출
            onFilterSelectionListener?.onFilterSelected(questionIndex, selectedOptions.isNotEmpty())
        }
    }

    override fun getItemCount(): Int = optionList.size

    inner class ViewHolder(private val binding: ItemFilterOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: FilterOption, isSelected: Boolean) {
            binding.option = option
            if (isSelected) {
                binding.root.setBackgroundResource(R.drawable.shape_filter_selected)
                binding.optionText.setTextColor(Color.WHITE)
            } else {
                binding.root.setBackgroundResource(R.drawable.shape_filter_normal)
                binding.optionText.setTextColor(Color.BLACK)
            }
            binding.executePendingBindings()
        }
    }
}

