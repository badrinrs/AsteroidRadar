package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaAPI
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel: ViewModel() {

    private val sTAG = MainViewModel::class.java.simpleName

    enum class PictureOfTheDayStatus { LOADING, ERROR, DONE }
    enum class AsteroidStatus {LOADING, ERROR, DONE}

    private val _podStatus = MutableLiveData<PictureOfTheDayStatus>()
    val podStatus: LiveData<PictureOfTheDayStatus>
        get() = _podStatus

    private val _asteroidStatus = MutableLiveData<AsteroidStatus>()
    val asteroidStatus: LiveData<AsteroidStatus>
        get() = _asteroidStatus

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
    init {
        getPictureOfTheDay()
        getAsteroidList()
    }

    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            _podStatus.value = PictureOfTheDayStatus.LOADING
            Log.d(sTAG, "Inside View Model Scope")
            try {
                val pictureOfDay = NasaAPI.retrofitService.getPictureOfDay(Constants.API_KEY)
                Log.d(sTAG, "View Model Scope, Picture Of The Day URL: ${pictureOfDay.url}")
                _podStatus.value = PictureOfTheDayStatus.DONE
                _pictureOfTheDay.value = pictureOfDay
            } catch (e: Exception) {
                e.printStackTrace()
                _podStatus.value = PictureOfTheDayStatus.ERROR
            }
        }
    }

    private fun getAsteroidList() {
        viewModelScope.launch {
            _asteroidStatus.value = AsteroidStatus.LOADING
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val calendar = Calendar.getInstance()
                val startDate = dateFormat.format(Date())
                calendar.time = Date()
                calendar.add(Calendar.DAY_OF_YEAR, 7)
                val endDate = dateFormat.format(calendar.time)
                Log.d(sTAG, "Start Date: $startDate")
                Log.d(sTAG, "End Date: $endDate")

                val asteroidList = parseAsteroidsJsonResult(
                    JSONObject(
                        NasaAPI.retrofitService.getFeed(
                            apiKey = Constants.API_KEY,
                            startDate = startDate,
                            endDate = endDate
                        )
                    )
                )
                _asteroidStatus.value = AsteroidStatus.DONE
//                _asteroidList.value = asteroidList
                Log.d(sTAG, "Asteroid List: $asteroidList")
            } catch (e: Exception) {
                _asteroidStatus.value = AsteroidStatus.ERROR
                Log.d(sTAG, "Error:")
                e.printStackTrace()
            }
        }
    }
}