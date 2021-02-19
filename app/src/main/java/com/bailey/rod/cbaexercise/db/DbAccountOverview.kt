package com.bailey.rod.cbaexercise.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Simple way of saving all JSON data from server to is to store the raw JSON
 * against the account number it pertains to. Ideally, ATM objects and Transaction
 * objects would be split into separate tables but the below, simplified approach is
 * adequate for demonstrating use of Room for persistence.
 */
@Entity(tableName = "AccountOverviewTable")
data class DbAccountOverview(
    // The account number whose overview appears in this.overviewJson
    @PrimaryKey val accountNumber : String,

    // Number of seconds from the epoch of 1970-01-01T00:00:00Z.
    // The time the JSON in this.summaryJson was fetched over network
    val fetchTime: Long,

    // A JSON string that deserializes into an instance of XAccountOverview
    val overviewJson : String
)
