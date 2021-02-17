package com.bailey.rod.cbaexercise.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AccountActivitySummaryTable")
data class DbAccountActivitySummary(
    @PrimaryKey val accountNumber : String,
    val summaryJson : String
)
