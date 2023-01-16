package com.coriolang.lighteducation.ui.direction

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.coriolang.lighteducation.databinding.FragmentDirectionBinding
import com.coriolang.lighteducation.model.data.toFormat
import com.coriolang.lighteducation.model.data.toLevel
import com.coriolang.lighteducation.model.data.toString
import com.coriolang.lighteducation.ui.auth.AuthViewModel
import com.coriolang.lighteducation.ui.home.TopicAdapter
import kotlinx.coroutines.launch
import java.text.NumberFormat

class DirectionFragment : Fragment() {

    private lateinit var binding: FragmentDirectionBinding

    private val args: DirectionFragmentArgs by navArgs()
    private val authViewModel:
            AuthViewModel by viewModels { AuthViewModel.Factory }
    private val directionViewModel:
            DirectionViewModel by viewModels { DirectionViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDirectionBinding
            .inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        val directionId = args.directionId

        setupDirectionInfo(directionId)
        setupInstitutionInfo()
        setupTopicsRecyclerView(directionId)
        setupFabButton(directionId)
    }

    private fun setupDirectionInfo(directionId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            directionViewModel.getDirection(directionId)
            directionViewModel.direction.collect { direction ->

                binding.textDirectionName.text = direction.name
                binding.textCode.text = direction.code
                binding.textLevel.text = direction.level
                    ?.toLevel()?.toString(requireContext())
                binding.textFormat.text = direction.format
                    ?.toFormat()?.toString(requireContext())
                binding.textMoney.text = NumberFormat
                    .getCurrencyInstance().format(direction.money ?: 0L)
                binding.textScore.text = direction.score.toString()

                directionViewModel.getInstitution(
                    direction.institutionId.toString()
                )
            }
        }
    }

    private fun setupInstitutionInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            directionViewModel.institution.collect { institution ->
                binding.textInstitutionName.text = institution.name
                binding.textAddress.text = institution.address
                binding.textUrl.text = institution.url

                // click listener for url textView
            }
        }
    }

    private fun setupTopicsRecyclerView(directionId: String) {
        val adapter = TopicAdapter { topicId ->
            val action = DirectionFragmentDirections
                .actionDirectionFragmentToTopicFragment(topicId)
            findNavController().navigate(action)
        }

        binding.recyclerViewTopics
            .layoutManager = LinearLayoutManager(context)
        binding.recyclerViewTopics.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            observeTopics(adapter, directionId)
        }
    }

    private suspend fun observeTopics(adapter: TopicAdapter, directionId: String) {
        directionViewModel.startListenTopics(directionId)
        directionViewModel.topics.collect { topics ->
            adapter.submitList(topics)
        }
    }

    private fun setupFabButton(directionId: String) {
        binding.fabCreateTopic.setOnClickListener {
            val onCreateListener = { title: String, text: String ->

                directionViewModel.writeTopic(
                    directionId,
                    authViewModel.userId,
                    title,
                    text
                )

                navigateToTopic(directionViewModel.topicId.value)
            }

            TopicDialog(onCreateListener).show(
                parentFragmentManager,
                TopicDialog.TAG
            )
        }
    }

    private fun navigateToTopic(topicId: String) {
        val action = DirectionFragmentDirections
            .actionDirectionFragmentToTopicFragment(topicId)
        findNavController().navigate(action)
    }
}