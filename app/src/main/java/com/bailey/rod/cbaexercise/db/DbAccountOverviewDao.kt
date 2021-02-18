package com.bailey.rod.cbaexercise.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DbAccountOverviewDao {

    @Query("SELECT * FROM AccountOverviewTable")
    fun getAll() : LiveData<List<DbAccountOverview>>

    @Query("SELECT * FROM AccountOverviewTable LIMIT 1")
    fun get() : LiveData<DbAccountOverview>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(insertMe: DbAccountOverview)

    @Query("DELETE From AccountOverviewTable")
    fun deleteAll()

    @Query("SELECT * FROM AccountOverviewTable")
    fun getAllSync(): List<DbAccountOverview>

    @Query("SELECT * FROM AccountOverviewTable LIMIT 1")
    fun getSync(): DbAccountOverview?

}