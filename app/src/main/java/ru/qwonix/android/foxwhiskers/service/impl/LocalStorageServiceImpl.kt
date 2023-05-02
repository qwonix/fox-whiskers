package ru.qwonix.android.foxwhiskers.service.impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import ru.qwonix.android.foxwhiskers.entity.UserProfile
import ru.qwonix.android.foxwhiskers.repository.ResponseDao
import ru.qwonix.android.foxwhiskers.service.LocalStorageService

class LocalStorageServiceImpl(
    private val context: Context
) : LocalStorageService {

    private val Context.dataStore by preferencesDataStore(name = "UserProfile")

    private companion object {
        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val EMAIL = stringPreferencesKey("email")
        val PHONE_NUMBER = stringPreferencesKey("phone_number")
        val JWT_ACCESS_TOKEN = stringPreferencesKey("jwt_access_token")
        val JWT_REFRESH_TOKEN = stringPreferencesKey("jwt_refresh_token")
    }


    override suspend fun loadUserProfile(): ResponseDao<UserProfile?> {
        val preferences = context.dataStore.data.firstOrNull()
        return if (preferences == null || !preferences.contains(FIRST_NAME)) {
            ResponseDao.ofNullable(null)
        } else {
            ResponseDao.ofSuccess(
                UserProfile(
                    firstName = preferences[FIRST_NAME],
                    lastName = preferences[LAST_NAME],
                    email = preferences[EMAIL],
                    phoneNumber = preferences[PHONE_NUMBER] ?: "",
                    jwtAccessToken = preferences[JWT_ACCESS_TOKEN] ?: "",
                    jwtRefreshToken = preferences[JWT_REFRESH_TOKEN] ?: ""
                )
            )
        }
    }

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        context.dataStore.edit { preferences ->
            preferences[FIRST_NAME] = userProfile.firstName ?: ""
            preferences[LAST_NAME] = userProfile.lastName ?: ""
            preferences[EMAIL] = userProfile.email ?: ""
            preferences[PHONE_NUMBER] = userProfile.phoneNumber
            preferences[JWT_ACCESS_TOKEN] = userProfile.jwtAccessToken
            preferences[JWT_REFRESH_TOKEN] = userProfile.jwtRefreshToken
        }
    }

    override suspend fun clearUserProfile() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}