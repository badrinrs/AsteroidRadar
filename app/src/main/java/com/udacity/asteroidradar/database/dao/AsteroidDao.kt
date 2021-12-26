package com.udacity.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.udacity.asteroidradar.database.model.EntityAsteroid


@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroid order by close_approach_date ASC")
    fun getAll(): LiveData<List<EntityAsteroid>>

    @Query("SELECT * FROM asteroid where close_approach_date like '%' || :today || '%'")
    fun getAsteroidsFor(today: String): LiveData<List<EntityAsteroid>>

    @Insert(onConflict = REPLACE)
    fun insertAll(entityAsteroid: List<EntityAsteroid>)

    @Query("DELETE from asteroid where close_approach_date < :closeApproachDate")
    fun deleteWhere(closeApproachDate: String)
}