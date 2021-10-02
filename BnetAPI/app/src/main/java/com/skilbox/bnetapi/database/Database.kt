package com.skilbox.bnetapi.database

import android.content.Context
import androidx.room.Room

object Database {

    lateinit var instance: NotesDB

        private set

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context,
            NotesDB::class.java,
            NotesDB.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
