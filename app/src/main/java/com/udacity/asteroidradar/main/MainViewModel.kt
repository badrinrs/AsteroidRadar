package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaAPI
import com.udacity.asteroidradar.api.getDay
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.database.repository.AsteroidRepository
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val sTAG = MainViewModel::class.java.simpleName

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    enum class PictureOfTheDayStatus { LOADING, ERROR, DONE }

    private val _podStatus = MutableLiveData<PictureOfTheDayStatus>()
    val podStatus: LiveData<PictureOfTheDayStatus>
        get() = _podStatus

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private var _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    private var _asteroidList = asteroidRepository.allAsteroids
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    init {
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()
            } catch (e: Exception){
                Log.e(sTAG, "Error: ${e.printStackTrace()}")
            }
        }
        getPictureOfTheDay()
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

    fun getAsteroidsForToday() {
        viewModelScope.launch {
            Log.d(sTAG, "Today: ${getDay(0)}")
            _asteroidList = asteroidRepository.asteroidsToday
        }
    }

    fun getAllAsteroids() {
        viewModelScope.launch {
            _asteroidList = asteroidRepository.allAsteroids
        }
    }
}