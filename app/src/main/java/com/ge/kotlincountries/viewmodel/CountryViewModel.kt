package com.ge.kotlincountries.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ge.kotlincountries.model.Country
import com.ge.kotlincountries.service.CountryDatabase
import com.ge.kotlincountries.service.CountryDatabase_Impl
import kotlinx.coroutines.launch

class CountryViewModel(application: Application) : BaseViewModel(application) {

    val countryLiveData = MutableLiveData<Country>()

    fun getDataFromRoom(uuid: Int) {

        launch{
            val dao = CountryDatabase(getApplication()).countryDao()
            val country = dao.getCountry(uuid)
            //val country = Country("Turkey", "Ankara", "Asia", "TRY", "Turkish", "wwww.flag")
            countryLiveData.value = country
        }

    }
}