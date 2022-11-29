package com.example.tripso.domain.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.tripso.application.MyApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

//not used yet

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object DataStoreUtils {


    /*fun readPreference(key: String, defaultValue: Int): Flow<Int> {
        val keyPreferences = intPreferencesKey(key)
        return MyApplication.context.dataStore.data.map {
            it[keyPreferences] ?: defaultValue
        }
    }*/

    fun readPreference (key: String,defaultValue: String) : String {
        val keyPreferences = stringPreferencesKey(key)
        return runBlocking {
            MyApplication.context.dataStore.data.map {
                it[keyPreferences] ?: defaultValue
            }.first()
        }
    }


    suspend fun savePreference(key: String, value: String) {
        val keyPreferences = stringPreferencesKey(key)
        MyApplication.context.dataStore.edit {
            it[keyPreferences] = value
        }
    }


}
