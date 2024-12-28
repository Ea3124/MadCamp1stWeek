package com.example.madcamp1stweek

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AddDescriptionDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val imageUrl = arguments?.getString(ARG_IMAGE_URL)

        // LayoutInflater를 사용해 커스텀 뷰 생성
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.add_description_dialog, null)

        val input = view.findViewById<EditText>(R.id.editDescription)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)

        return AlertDialog.Builder(requireContext())
            .setTitle("사진 설명 추가")
            .setView(view)
            .setPositiveButton("추가") { _, _ ->
                val description = input.text.toString()
                val rating = ratingBar.rating
                if (targetFragment is DashboardFragment && imageUrl != null) {
                    (targetFragment as DashboardFragment).addImageWithDescription(imageUrl, description, rating)
                }
            }
            .setNegativeButton("취소", null)
            .create()
    }

    companion object {
        private const val ARG_IMAGE_URL = "imageUrl"

        fun newInstance(imageUrl: String): AddDescriptionDialogFragment {
            val fragment = AddDescriptionDialogFragment()
            val args = Bundle()
            args.putString(ARG_IMAGE_URL, imageUrl)
            fragment.arguments = args
            return fragment
        }
    }
}
