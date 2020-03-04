package com.ge.kotlincountries.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ge.kotlincountries.model.Country

@Dao
interface CountryDao {

    //Data Access Object

    @Insert
    suspend fun  insertAll(vararg countries: Country): List<Long>

    //Insert -> INSERT INTO
    //suspend -> coroutine, pause & resume
    //vararg -> multiple country object i teker teker insert ediyor
    //List<Long> -> primary keys return ediyor


    @Query("SELECT * FROM country")
    suspend fun getAllCountries() : List<Country>

    @Query("SELECT * FROM country WHERE uuid = :countryId")
    suspend fun getCountry(countryId : Int) : Country

    @Query("DELETE FROM country")
    suspend fun deleteAllCountries()
}