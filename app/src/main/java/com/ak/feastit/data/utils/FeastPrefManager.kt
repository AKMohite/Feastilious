package com.ak.feastit.data.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class FilterPreferences(
    val isFirstInstall: Boolean,
    val searchQuery: String
)
private const val TAG = "FeastPrefManager"

@Singleton
class FeastPrefManager @Inject constructor(
    @ApplicationContext context: Context,
    private val dispatcher: DispatcherProvider
) {

    private val dataStore = context.createDataStore("feat_prefs")

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                Log.e(TAG, "Error reading preferences: ", exception)
                emit(emptyPreferences()) // will use default preferences
            } else{
                throw exception
            }
        }
        .flowOn(dispatcher.io)
        .map { preferences->
            val firstInstall = preferences[PreferencesKeys.IS_FIRST_INSTALL] ?: true
            val searchQuery = preferences[PreferencesKeys.SEARCH_QUERY] ?: ""
            FilterPreferences(firstInstall, searchQuery)
        }.flowOn(dispatcher.computation)

    suspend fun updateFirstInstall(isFirstInstall: Boolean) = withContext(dispatcher.io){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_INSTALL] = isFirstInstall
        }
    }

    private object PreferencesKeys{
        val IS_FIRST_INSTALL = booleanPreferencesKey("pref_first_install")
        val SEARCH_QUERY = stringPreferencesKey("pref_search_query")
    }
}