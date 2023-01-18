package com.coriolang.lighteducation.ui.topic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.coriolang.lighteducation.databinding.FragmentTopicBinding
import com.coriolang.lighteducation.ui.auth.AuthViewModel
import kotlinx.coroutines.launch

class TopicFragment : Fragment() {

    private lateinit var binding: FragmentTopicBinding

    private val args: TopicFragmentArgs by navArgs()
    private val authViewModel:
            AuthViewModel by viewModels { AuthViewModel.Factory }
    private val topicViewModel:
            TopicViewModel by viewModels { TopicViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopicBinding
            .inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        val topicId = args.topicId

        setupTopicInfo(topicId)
        setupMessagesRecyclerView(topicId)
        setupMessageTextField(topicId, authViewModel.userId)
    }

    private fun setupTopicInfo(topicId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            topicViewModel.getTopic(topicId)
            topicViewModel.topic.collect { topic ->
                binding.textTopic.text = topic.title
                binding.textTopicMessage.text = topic.text
            }
        }
    }

    private fun setupMessagesRecyclerView(topicId: String) {
        val adapter = MessageAdapter(authViewModel.userId)

        binding.recyclerViewMessages.layoutManager =
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                true
            )

        binding.recyclerViewMessages.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            topicViewModel.startListenMessages(topicId)
            topicViewModel.messages.collect { messages ->
                adapter.submitList(messages)
            }
        }
    }

    private fun setupMessageTextField(topicId: String, userId: String) {
        binding.textFieldMessage.setEndIconActivated(true)
        binding.textFieldMessage.setEndIconOnClickListener {
            val text = binding.textFieldMessage
                .editText?.text.toString()

            if (text.isNotEmpty()) {
                topicViewModel.writeMessage(topicId, userId, text)
                binding.textFieldMessage.editText?.text = null
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }
}