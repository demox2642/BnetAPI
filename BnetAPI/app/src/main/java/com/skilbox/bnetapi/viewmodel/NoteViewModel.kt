package com.skilbox.bnetapi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.skilbox.bnetapi.Note
import com.skilbox.bnetapi.Session
import com.skilbox.bnetapi.database.Database
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {
    private val repository = NoteRepository()
    private var sessionId: String? = null

    private val notestMutableLiveData = MutableLiveData<List<Note>>()
    val notestLiveData: LiveData<List<Note>>
        get() = notestMutableLiveData

    private val loadingMutableLiveData = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = loadingMutableLiveData

    fun getSession() {
        viewModelScope.launch {
            try {
                loadingMutableLiveData.postValue(true)
                repository.getSessionFromDB() {
                    sessionId = it.session
                }
            } catch (t: Throwable) {
                Log.e("getSession from DB", "ERROR $t")
                try {
                    repository.getSession() { it ->
                        sessionId = it.data.session
                        addSessionInDB(it.data)
                    }
                } catch (t: Throwable) {
                    Log.e("getSession from DB/try", "ERROR $t")
                }
            } finally {
                updateNoteList()
                loadingMutableLiveData.postValue(false)
            }
        }
    }

    private fun addSessionInDB(session: Session) {
        viewModelScope.launch {
            Database.instance.withTransaction {
                try {
                    repository.addSessionIdInDB(session)
                } catch (t: Throwable) {
                    Log.e("insertToDataBase", "$t")
                }
            }
        }
    }

    private fun updateNoteList() {

        viewModelScope.launch {
            try {
                loadingMutableLiveData.postValue(true)
                repository.getAllNotes(
                    session = sessionId!!
                ) {
                    notestMutableLiveData.postValue(it)
                }
            } catch (t: Exception) {
                Log.e("MatchViewModel", "getTest Error:${t.message}")
            } finally {
                loadingMutableLiveData.postValue(false)
            }
        }
    }

    fun addNote(note: String) {
        viewModelScope.launch {
            try {
                Log.e("addNote", "sessionId = $sessionId , $note")
                repository.addNote(session = sessionId!!, note)
            } catch (t: Exception) {
                Log.e("addNote", " Error:${t.message}")
            }
        }
    }
}
