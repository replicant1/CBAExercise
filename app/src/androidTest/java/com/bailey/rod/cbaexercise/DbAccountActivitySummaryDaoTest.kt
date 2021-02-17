package com.bailey.rod.cbaexercise

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bailey.rod.cbaexercise.db.AppDatabase
import com.bailey.rod.cbaexercise.db.DbAccountActivitySummary
import com.bailey.rod.cbaexercise.db.DbAccountActivitySummaryDao
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DbAccountActivitySummaryDaoTest {

    lateinit var dao:DbAccountActivitySummaryDao
    lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val ctx : Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
        dao = db.dbAccountActivitySummaryDao()
    }

    @After
    fun closeDb() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun insertDao() {
        val newSummary = DbAccountActivitySummary("1234", "json")
        dao.insert(newSummary)
        Assert.assertEquals(1, dao.getAllSync().size)
    }

    @Test
    fun deleteAll() {
        val newSummary = DbAccountActivitySummary("2345", "somejson")
        dao.insert(newSummary)
        dao.deleteAll()
        Assert.assertEquals(0, dao.getAllSync().size)
    }

    @Test
    fun getFirst() {
        val newSummary = DbAccountActivitySummary("1234", "json")
        dao.insert(newSummary)
        val loaded : DbAccountActivitySummary? = dao.getSync()
        Assert.assertNotNull(loaded)
    }
}