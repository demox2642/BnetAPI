package com.skilbox.bnetapi

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.skilbox.bnetapi.database.SessionContract
import java.util.*

data class Note(
    var id: String,
    val body: String,
    val da: Long,
    val dm: Long
)

data class NoteResponse(

    val status: Int,

    val data: ArrayList<ArrayList<Note>>,

    val error: String?
)

data class SessionResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val data: Session
)

@Entity(tableName = SessionContract.TABLE_NAME)
data class Session(
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = SessionContract.Colums.ID)
    val id: Long?,
    @ColumnInfo(name = SessionContract.Colums.SESSION)
    val session: String
)
