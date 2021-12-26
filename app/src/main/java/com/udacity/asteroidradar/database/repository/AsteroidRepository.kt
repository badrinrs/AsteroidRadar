package com.udacity.asteroidradar.database.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaAPI
import com.udacity.asteroidradar.api.getDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.model.EntityAsteroid
import com.udacity.asteroidradar.database.model.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.utils.AsteroidUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allAsteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao().getAll()) {
        it.asDomainModel()
    }

    val asteroidsToday: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao().getAsteroidsFor(getDay(0))) {
        it.asDomainModel()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(entityAsteroid: List<EntityAsteroid>) {
        database.asteroidDao().insertAll(entityAsteroid)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteWhere(closeApproachDate: String) {
        database.asteroidDao().deleteWhere(closeApproachDate)
    }

    @SuppressWarnings("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAsteroidsFor(today: String) {
        database.asteroidDao().getAsteroidsFor(today)
    }

    @SuppressWarnings("RedundantSuspendModifier")
    @WorkerThread
    suspend fun refreshAsteroids(startDate: String = getDay(0), endDate: String = getDay(6)) {
        withContext(Dispatchers.IO){
            val response = NasaAPI.retrofitService.getFeed(startDate, endDate, Constants.API_KEY)
            val jsonObj = JSONObject(response)
            val asteroidList = parseAsteroidsJsonResult(jsonObj)
            val entityAsteroid = ArrayList<EntityAsteroid>()
            for(asteroid in asteroidList) {
                entityAsteroid.add(AsteroidUtils.fromAsteroidToEntity(asteroid))
            }
            insertAll(entityAsteroid)
        }
    }

    @SuppressWarnings("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteOlder(startDate: String = getDay(0)) {
        withContext(Dispatchers.IO){
            deleteWhere(startDate)
        }
    }

}