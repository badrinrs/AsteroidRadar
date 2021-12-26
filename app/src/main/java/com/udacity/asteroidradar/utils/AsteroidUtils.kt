package com.udacity.asteroidradar.utils

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.model.EntityAsteroid
import com.udacity.asteroidradar.model.Asteroid
import java.text.SimpleDateFormat
import java.util.*

object AsteroidUtils {
    fun fromEntityToAsteroid(entityAsteroid: EntityAsteroid): Asteroid {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return Asteroid(
            id = entityAsteroid.id,
            codename = entityAsteroid.codename,
            closeApproachDate = dateFormat.format(entityAsteroid.closeApproachDate),
            absoluteMagnitude = entityAsteroid.absoluteMagnitude,
            estimatedDiameter = entityAsteroid.estimatedDiameter,
            relativeVelocity = entityAsteroid.relativeVelocity,
            distanceFromEarth = entityAsteroid.distanceFromEarth,
            isPotentiallyHazardous = entityAsteroid.isPotentiallyHazardous
        )
    }

    fun fromAsteroidToEntity(asteroid: Asteroid): EntityAsteroid {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return EntityAsteroid(
            id = asteroid.id,
            codename = asteroid.codename,
            closeApproachDate = asteroid.closeApproachDate,
            absoluteMagnitude = asteroid.absoluteMagnitude,
            estimatedDiameter = asteroid.estimatedDiameter,
            relativeVelocity = asteroid.relativeVelocity,
            distanceFromEarth = asteroid.distanceFromEarth,
            isPotentiallyHazardous = asteroid.isPotentiallyHazardous
        )
    }
}