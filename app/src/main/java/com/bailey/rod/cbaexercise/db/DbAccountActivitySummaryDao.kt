package com.bailey.rod.cbaexercise.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DbAccountActivitySummaryDao {

    @Query("SELECT * FROM AccountActivitySummaryTable")
    fun getAll() : LiveData<List<DbAccountActivitySummary>>

    @Query("SELECT * FROM AccountActivitySummaryTable LIMIT 1")
    fun get() : LiveData<DbAccountActivitySummary>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(insertMe: DbAccountActivitySummary)

    @Query("DELETE From AccountActivitySummaryTable")
    fun deleteAll()

    @Query("SELECT * FROM AccountActivitySummaryTable")
    fun getAllSync(): List<DbAccountActivitySummary>

    @Query("SELECT * FROM AccountActivitySummaryTable LIMIT 1")
    fun getSync(): DbAccountActivitySummary?

}