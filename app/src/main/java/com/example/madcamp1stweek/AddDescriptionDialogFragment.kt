package com.example.madcamp1stweek

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.io.InputStreamReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddDescriptionDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val imageUrl = arguments?.getString(ARG_IMAGE_URL)
        val isEditing = arguments?.getBoolean(ARG_IS_EDITING) ?: false
        val index = arguments?.getInt(ARG_INDEX) ?: -1

        Log.d("AddDescriptionDialog", "onCreateDialog called")
        Log.d("AddDescriptionDialog", "Arguments - imageUrl: $imageUrl, isEditing: $isEditing, index: $index")

        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.add_description_dialog, null)

        val input = view.findViewById<EditText>(R.id.editDescription)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val spinner = view.findViewById<Spinner>(R.id.hairshopSpinner)

        val hairshopNames = loadHairshopData().map { it.name }
        Log.d("AddDescriptionDialog", "Loaded hairshop names: $hairshopNames")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hairshopNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 수정 모드일 경우 기존 데이터 설정
        if (isEditing) {
            val existingDescription = arguments?.getString(ARG_DESCRIPTION)
            val existingRating = arguments?.getFloat(ARG_RATING) ?: 3.0f
            val existingHairshopName = arguments?.getString(ARG_HAIRSHOP_NAME)

            Log.d("AddDescriptionDialog", "Editing mode - existingDescription: $existingDescription, existingRating: $existingRating, existingHairshopName: $existingHairshopName")

            input.setText(existingDescription)
            ratingBar.rating = existingRating
            val selectedIndex = hairshopNames.indexOf(existingHairshopName)
            if (selectedIndex >= 0) {
                spinner.setSelection(selectedIndex)
            } else {
                Log.w("AddDescriptionDialog", "Hairshop name not found in list: $existingHairshopName")
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(if (isEditing) "리뷰 수정" else "리뷰 쓰기")
            .setView(view)
            .setPositiveButton(if (isEditing) "수정" else "추가") { _, _ ->
                val description = input.text.toString()
                val rating = ratingBar.rating
                val selectedHairshop = spinner.selectedItem.toString()

                Log.d("AddDescriptionDialog", "Positive button clicked - description: $description, rating: $rating, selectedHairshop: $selectedHairshop")

                if (isEditing) {
                    Log.d("AddDescriptionDialog", "Updating photo - index: $index")
                    (targetFragment as DashboardFragment).updatePhoto(
                        index = index,
                        newDescription = description,
                        newRating = rating,
                        newHairshopName = selectedHairshop
                    )
                } else {
                    Log.d("AddDescriptionDialog", "Adding new photo")
                    (targetFragment as DashboardFragment).addImageWithDescription(
                        imageUrl = imageUrl ?: "",
                        description = description,
                        rating = rating,
                        hairshopName = selectedHairshop
                    )
                }
            }
            .setNegativeButton("취소") { _, _ ->
                Log.d("AddDescriptionDialog", "Negative button clicked - Cancel")
            }
            .create()
    }

    private fun loadHairshopData(): List<HairShop> {
        Log.d("AddDescriptionDialog", "Loading hairshop data from resources")
        val inputStream = requireContext().resources.openRawResource(R.raw.hairshop_data)
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<HairShop>>() {}.type
        return Gson().fromJson(reader, type)
    }

    companion object {
        private const val ARG_IMAGE_URL = "imageUrl"
        private const val ARG_IS_EDITING = "isEditing"
        private const val ARG_INDEX = "index"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_RATING = "rating"
        private const val ARG_HAIRSHOP_NAME = "hairshopName"

        fun newInstance(
            imageUrl: String,
            isEditing: Boolean = false,
            index: Int? = null,
            description: String? = null,
            rating: Float? = null,
            hairshopName: String? = null
        ): AddDescriptionDialogFragment {
            val fragment = AddDescriptionDialogFragment()
            val args = Bundle()
            args.putString(ARG_IMAGE_URL, imageUrl)
            args.putBoolean(ARG_IS_EDITING, isEditing)
            if (index != null) {
                args.putInt(ARG_INDEX, index)
            }
            if (description != null) {
                args.putString(ARG_DESCRIPTION, description)
            }
            if (rating != null) {
                args.putFloat(ARG_RATING, rating)
            }
            if (hairshopName != null) {
                args.putString(ARG_HAIRSHOP_NAME, hairshopName)
            }
            fragment.arguments = args
            Log.d("AddDescriptionDialog", "newInstance called - args: $args")
            return fragment
        }
    }
}
