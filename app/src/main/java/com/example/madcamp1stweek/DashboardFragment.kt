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
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYF_qQIKcZt7QGA4IwBaFMglq3GmIACHwCgg&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYF_qQIKcZt7QGA4IwBaFMglq3GmIACHwCgg&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYF_qQIKcZt7QGA4IwBaFMglq3GmIACHwCgg&s"
        )
        val descriptions = listOf(
            "사장님이 친절하고 머리가 맛있어요",
            "사장님이 친절하고 머리가 맛있어요",
            "사장님이 친절하고 머리가 맛있어요"
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
