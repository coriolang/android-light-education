package com.coriolang.lighteducation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.coriolang.lighteducation.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout

class FiltersBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.filters_bottom_sheet_content, container, false)

        val items = listOf("Subject 1", "Subject 2", "Subject 3", "Subject 4")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        val menuSubject = view.findViewById<TextInputLayout>(R.id.menu_subject)
        (menuSubject.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        return view
    }

    companion object {
        const val TAG = "FilterSBottomSheet"
    }


}