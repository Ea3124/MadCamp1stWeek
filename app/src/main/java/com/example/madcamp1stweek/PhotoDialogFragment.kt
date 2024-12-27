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
