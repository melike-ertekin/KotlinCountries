package com.ge.kotlincountries


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ge.kotlincountries.util.downloadFromUrl
import com.ge.kotlincountries.util.placeholderprogressBar
import com.ge.kotlincountries.viewmodel.CountryViewModel
import kotlinx.android.synthetic.main.fragment_country.*

/**
 * A simple [Fragment] subclass.
 */
class CountryFragment : Fragment() {


    private var countryId = 0
    private lateinit var viewModel : CountryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            countryId = CountryFragmentArgs.fromBundle(it).countryID

        }


        viewModel = ViewModelProviders.of(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(countryId)



        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer {country ->
            country?.let {
                countryName.text = country.countryName
                countryCapital.text = country.countryCapital
                countryCurrency.text = country.countryCurrency
                countryLanguage.text = country.countryLanguage
                countryRegion.text = country.countryRegion

                context?.let {
                    countryImage.downloadFromUrl(country.imageUrl, placeholderprogressBar(it))
                }

            }
        })

    }
}
