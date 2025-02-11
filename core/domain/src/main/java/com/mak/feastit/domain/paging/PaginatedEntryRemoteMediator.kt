package com.mak.feastit.domain.paging

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mak.feastit.domain.model.PaginatedEntry
import kotlin.coroutines.cancellation.CancellationException

/**
 * Reference: [Tivi](https://github.com/chrisbanes/tivi/blob/main/domain/src/commonMain/kotlin/app/tivi/domain/PaginatedEntryRemoteMediator.kt)
 */
@OptIn(androidx.paging.ExperimentalPagingApi::class)
class PaginatedEntryRemoteMediator<Entity>(
    private val fetch: suspend (page: Int) -> Unit,
) : RemoteMediator<Int, Entity>() where Entity : PaginatedEntry {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Entity>,
    ): MediatorResult {
        val nextPage = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastItem.page + 1
            }
            else -> error("Unknown LoadType: $loadType")
        }
        return try {
            fetch(nextPage)
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}