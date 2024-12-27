package com.example.madcamp1stweek

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val imageUrls = listOf(
            "https://via.placeholder.com/150",
            "https://via.placeholder.com/200",
            "https://via.placeholder.com/250"
        )
        val descriptions = listOf(
            "사진 1 설명",
            "사진 2 설명",
            "사진 3 설명"
        )

        recyclerView.layoutManager = GridLayoutManager(context, 3,RecyclerView.VERTICAL, false) // 3열 그리드 레이아웃
        recyclerView.adapter = GalleryAdapter(imageUrls, descriptions) { photoUrl, description ->
            PhotoDialogFragment.newInstance(photoUrl, description).show(
                parentFragmentManager,
                "PhotoDialog"
            )
        }
        recyclerView.smoothScrollToPosition(10)
        return view
    }
}
