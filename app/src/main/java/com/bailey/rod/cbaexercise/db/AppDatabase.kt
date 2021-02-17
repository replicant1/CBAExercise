package com.bailey.rod.cbaexercise.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DbAccountActivitySummary::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbAccountActivitySummaryDao() : DbAccountActivitySummaryDao
}