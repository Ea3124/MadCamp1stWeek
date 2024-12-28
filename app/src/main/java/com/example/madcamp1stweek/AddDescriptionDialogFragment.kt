package com.example.madcamp1stweek

import android.app.Dialog
import android.os.Bundle
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

        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.add_description_dialog, null)

        val input = view.findViewById<EditText>(R.id.editDescription)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val spinner = view.findViewById<Spinner>(R.id.hairshopSpinner)

        val hairshopNames = loadHairshopData().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hairshopNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        return AlertDialog.Builder(requireContext())
            .setTitle("리뷰 쓰기")
            .setView(view)
            .setPositiveButton("추가") { _, _ ->
                val description = input.text.toString()
                val rating = ratingBar.rating
                val selectedHairshop = spinner.selectedItem.toString()
                if (targetFragment is DashboardFragment && imageUrl != null) {
                    (targetFragment as DashboardFragment).addImageWithDescription(imageUrl, description, rating, selectedHairshop)
                }
            }
            .setNegativeButton("취소", null)
            .create()
    }

    private fun loadHairshopData(): List<HairShop> {
        val inputStream = requireContext().resources.openRawResource(R.raw.hairshop_data)
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<HairShop>>() {}.type
        return Gson().fromJson(reader, type)
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
