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

        // RecyclerView 초기화
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val imageUrls = listOf(
            "https://via.placeholder.com/150",
            "https://via.placeholder.com/200",
            "https://via.placeholder.com/250",
            "https://via.placeholder.com/300",
            "https://via.placeholder.com/350",
            "https://via.placeholder.com/400"
        )

        recyclerView.layoutManager = GridLayoutManager(context, 3) // 3열 그리드 레이아웃
        recyclerView.adapter = GalleryAdapter(imageUrls)

        return view
    }
}
