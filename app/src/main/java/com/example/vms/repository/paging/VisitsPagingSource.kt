package com.example.vms.repository.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vms.model.Visit
import com.example.vms.model.asModelVisit
import com.example.vms.repository.api.VisitsClient

/**
 * Created by mśmiech on 08.09.2023.
 */
class VisitsPagingSource(
    private val api: VisitsClient
) : PagingSource<String, Visit>() {
    override fun getRefreshKey(state: PagingState<String, Visit>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Visit> {
        return kotlin.runCatching {
            if (params.key != null) {
                throw IllegalStateException()
            }
            api.getVisits(cursor = params.key, limit = params.loadSize.coerceAtMost(50))
        }
            .fold(
                onSuccess = { response ->
                    LoadResult.Page(
                        response.visits.map { it.asModelVisit() },
                        null,
                        response.cursor
                    )
                },
                onFailure = {
                    Log.e("VisitsPagingSource", "load failed", it)
                    LoadResult.Error(it)
                }
            )
    }
}