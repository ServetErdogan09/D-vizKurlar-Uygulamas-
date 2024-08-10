package com.example.parabirimi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.parabirimi.Model.ExchangeResponse
import com.example.parabirimi.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.reactivestreams.Subscriber
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val ratesListe = listOf("USD", "EUR", "GBP", "JPY", "AUD")

    private var job : Job? = null

    //private val ratesListe = listOf("USD", "EUR")
    private lateinit var binding: ActivityMainBinding
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        compositeDisposable = CompositeDisposable()
        CoroutineScope(Dispatchers.Main).launch {
            fetchAllExchangeRates()
        }

    }


    private suspend fun fetchAllExchangeRates() {
        ratesListe.forEach { currencyName ->
            fetchExchangeRates(currencyName)
            Log.e("currencyName", currencyName)
        }
    }


    private suspend fun fetchExchangeRates(currencyName: String) {
        // val disposable = RetrofitInstance.api.getExchangeRates(currencyName, "TRY")

        job = CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.getExchangeRates(currencyName, "TRY")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val rate = response.body()?.rates?.get("TRY")
                    Log.e("currencyName", currencyName)
                    Log.e("rate", rate.toString())
                    if (rate != null) {
                        updateUI(currencyName, rate)
                    } else {
                        Log.e("API_ERROR", "Rate is null for currency: $currencyName")
                    }
                } else {
                    Log.e("API_ERROR", "Response not successful for currency: $currencyName")
                }
            }
        }

        /*
            .subscribeOn(Schedulers.io()) //  // disposable'ı IO iş parçacığında çalıştır
            .observeOn(AndroidSchedulers.mainThread()) // ana iş parçacığında gözlemliyecek
            .subscribe({ response ->
                val rate = response.rates[currencyName]
                if (rate != null) {
                    Log.e("currencyName", currencyName)
                    Log.e("rate", rate.toString())
                    updateUI(currencyName, rate)
                }
            }, { error ->
                Log.e("API_ERROR", "Failed to fetch exchange rates", error)
            }
            )
        compositeDisposable.add(disposable)
    }

         */
    }

    override fun onDestroy() {
        super.onDestroy()
        job!!.cancel()

    }

    fun updateUI(currencyName: String, rate: Double) {
        when (currencyName) {
            "USD" -> binding.dolarDegerTextView.text = rate.toString()
            "EUR" -> binding.euroDegerTextView.text = rate.toString()
            "GBP" -> binding.sterlinDegerTextView.text = rate.toString()
            "JPY" -> binding.japonDegerTextView.text = rate.toString()
            "AUD" -> binding.avusturalyaDegerTextView.text = rate.toString()
            else -> Log.e("API_ERROR", "Unknown currency: $currencyName")
        }
    }
}