package com.bailey.rod.cbaexercise.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DbAccountOverview::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbAccountOverviewDao() : DbAccountOverviewDao
}