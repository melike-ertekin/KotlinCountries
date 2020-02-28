package com.ge.kotlincountries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ge.kotlincountries.model.Country
import com.ge.kotlincountries.service.CountryAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class FeedViewModel: ViewModel() {

    private val countryApiService = CountryAPIService()
    private val disposable = CompositeDisposable()

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()




    fun refreshData(){

        getDataFromAPI()

        //loadDummyData()
    }


    private fun getDataFromAPI(){

        countryLoading.value = true

        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {

                        countries.value = t
                        countryError.value = false
                        countryLoading.value = false
                    }

                    override fun onError(e: Throwable) {

                        countryError.value = true
                        countryLoading.value = false

                        e.printStackTrace()
                    }

                })
        )
    }



    private fun loadDummyData(){

        val country = Country("Turkey", "Ankara" , "Asia", "TRY", "Turkish", "wwww.flag")
        val country2 = Country("France", "Paris" , "Europe", "EUR", "French", "wwww.flag")
        val country3 = Country("Germany", "Berlin" , "Europe", "EUR", "German", "wwww.flag")

        val countryList = arrayListOf<Country>(country, country2, country3)

        countries.value = countryList
        countryError.value = false
        countryLoading.value = false

    }
}