package com.skilbox.bnetapi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skilbox.bnetapi.Session
import com.skilbox.bnetapi.database.NotesDB.Companion.DB_VERSION

@Database(
    entities = [
        Session::class
    ],
    version = DB_VERSION
)

abstract class NotesDB : RoomDatabase() {

    abstract fun sessionDao(): SessionDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "BnetDB"
    }
}
