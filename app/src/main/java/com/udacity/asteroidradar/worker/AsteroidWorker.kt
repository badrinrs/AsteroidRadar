package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.database.repository.AsteroidRepository
import retrofit2.HttpException

class AsteroidWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "ASTEROID_WORKER"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)

        val repository = AsteroidRepository(database)

        return try {
            repository.refreshAsteroids()
            repository.deleteOlder()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}