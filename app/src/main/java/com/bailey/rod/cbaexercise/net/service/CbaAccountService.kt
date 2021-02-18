package com.bailey.rod.cbaexercise.net.service

import androidx.lifecycle.LiveData
import com.bailey.rod.cbaexercise.data.XAccountOverview
import com.bailey.rod.cbaexercise.net.google.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Restful API from which app data is retrieved
 */
interface CbaAccountService {

    /**
     * Assume "/s" is fixed
     * @param accountKey The account whose data is retrieved
     * @param fileName The data set to be retrieved
     * @param dl Download - always 1
     */
    @GET("/s/{account_key}/{file_name}")
    fun getAccountOverview(
        @Path("account_key") accountKey: String,
        @Path("file_name") fileName: String,
        @Query("dl") dl: Int
    ): LiveData<ApiResponse<XAccountOverview>>

}