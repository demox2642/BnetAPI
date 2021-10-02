package com.skilbox.bnetapi.network

import com.skilbox.bnetapi.NoteResponse
import com.skilbox.bnetapi.SessionResponse
import retrofit2.Call
import retrofit2.http.*

interface BnetAPI {

    @POST(".")
    @FormUrlEncoded
    fun getNewSession(@Field("a") command: String?): Call<SessionResponse>

    @POST(".")
    @FormUrlEncoded
    fun getAllNotes(
        @Field("session") session: String?,
        @Field("a") command: String?
    ): Call<NoteResponse>

    @POST(".")
    @FormUrlEncoded
    fun addNote(
        @Field("session") session: String,
        @Field("a") command: String,
        @Field("body") text: String
    ): Call<String>?
}
