package com.coriolang.lighteducation.ui

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.coriolang.lighteducation.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DirectionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object: MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.direction_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_bookmark -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val view = inflater.inflate(R.layout.fragment_direction, container, false)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_create_topic)

        fab.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(ContextCompat.getDrawable(
                    requireContext(), R.drawable.ic_round_chat
                ))
                .setTitle("New topic")
                .setView(R.layout.dialog_topic)
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton("Create") { dialog, which ->
                    // Respond to positive button press
                }
                .show()
        }

        return view
    }
}