package com.coriolang.lighteducation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coriolang.lighteducation.R
import com.google.android.material.textfield.TextInputLayout

class TopicFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_topic, container, false)

        val messageField = view.findViewById<TextInputLayout>(R.id.text_field_message)

        messageField.setEndIconActivated(true)
        messageField.setEndIconOnClickListener {
            // send message
        }

        return view
    }
}