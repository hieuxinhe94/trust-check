package com.example.trustcheck.data.repository

import com.example.trustcheck.data.models.PhoneData
import com.example.trustcheck.data.models.RecentWarning
import com.example.trustcheck.data.models.Report
import kotlinx.coroutines.flow.Flow

interface TrustCheckRepository {
    suspend fun checkCheatInput(cheatInput: String): Flow<Boolean>
    suspend fun reportCheat(cheatReport: Report): Flow<Boolean>
    suspend fun getCurrentWarning(): Flow<List<RecentWarning>>
    suspend fun getPhoneData(phoneNumber: String): Flow<PhoneData>
}