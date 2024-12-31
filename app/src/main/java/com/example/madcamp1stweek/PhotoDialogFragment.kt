package com.example.madcamp1stweek

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.madcamp1stweek.databinding.FragmentPhotoDialogBinding

class PhotoDialogFragment : DialogFragment() {

    private var _binding: FragmentPhotoDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명 설정
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) // 소프트 입력 모드 조정
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 데이터 설정
        val photoUrl = arguments?.getString(ARG_PHOTO_URL)
        val description = arguments?.getString(ARG_DESCRIPTION)
        val hairshopName = arguments?.getString(ARG_HAIRSHOP_NAME)
        val rating = arguments?.getFloat(ARG_RATING) ?: 0f

        photoUrl?.let {
            Glide.with(this)
                .load(it)
                .into(binding.photoImageView)
        }
        binding.photoDescription.text = description
        binding.hairshopName.text = hairshopName
        binding.photoRating.text = "$rating"

        // 닫기 버튼
        binding.closeButton.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }

        // 삭제 버튼
        binding.deleteButton.setOnClickListener {
            val targetFragment = targetFragment as? DashboardFragment
            val indexToDelete = arguments?.getInt(ARG_INDEX) // 삭제할 항목의 인덱스를 가져옴

            Log.d("PhotoDialogFragment", "삭제 버튼 클릭됨. Index: $indexToDelete")

            if (indexToDelete != null && targetFragment != null) {
                Log.d("PhotoDialogFragment", "삭제 요청을 DashboardFragment로 전달.")
                targetFragment.deletePhoto(indexToDelete) // DashboardFragment로 삭제 요청
                dismiss() // 다이얼로그 닫기
            } else {
                Log.e("PhotoDialogFragment", "삭제 요청 실패 - Index 또는 대상 프래그먼트가 없음.")
            }
        }

        // 수정 버튼
        binding.editButton.setOnClickListener {
            val targetFragment = targetFragment as? DashboardFragment
            val indexToEdit = arguments?.getInt(ARG_INDEX) // 수정할 항목의 인덱스

            Log.d("PhotoDialogFragment", "수정 버튼 클릭됨. Index: $indexToEdit")
            Log.d("PhotoDialogFragment", "Target Fragment: $targetFragment")

            if (indexToEdit != null && targetFragment != null) {
                targetFragment.editPhoto(indexToEdit)
                Log.d("PhotoDialogFragment", "DashboardFragment로 수정 요청")
                dismiss() // 다이얼로그 닫기
            } else {
                Log.e("PhotoDialogFragment", "수정 요청 실패 - Index 또는 대상 프래그먼트가 설정되지 않음.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PHOTO_URL = "photoUrl"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_HAIRSHOP_NAME = "hairshopName"
        private const val ARG_RATING = "rating" // 별점 추가
        private const val ARG_INDEX = "index"

        fun newInstance(photoUrl: String, description: String, hairshopName: String, rating: Float, index: Int): PhotoDialogFragment {
            val fragment = PhotoDialogFragment()
            val args = Bundle()
            args.putString(ARG_PHOTO_URL, photoUrl)
            args.putString(ARG_DESCRIPTION, description)
            args.putString(ARG_HAIRSHOP_NAME, hairshopName)
            args.putFloat(ARG_RATING, rating) // 별점 추가
            args.putInt(ARG_INDEX, index)
            fragment.arguments = args
            return fragment
        }
    }
}
