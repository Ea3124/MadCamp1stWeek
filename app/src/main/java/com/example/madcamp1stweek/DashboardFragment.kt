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
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYF_qQIKcZt7QGA4IwBaFMglq3GmIACHwCgg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYF_qQIKcZt7QGA4IwBaFMglq3GmIACHwCgg&s"
    )
    private val descriptions = mutableListOf(
        "기본 사진 1",
        "기본 사진 2",
        "기본 사진 3"
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
