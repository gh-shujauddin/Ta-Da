package com.qadri.to_do.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "sync_prefs")

class SyncSettingsManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    companion object {
        val LAST_SYNC_TIME_KEY = longPreferencesKey("last_sync_time")
    }

    val lastSyncTimeFlow: Flow<Long> = context.datastore
        .data.map { preferences ->
            preferences[LAST_SYNC_TIME_KEY] ?: 0
        }

    // Save the time
    suspend fun updateLastSyncTime(timestamp: Long) {
        context.datastore.edit { preferences ->
            preferences[LAST_SYNC_TIME_KEY] = timestamp
        }
    }
}