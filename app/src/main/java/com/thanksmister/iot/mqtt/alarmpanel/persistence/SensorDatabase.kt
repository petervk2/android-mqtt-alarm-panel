/*
 * Copyright (c) 2018 ThanksMister LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thanksmister.iot.mqtt.alarmpanel.persistence

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context

/**
 * The Room database that contains the Sensors table
 */
@Database(entities = arrayOf(Sensor::class, Weather::class, Sun::class), version = 10, exportSchema = false)
@TypeConverters(ForecastConverter::class)
abstract class SensorDatabase : RoomDatabase() {

    abstract fun sensorDao(): SensorDao
    abstract fun weatherDao(): WeatherDao
    abstract fun sunDao(): SunDao

    companion object {

        @Volatile private var INSTANCE: SensorDatabase? = null

        @JvmStatic fun getInstance(context: Context): SensorDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        SensorDatabase::class.java, "sensors.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}