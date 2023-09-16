package com.example.vms.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vms.model.Request
import com.example.vms.model.asModelRequest
import com.example.vms.repository.api.VisitsClient

class RequestsPagingSource(
    private val api: VisitsClient
) : PagingSource<String, Request>() {
    override fun getRefreshKey(state: PagingState<String, Request>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Request> {
        return kotlin.runCatching {
            if (params.key != null) {
                throw IllegalStateException()
            }
            api.getRequests(cursor = params.key, limit = params.loadSize.coerceAtMost(50))
        }
            .fold(
                onSuccess = { response ->
                    LoadResult.Page(
                        response.visitRequests.map { it.asModelRequest() },
                        null,
                        response.cursor
                    )
                },
                onFailure = {
                    LoadResult.Error(it)
                }
            )
    }
}