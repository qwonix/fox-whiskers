package ru.qwonix.android.foxwhiskers.service

import ru.qwonix.android.foxwhiskers.AuthenticationResponseDTO
import ru.qwonix.android.foxwhiskers.entity.UserProfile
import ru.qwonix.android.foxwhiskers.repository.ResponseDao

interface AuthenticationService {

    suspend fun findUserProfile(
        phoneNumber: String,
        jwtAccessToken: String
    ): ResponseDao<UserProfile?>

    suspend fun authenticate(
        phoneNumber: String,
        code: Int
    ): ResponseDao<AuthenticationResponseDTO?>

    suspend fun sendAuthenticationSmsCodeToNumber(phoneNumber: String)
    suspend fun updateProfile(userProfile: UserProfile): ResponseDao<UserProfile?>
}