package com.example.madcamp1stweek

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashboardFragment : Fragment() {

    private lateinit var adapter: GalleryAdapter
    private val imageUrls = mutableListOf(
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYF_qQIKcZt7QGA4IwBaFMglq3GmIACHwCgg&s", // 기본 이미지 URL
        "https://img2.quasarzone.com/editor/2023/02/06/2d1bd3940e248b54ef841a3b0b4453bc.jpg",
        "https://image.fmkorea.com/files/attach/new3/20230901/486616/4513918673/6136156558/0798c45a389dd8bc58302c3a932d1919.jpg",
        "https://news.nateimg.co.kr/orgImg/ts/2020/12/03/849092_576856_322.jpg",
        "https://daedamo.com/new/data/file/photo2/2040939026_vAeDR4aC_2ffb199a868919a461e1c0645845cdf91551710f.jpg",
        "https://daedamo.com/new/data/file/photo2/2041913824_dqOgyhvB_04eb872e3c2a3aae901a4ed2178ab03cef55f0e8.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDdUMad0cZhy9F6ePzSTmLcs92PcwVZYCimg&s"

    )
    private val descriptions = mutableListOf(
        "기본 사진 1",
        "기본 사진 2",
        "기본 사진 3",
        "기본 사진 4",
        "기본 사진 5",
        "기본 사진 6",
        "siuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu",
        "siuuuu"

    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)

        adapter = GalleryAdapter(imageUrls, descriptions) { photoUrl, description ->
            PhotoDialogFragment.newInstance(photoUrl, description).show(
                parentFragmentManager,
                "PhotoDialog"
            )
        }
        recyclerView.adapter = adapter

        // 추가 버튼 클릭 이벤트
        val addPhotoButton = view.findViewById<Button>(R.id.addPhotoButton)
        addPhotoButton.setOnClickListener {
            openGallery()
        }
        recyclerView.smoothScrollToPosition(10)
        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                openAddDescriptionDialog(it.toString())
            }
        }
    }

    private fun openAddDescriptionDialog(imageUrl: String) {
        val dialog = AddDescriptionDialogFragment.newInstance(imageUrl)
        dialog.setTargetFragment(this, REQUEST_CODE_ADD_DESCRIPTION)
        dialog.show(parentFragmentManager, "AddDescriptionDialogFragment")
    }

    fun addImageWithDescription(imageUrl: String, description: String) {
        adapter.addImage(imageUrl, description)
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
        private const val REQUEST_CODE_ADD_DESCRIPTION = 1002
    }
}
