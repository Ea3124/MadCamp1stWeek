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
import com.example.madcamp1stweek.models.GalleryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class DashboardFragment : Fragment() {

    private lateinit var adapter: GalleryAdapter
    private val imageUrls = mutableListOf<String>()
    private val descriptions = mutableListOf<String>()
    private val ratings = mutableListOf<Float>()  // 별점 리스트 추가

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)

        if (imageUrls.isEmpty()) { // 데이터가 비어 있을 때만 로드
            val galleryData = loadGalleryData()
            imageUrls.addAll(galleryData.map { it.imageUrl })
            descriptions.addAll(galleryData.map { it.description })
            ratings.addAll(galleryData.map { it.rating })
        }

        adapter = GalleryAdapter(imageUrls, descriptions, ratings) { photoUrl, description, rating ->
            PhotoDialogFragment.newInstance(photoUrl, description).show(
                parentFragmentManager,
                "PhotoDialog"
            )
        }
        recyclerView.adapter = adapter

        val addPhotoButton = view.findViewById<Button>(R.id.addPhotoButton)
        addPhotoButton.setOnClickListener {
            openGallery()
        }

        return view
    }


    private fun loadGalleryData(): List<GalleryItem> {
        val inputStream = resources.openRawResource(R.raw.gallery_data)
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<GalleryItem>>() {}.type
        return Gson().fromJson(reader, type)
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

    fun addImageWithDescription(imageUrl: String, description: String, rating: Float) {
        imageUrls.add(0, imageUrl)
        descriptions.add(0, description)
        ratings.add(0, rating)  // 별점 추가
        adapter.notifyItemInserted(0)
        view?.findViewById<RecyclerView>(R.id.recyclerView)?.smoothScrollToPosition(0)
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
        private const val REQUEST_CODE_ADD_DESCRIPTION = 1002
    }
}
