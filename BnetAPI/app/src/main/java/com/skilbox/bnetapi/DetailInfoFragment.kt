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
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.skilbox.bnetapi.databinding.FragmentDetailInfoBinding
import com.skilbox.bnetapi.plugins.NetworkStatus
import com.skilbox.bnetapi.plugins.ViewBindingFragment
import com.skilbox.bnetapi.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailInfoFragment : ViewBindingFragment<FragmentDetailInfoBinding>(FragmentDetailInfoBinding::inflate) {
    private val viewModel: NoteViewModel by viewModels()
    private val args: DetailInfoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSession()
        checkNetwork()
        observViewModelState()
        val list = viewModel.notestLiveData.value
        Log.e("DetailInfoFragment", " onViewCreated list=$list")
    }

    private fun getDateTime(s: Long): String {
        try {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private fun observViewModelState() {
        viewModel.notestLiveData
            .observe(viewLifecycleOwner) { noteList ->
                updateView(noteList)
            }

        viewModel.loading.observe(viewLifecycleOwner) { loading -> loadData(loading) }
    }

    private fun loadData(loading: Boolean) {
        binding.detailDa.isVisible = loading.not()
        binding.detailDm.isVisible = loading.not()
        binding.detailNote.isVisible = loading.not()
        binding.progressBar3.isVisible = loading
    }

    fun updateView(noteList: List<Note>) {
        val note_detail = noteList.find { it.id == args.noteID }
        binding.detailDa.text = "DA: ${getDateTime(note_detail!!.da)}"
        binding.detailDm.text = "DM: ${getDateTime(note_detail.dm)}"
        binding.detailNote.text = "NOTE: ${note_detail.body}"
    }

    private fun checkNetwork() {
        updateConnectedFlags()
        if (NetworkStatus.sPref == NetworkStatus.ANY && (NetworkStatus.wifiConnected || NetworkStatus.mobileConnected) || NetworkStatus.sPref == NetworkStatus.WIFI && NetworkStatus.wifiConnected) {
            Log.e("If", "true")
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

    private fun updateConnectedFlags() {
        val connMgr = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeInfo: NetworkInfo? = connMgr.activeNetworkInfo
        if (activeInfo?.isConnected == true) {
            NetworkStatus.wifiConnected = activeInfo.type == ConnectivityManager.TYPE_WIFI
            NetworkStatus.mobileConnected = activeInfo.type == ConnectivityManager.TYPE_MOBILE
        } else {
            NetworkStatus.wifiConnected = false
            NetworkStatus.mobileConnected = false
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        NetworkStatus.sPref = sharedPrefs.getString("listPref", "Wi-Fi")

        updateConnectedFlags()
    }

    override fun onResume() {
        super.onResume()
        checkNetwork()
    }
}
