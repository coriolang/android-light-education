package com.coriolang.lighteducation.ui.search

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.coriolang.lighteducation.R
import com.coriolang.lighteducation.databinding.FiltersBottomSheetContentBinding
import com.coriolang.lighteducation.model.data.Format
import com.coriolang.lighteducation.model.data.Level
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class FiltersBottomSheet(
    private val searchViewModel: SearchViewModel
) : BottomSheetDialogFragment() {

    private lateinit var binding: FiltersBottomSheetContentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FiltersBottomSheetContentBinding
            .inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        setupOffFiltersButton()
        setupSubjectsMenu()
        setupLevelChips()
        setupMoneyTextField()
        setupFormatChips()
        setupScoreTextField()

        setupSelectedFields()
    }

    private fun setupOffFiltersButton() {
        binding.buttonOffFilters.setOnClickListener {
            searchViewModel.subject = null
            searchViewModel.level = null
            searchViewModel.money = null
            searchViewModel.format = null
            searchViewModel.score = null

            setupSelectedFields()
        }
    }

    private fun setupSubjectsMenu() {
        val subjectMenu = (binding.menuSubject.editText
                as? AutoCompleteTextView)

        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.getSubjects()
            searchViewModel.subjects.collect { subjects ->
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.list_item,
                    subjects
                )

                subjectMenu?.setAdapter(adapter)
            }
        }

        subjectMenu?.setOnItemClickListener { _, _, position, _ ->
            searchViewModel.subject = searchViewModel
                .subjects.value[position]
        }
    }

    private fun setupLevelChips() {
        binding.chipLevelMiddle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchViewModel.level = Level.MIDDLE
            }
        }

        binding.chipLevelUndergraduate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchViewModel.level = Level.UNDERGRADUATE
            }
        }

        binding.chipLevelMagistracy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchViewModel.level = Level.MAGISTRACY
            }
        }

        binding.chipLevelGraduate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchViewModel.level = Level.GRADUATE_SCHOOL
            }
        }
    }

    private fun setupMoneyTextField() {
        binding.textFieldMoney.editText?.doOnTextChanged { text, _, _, _ ->
            searchViewModel.money = text.toString().toLongOrNull()
        }
    }

    private fun setupFormatChips() {
        binding.chipFormatFull.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchViewModel.format = Format.FULL_TIME
            }
        }

        binding.chipFormatPart.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchViewModel.format = Format.PART_TIME
            }
        }

        binding.chipFormatEvening.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchViewModel.format = Format.EVENING
            }
        }

        binding.chipFormatRemote.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchViewModel.format = Format.REMOTE
            }
        }

        binding.chipFormatAccelerated.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchViewModel.format = Format.ACCELERATED
            }
        }
    }

    private fun setupScoreTextField() {
        binding.textFieldScore.editText?.doOnTextChanged { text, _, _, _ ->
            searchViewModel.score = text.toString().toLongOrNull()
        }
    }

    private fun setupSelectedFields() {
        if (searchViewModel.subject != null) {
            val subjectMenu = (binding.menuSubject.editText
                    as? AutoCompleteTextView)

            subjectMenu?.listSelection = searchViewModel
                .subjects.value.indexOf(searchViewModel.subject)
        }

        when (searchViewModel.level) {
            Level.MIDDLE -> binding
                .chipLevelMiddle.isChecked = true
            Level.UNDERGRADUATE -> binding
                .chipLevelUndergraduate.isChecked = true
            Level.MAGISTRACY -> binding
                .chipLevelMagistracy.isChecked = true
            Level.GRADUATE_SCHOOL -> binding
                .chipLevelGraduate.isChecked = true
            else -> {
                binding.chipLevelMiddle.isChecked = false
                binding.chipLevelUndergraduate.isChecked = false
                binding.chipLevelMagistracy.isChecked = false
                binding.chipLevelGraduate.isChecked = false
            }
        }

        if (searchViewModel.money != null) {
            binding.textFieldMoney.editText
                ?.setText(searchViewModel.money.toString())
        } else {
            binding.textFieldMoney.editText?.setText(null)
        }

        when (searchViewModel.format) {
            Format.FULL_TIME -> binding
                .chipFormatFull.isChecked = true
            Format.PART_TIME -> binding
                .chipFormatPart.isChecked = true
            Format.EVENING -> binding
                .chipFormatEvening.isChecked = true
            Format.REMOTE -> binding
                .chipFormatRemote.isChecked = true
            Format.ACCELERATED -> binding
                .chipFormatAccelerated.isChecked = true
            else -> {
                binding.chipFormatFull.isChecked = false
                binding.chipFormatPart.isChecked = false
                binding.chipFormatEvening.isChecked = false
                binding.chipFormatRemote.isChecked = false
                binding.chipFormatAccelerated.isChecked = false
            }
        }

        if (searchViewModel.score != null) {
            binding.textFieldScore.editText
                ?.setText(searchViewModel.score.toString())
        } else {
            binding.textFieldScore.editText?.setText(null)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        searchViewModel.getDirections()
    }

    companion object {
        const val TAG = "FilterSBottomSheet"
    }
}