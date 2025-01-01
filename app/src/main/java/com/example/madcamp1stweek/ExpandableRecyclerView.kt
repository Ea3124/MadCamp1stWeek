// ExpandableRecyclerView.kt
package com.example.madcamp1stweek

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ExpandableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val expandedSpec = View.MeasureSpec.makeMeasureSpec(
            Int.MAX_VALUE shr 2,
            View.MeasureSpec.AT_MOST
        )
        super.onMeasure(widthSpec, expandedSpec)
    }
}
