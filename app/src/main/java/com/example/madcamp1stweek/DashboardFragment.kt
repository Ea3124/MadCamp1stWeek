package com.example.madcamp1stweek

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
//import com.example.madcamp1stweek.models.GalleryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class DashboardFragment : Fragment() {

    private lateinit var adapter: GalleryAdapter
    private val galleryItems = mutableListOf<GalleryItem>()
    private lateinit var hairshopList: List<HairShop>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        // --- ① StaggeredGridLayoutManager (2열)로 변경 ---
        val staggeredLayoutManager = StaggeredGridLayoutManager(
            2, // 2열
            StaggeredGridLayoutManager.VERTICAL
        )
        // gapStrategy 설정 (아이템 간 간격 조정)
        staggeredLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        recyclerView.layoutManager = staggeredLayoutManager

        // hairshopList, galleryItems 기존 로딩 로직 그대로
        hairshopList = loadHairshopData()
        if (galleryItems.isEmpty()) {
            galleryItems.addAll(loadGalleryData())
        }

        // dashboardItems (Header + GalleryCard)로 구성하는 부분도 동일
        val dashboardItems = mutableListOf<DashboardItem>()
        dashboardItems.add(DashboardItem.Header) // 헤더 추가
        galleryItems.forEach { gItem ->
            dashboardItems.add(DashboardItem.GalleryCard(gItem))
        }

        // 어댑터도 기존에 만들어둔 GalleryAdapter(멀티 뷰타입) 사용
        adapter = GalleryAdapter(
            dashboardItems,
            onItemClick = { photoUrl, description, rating, hairshopName, index ->
                PhotoDialogFragment.newInstance(photoUrl, description, hairshopName, rating, index).apply {
                    setTargetFragment(this@DashboardFragment, 0)
                }.show(parentFragmentManager, "PhotoDialog")
            }
        )
        recyclerView.adapter = adapter

        // + 버튼(사진 추가) 핸들러
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

    private fun loadHairshopData(): List<HairShop> {
        val inputStream = resources.openRawResource(R.raw.hairshop_data)
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<HairShop>>() {}.type
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

    fun addImageWithDescription(imageUrl: String, description: String, rating: Float, hairshopName: String) {
        val newItem = GalleryItem(
            index = galleryItems.size + 1,
            imageUrl = imageUrl,
            description = description,
            rating = rating,
            hairshopName = hairshopName
        )

        galleryItems.add(0, newItem)
        // (2) 어댑터에서 dashboardItems도 갱신
        val adapterRef = adapter as? GalleryAdapter
        adapterRef?.let { galAdapter ->
            // dashboardItems에서 position=0은 Header.
            // 새 갤러리 카드를 맨 위(헤더 아래)에 표시하려면 position=1에 삽입.
            val positionToInsert = 1

            // DashboardItem.GalleryCard 형태로 삽입
            galAdapter.dashboardItems.add(positionToInsert, DashboardItem.GalleryCard(newItem))

            // (3) notifyItemInserted로 개별 삽입 애니메이션
            galAdapter.notifyItemInserted(positionToInsert)

            // 스크롤 맨 위로 이동 (선택 사항)
            view?.findViewById<RecyclerView>(R.id.recyclerView)?.smoothScrollToPosition(positionToInsert)
        }
    }


    fun deletePhoto(indexToDelete: Int) {
        Log.d("DashboardFragment", "deletePhoto 호출됨. 삭제 대상 Index: $indexToDelete")

        // 1) 'galleryItems'에서 먼저 해당 GalleryItem을 찾는다
        val itemToDelete = galleryItems.find { it.index == indexToDelete }
        if (itemToDelete != null) {
            // galleryItems에서도 제거 (동기화)
            galleryItems.remove(itemToDelete)

            // 2) 어댑터 쪽 DashboardItem 목록에서도 제거
            //    -> GalleryAdapter 내부의 'dashboardItems' 중,
            //       이 itemToDelete를 담고 있는 DashboardItem.GalleryCard를 찾아야 함.
            val adapterRef = adapter as? GalleryAdapter
            if (adapterRef != null) {
                val position = adapterRef.dashboardItems.indexOfFirst { dashboardItem ->
                    dashboardItem is DashboardItem.GalleryCard &&
                            dashboardItem.galleryItem.index == indexToDelete
                }

                if (position != -1) {
                    // dashboardItems에서 해당 위치 제거
                    adapterRef.dashboardItems.removeAt(position)
                    // notifyItemRemoved로 삭제 애니메이션 트리거
                    adapterRef.notifyItemRemoved(position)
                } else {
                    Log.e("DashboardFragment", "어댑터 내에서 해당 item을 찾을 수 없음.")
                    // fallback: 전체 갱신
                    adapterRef.notifyDataSetChanged()
                }
            } else {
                // 어댑터 참조 불가 시 fallback
                adapter.notifyDataSetChanged()
            }

            // 이후 galleryItems의 인덱스 재정렬 (원한다면)
            galleryItems.forEachIndexed { newIndex, item ->
                item.index = newIndex + 1 // 인덱스는 1부터 시작
            }

        } else {
            Log.e("DashboardFragment", "삭제할 아이템을 찾을 수 없습니다. Index: $indexToDelete")
        }
    }

    fun updatePhoto(index: Int, newDescription: String, newRating: Float, newHairshopName: String) {
        Log.d("DashboardFragment", "updatePhoto 호출됨. 수정 대상 Index: $index")

        val itemToUpdate = galleryItems.find { it.index == index }
        if (itemToUpdate != null) {
            Log.d("DashboardFragment", "수정 대상 찾음: $itemToUpdate")

            itemToUpdate.description = newDescription
            itemToUpdate.rating = newRating
            itemToUpdate.hairshopName = newHairshopName

            adapter.notifyDataSetChanged()
            Log.d("DashboardFragment", "수정 완료: $itemToUpdate")
        } else {
            Log.e("DashboardFragment", "수정할 아이템을 찾을 수 없습니다. Index: $index")
        }
    }


    fun editPhoto(indexToEdit: Int) {
        Log.d("DashboardFragment", "editPhoto 호출됨. 수정 대상 Index: $indexToEdit")

        val itemToEdit = galleryItems.find { it.index == indexToEdit }
        if (itemToEdit != null) {
            Log.d("DashboardFragment", "수정 대상 찾음: $itemToEdit")
            Log.d("DashboardFragment", "전달할 데이터 - imageUrl: ${itemToEdit.imageUrl}, description: ${itemToEdit.description}, rating: ${itemToEdit.rating}, hairshopName: ${itemToEdit.hairshopName}")

            // 수정 모드로 대화상자 호출
            val dialog = AddDescriptionDialogFragment.newInstance(
                imageUrl = itemToEdit.imageUrl,
                isEditing = true,
                index = indexToEdit,
                description = itemToEdit.description,
                rating = itemToEdit.rating,
                hairshopName = itemToEdit.hairshopName
            ).apply {
                setTargetFragment(this@DashboardFragment, 0)
            }
            dialog.show(parentFragmentManager, "EditDescriptionDialogFragment")
        } else {
            Log.e("DashboardFragment", "수정할 아이템을 찾을 수 없습니다. Index: $indexToEdit")
        }
    }





    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
        private const val REQUEST_CODE_ADD_DESCRIPTION = 1002
    }
}
