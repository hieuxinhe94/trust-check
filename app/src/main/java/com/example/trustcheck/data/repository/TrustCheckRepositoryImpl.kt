package com.example.trustcheck.data.repository

import com.example.trustcheck.data.models.RecentWarning
import com.example.trustcheck.data.models.Report
import com.example.trustcheck.data.remote.ApiService
import com.example.trustcheck.data.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TrustCheckRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TrustCheckRepository {
    override suspend fun checkCheatInput(cheatInput: String): Flow<DataState<Boolean>> {
        return flow {

        }
    }

    override suspend fun reportCheat(cheatReport: Report): Flow<DataState<Boolean>> {
        return flow {

        }
    }

    override suspend fun getCurrentWarning(): Flow<DataState<List<RecentWarning>>> {
        return flow {

        }
    }
}