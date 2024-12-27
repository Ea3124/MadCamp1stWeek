package com.example.madcamp1stweek

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AddDescriptionDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val imageUrl = arguments?.getString(ARG_IMAGE_URL)

        val input = EditText(requireContext())
        input.hint = "사진 설명을 입력하세요"

        return AlertDialog.Builder(requireContext())
            .setTitle("사진 설명 추가")
            .setView(input)
            .setPositiveButton("추가") { _, _ ->
                val description = input.text.toString()
                if (targetFragment is DashboardFragment && imageUrl != null) {
                    (targetFragment as DashboardFragment).addImageWithDescription(imageUrl, description)
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
