package com.ge.kotlincountries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ge.kotlincountries.model.Country
import com.ge.kotlincountries.service.CountryAPIService
import com.ge.kotlincountries.service.CountryDatabase
import com.ge.kotlincountries.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch


class FeedViewModel(application: Application): BaseViewModel(application) {

    private val countryApiService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L


    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()




    fun refreshData(){

        val updateTime = customPreferences.getTime()


        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            getDataFromSQLite()
        }else{
            getDataFromAPI()
        }


        //loadDummyData()
    }


    fun refreshFromAPI(){
        getDataFromAPI()
    }

    private fun getDataFromSQLite() {

        countryLoading.value = true

        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(), "Countries from SQLite", Toast.LENGTH_LONG).show()
        }
    }


    private fun getDataFromAPI(){

        countryLoading.value = true

        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {

                        storeInSQLite(t)
                        Toast.makeText(getApplication(), "Countries from API", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {

                        countryError.value = true
                        countryLoading.value = false

                        e.printStackTrace()
                    }

                })
        )
    }

    private fun storeInSQLite(list: List<Country>) {

        launch {

            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray())// ->list -> individual

            var i = 0

            while(i< list.size){
                list[i].uuid = listLong[i].toInt()
                i = i + 1
            }

            showCountries(list)
        }


        customPreferences.saveTime(System.nanoTime())
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

    private fun showCountries(countryList: List<Country>){

        countries.value = countryList
        countryError.value = false
        countryLoading.value = false

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}