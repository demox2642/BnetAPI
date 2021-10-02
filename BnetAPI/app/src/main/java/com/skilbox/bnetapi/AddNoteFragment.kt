package com.skilbox.bnetapi

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.skilbox.bnetapi.databinding.FragmentAddNoteBinding
import com.skilbox.bnetapi.plugins.ViewBindingFragment
import com.skilbox.bnetapi.viewmodel.NoteViewModel

class AddNoteFragment : ViewBindingFragment<FragmentAddNoteBinding>(FragmentAddNoteBinding::inflate) {
    private val viewModel: NoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observViewModelState()
        viewModel.getSession()
        binding.addButton.setOnClickListener {
            checkForm()
            findNavController().popBackStack()
        }
        binding.cancellButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkForm() {
        if (binding.note.text.isNullOrEmpty()) {
            Log.e("onViewCreated", "")
            Snackbar.make(requireView(), "Введите данные", Snackbar.LENGTH_LONG)
                .show()
        } else viewModel.addNote(binding.note.text.toString())
    }

    private fun observViewModelState() {
        viewModel.loading.observe(viewLifecycleOwner) { loading -> loadData(loading) }
    }

    private fun loadData(loading: Boolean) {
        binding.cancellButton.isVisible = loading.not()
        binding.addButton.isVisible = loading.not()
        binding.note.isVisible = loading.not()
        binding.progressBar2.isVisible = loading
    }
}
