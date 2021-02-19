package com.bailey.rod.cbaexercise

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bailey.rod.cbaexercise.db.AppDatabase
import com.bailey.rod.cbaexercise.db.DbAccountOverview
import com.bailey.rod.cbaexercise.db.DbAccountOverviewDao
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class DbAccountOverviewDaoInstrumentedTest {

    lateinit var dao:DbAccountOverviewDao
    lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val ctx : Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
        dao = db.dbAccountOverviewDao()
    }

    @After
    fun closeDb() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun insertDao() {
        val fetchTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val newSummary = DbAccountOverview("1234", fetchTime, "{ json : 5 }")
        dao.insert(newSummary)
        Assert.assertEquals(1, dao.getAllSync().size)
    }

    @Test
    fun deleteAll() {
        val fetchTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val newSummary = DbAccountOverview("2345", fetchTime, "{ bob : \"hi\" }")
        dao.insert(newSummary)
        dao.deleteAll()
        Assert.assertEquals(0, dao.getAllSync().size)
    }

    @Test
    fun getSync() {
        val fetchTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val newSummary = DbAccountOverview("1234", fetchTime, "{ alice : 23 }")
        dao.insert(newSummary)
        val loaded : DbAccountOverview? = dao.getSync()
        Assert.assertNotNull(loaded)
        Assert.assertEquals("1234", loaded?.accountNumber)
        Assert.assertEquals(fetchTime, loaded?.fetchTime)
        Assert.assertEquals("{ alice : 23 }", loaded?.overviewJson)
    }
}