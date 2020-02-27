package com.ge.kotlincountries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ge.kotlincountries.model.Country

class CountryViewModel : ViewModel() {

    val countryLiveData = MutableLiveData<Country>()

    fun getDataFromRoom() {
        val country = Country("Turkey", "Ankara", "Asia", "TRY", "Turkish", "wwww.flag")
        countryLiveData.value = country
    }
}