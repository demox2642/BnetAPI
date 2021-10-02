package com.skilbox.bnetapi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.skilbox.bnetapi.Session

@Dao
interface SessionDao {
    @Insert()
    suspend fun insertSession(session: Session)

    @Query("select * from ${SessionContract.TABLE_NAME}")
    suspend fun getSession(): Session
}
