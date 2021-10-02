package com.skilbox.bnetapi.viewmodel

import android.util.Log
import com.skilbox.bnetapi.Note
import com.skilbox.bnetapi.NoteResponse
import com.skilbox.bnetapi.Session
import com.skilbox.bnetapi.SessionResponse
import com.skilbox.bnetapi.database.Database
import com.skilbox.bnetapi.network.NetworkRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoteRepository {

    private val sessionDao = Database.instance.sessionDao()

    suspend fun getSessionFromDB(result: (Session) -> Unit) {
        result(sessionDao.getSession())
    }

    suspend fun addSessionIdInDB(session: Session) {
        sessionDao.insertSession(session)
    }

    fun getSession(result: (SessionResponse) -> Unit) {
        return NetworkRetrofit.api.getNewSession("new_session").enqueue(
            object : Callback<SessionResponse> {
                override fun onResponse(
                    call: Call<SessionResponse>,
                    response: Response<SessionResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.e("getSession onResponse", "ERROR${response.body()}")
                        result(

                            SessionResponse(
                                response.body()!!.status,

                                Session(id = null, session = response.body()!!.data.session)
                            )
                        )
                    } else {
                        Log.e("getSession isSuccessful", "ERROR${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<SessionResponse>, t: Throwable) {
                    Log.e("getSession onFailure", "ERROR$t")
                }
            }
        )
    }

    fun getAllNotes(session: String, result: (List<Note>) -> Unit) {
        var notes = mutableListOf<Note>()

        return NetworkRetrofit.api.getAllNotes(session, "get_entries").enqueue(
            object : Callback<NoteResponse> {
                override fun onResponse(
                    call: Call<NoteResponse>,
                    response: Response<NoteResponse>
                ) {
                    Log.e("getSession isSuccessful", "response=${response.body()?.data?.get(0)}")
                    if (response.isSuccessful) {
                        notes = response.body()!!.data.get(0)
                        result(notes)
                    } else {
                        Log.e("getSession isSuccessful", "ERROR${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                    Log.e("getSession onFailure", "ERROR$t")
                }
            }
        )
    }

    fun addNote(session: String, note: String) {
        return NetworkRetrofit.api.addNote(session, "add_entry", note)!!.enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("getSession isSuccessful", "response${response.body()}")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("getSession onFailure", "ERROR$t")
                }
            }
        )
    }
}
