package com.example.madcamp1stweek

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // 이미지와 설명 설정
        val photoUrl = arguments?.getString("photoUrl")
        val description = arguments?.getString("description")

        photoUrl?.let {
            Glide.with(this).load(it).into(binding.photoImageView)
        }
        binding.photoDescription.text = description

        // 닫기 버튼
        binding.closeButton.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        // 다이얼로그 크기 설정
        val dialog = dialog ?: return
        // 다이얼로그 크기 설정 (세로로 긴 직사각형)
        val width = resources.displayMetrics.widthPixels * 0.8 // 화면 너비의 80%
        val height = resources.displayMetrics.heightPixels * 0.6 // 화면 높이의 60%
        dialog.window?.setLayout(width.toInt(), height.toInt()) // 직사각형 설정
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // 배경 투명 설정 (옵션)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(photoUrl: String, description: String): PhotoDialogFragment {
            val fragment = PhotoDialogFragment()
            val args = Bundle()
            args.putString("photoUrl", photoUrl)
            args.putString("description", description)
            fragment.arguments = args
            return fragment
        }
    }
}
