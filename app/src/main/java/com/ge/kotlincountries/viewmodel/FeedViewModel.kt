package com.ge.kotlincountries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ge.kotlincountries.model.Country

class FeedViewModel: ViewModel() {
    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData(){
        val country = Country("Turkey", "Ankara" , "Asia", "TRY", "Turkish", "wwww.flag")
        val country2 = Country("France", "Paris" , "Europe", "EUR", "French", "wwww.flag")
        val country3 = Country("Germany", "Berlin" , "Europe", "EUR", "German", "wwww.flag")

        val countryList = arrayListOf<Country>(country, country2, country3)

        countries.value = countryList
        countryError.value = false
        countryLoading.value = false
    }
}