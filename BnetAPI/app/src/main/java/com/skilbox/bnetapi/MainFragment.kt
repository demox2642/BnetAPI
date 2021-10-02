package com.skilbox.bnetapi

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.skilbox.bnetapi.adapter.NoteAdapter
import com.skilbox.bnetapi.databinding.FragmentMainBinding
import com.skilbox.bnetapi.plugins.NetworkStatus.ANY
import com.skilbox.bnetapi.plugins.NetworkStatus.WIFI
import com.skilbox.bnetapi.plugins.NetworkStatus.mobileConnected
import com.skilbox.bnetapi.plugins.NetworkStatus.sPref
import com.skilbox.bnetapi.plugins.NetworkStatus.wifiConnected
import com.skilbox.bnetapi.plugins.ViewBindingFragment
import com.skilbox.bnetapi.viewmodel.NoteViewModel

class MainFragment : ViewBindingFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: NoteViewModel by viewModels()
    private var noteAdapter: NoteAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkNetwork()
        observViewModelState()
        initList()
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addNoteFragment)
        }
    }

    private fun initList() {
        noteAdapter = NoteAdapter { noteId -> noteDetailInfo(noteId) }

        with(binding.noteRecycleView) {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observViewModelState() {
        viewModel.notestLiveData
            .observe(viewLifecycleOwner) { noteList ->
                Log.e("observViewModelState", "$noteList")
                noteAdapter?.items = noteList
            }

        viewModel.loading.observe(viewLifecycleOwner) { loading -> loadData(loading) }
    }

    private fun loadData(loading: Boolean) {
        binding.noteRecycleView.isVisible = loading.not()
        binding.addNote.isVisible = loading.not()
        binding.progressBar.isVisible = loading
    }

    override fun onStart() {
        super.onStart()
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sPref = sharedPrefs.getString("listPref", "Wi-Fi")

        updateConnectedFlags()
    }

    private fun updateConnectedFlags() {
        val connMgr = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeInfo: NetworkInfo? = connMgr.activeNetworkInfo
        if (activeInfo?.isConnected == true) {
            wifiConnected = activeInfo.type == ConnectivityManager.TYPE_WIFI
            mobileConnected = activeInfo.type == ConnectivityManager.TYPE_MOBILE
        } else {
            wifiConnected = false
            mobileConnected = false
        }
    }

    private fun checkNetwork() {
        updateConnectedFlags()
        if (sPref == ANY && (wifiConnected || mobileConnected) || sPref == WIFI && wifiConnected) {
            Log.e("If", "true")
            viewModel.getSession()
            Snackbar.make(requireView(), "Список обновлён", Snackbar.LENGTH_LONG)
                .show()
        } else {
            Log.e("If", "false")
            Snackbar.make(requireView(), "Соединение с сервером отсутствует, показаны сохранённые объекты", Snackbar.LENGTH_INDEFINITE)
                .setAction("Повторить") {
                    checkNetwork()
                }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        checkNetwork()
    }

    private fun noteDetailInfo(id: String) {
        val args = MainFragmentDirections.actionMainFragmentToDetailInfoFragment(id)
        findNavController().navigate(args)
    }
}
