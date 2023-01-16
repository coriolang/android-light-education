package com.coriolang.lighteducation.ui.direction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.coriolang.lighteducation.databinding.DialogTopicBinding

class TopicDialog(
    private val onCreateClicked: (
        title: String,
        text: String
    ) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogTopicBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTopicBinding
            .inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        setupCreateButton()
        setupCancelButton()
    }

    private fun setupCreateButton() {
        binding.buttonCreate.setOnClickListener {
            val topic = binding.textFieldTopic
                .editText?.text.toString()
            val message = binding.textFieldMessage
                .editText?.text.toString()

            onCreateClicked(topic, message)

            dismiss()
        }
    }

    private fun setupCancelButton() {
        binding.buttonDismiss.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "TopicDialog"
    }
}