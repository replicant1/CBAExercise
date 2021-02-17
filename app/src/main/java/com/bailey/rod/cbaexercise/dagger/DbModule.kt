package com.bailey.rod.cbaexercise.dagger

import android.app.Application
import androidx.room.Room
import com.bailey.rod.cbaexercise.db.AppDatabase
import com.bailey.rod.cbaexercise.db.DbAccountActivitySummaryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        val result = Room.databaseBuilder(app, AppDatabase::class.java, "cba.db")
            .allowMainThreadQueries()
            .build()
        result.clearAllTables()
        return result
    }

    @Singleton
    @Provides
    fun provideSummary(db:AppDatabase) : DbAccountActivitySummaryDao {
        return db.dbAccountActivitySummaryDao()
    }
}