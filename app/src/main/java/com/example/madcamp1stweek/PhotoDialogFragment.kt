package com.example.madcamp1stweek

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

        // 전달받은 데이터 설정
        val photoUrl = arguments?.getString(ARG_PHOTO_URL)
        val description = arguments?.getString(ARG_DESCRIPTION)
        val hairshopName = arguments?.getString(ARG_HAIRSHOP_NAME)

        photoUrl?.let {
            Glide.with(this)
                .load(it)
                .into(binding.photoImageView)
        }
        binding.photoDescription.text = description
        binding.hairshopName.text = hairshopName

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
        private const val ARG_PHOTO_URL = "photoUrl"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_HAIRSHOP_NAME = "hairshopName"

        fun newInstance(photoUrl: String, description: String, hairshopName: String): PhotoDialogFragment {
            val fragment = PhotoDialogFragment()
            val args = Bundle()
            args.putString(ARG_PHOTO_URL, photoUrl)
            args.putString(ARG_DESCRIPTION, description)
            args.putString(ARG_HAIRSHOP_NAME, hairshopName)
            fragment.arguments = args
            return fragment
        }
    }
}
