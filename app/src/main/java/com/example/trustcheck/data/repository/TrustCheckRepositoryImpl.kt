package com.example.trustcheck.data.repository

import com.example.trustcheck.data.models.PhoneData
import com.example.trustcheck.data.models.RecentWarning
import com.example.trustcheck.data.models.Report
import com.example.trustcheck.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TrustCheckRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TrustCheckRepository {
    override suspend fun checkCheatInput(cheatInput: String): Flow<Boolean> {
        return flow {
        }
    }

    override suspend fun reportCheat(cheatReport: Report): Flow<Boolean> {
        return flow {
        }
    }

    override suspend fun getCurrentWarning(): Flow<List<RecentWarning>> {
        return flow {

        }
    }

    override suspend fun getPhoneData(phoneNumber: String): Flow<PhoneData> {
        return flow {
            emit(apiService.findPhone(phoneNumber))
        }
    }
}