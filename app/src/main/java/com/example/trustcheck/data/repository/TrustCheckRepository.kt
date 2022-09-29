package com.example.trustcheck.data.repository

import com.example.trustcheck.data.models.RecentWarning
import com.example.trustcheck.data.models.Report
import com.example.trustcheck.data.state.DataState
import kotlinx.coroutines.flow.Flow

interface TrustCheckRepository {
    suspend fun checkCheatInput(cheatInput: String): Flow<DataState<Boolean>>
    suspend fun reportCheat(cheatReport: Report): Flow<DataState<Boolean>>
    suspend fun getCurrentWarning(): Flow<DataState<List<RecentWarning>>>
}