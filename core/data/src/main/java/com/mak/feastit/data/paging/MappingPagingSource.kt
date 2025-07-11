// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mak.feastit.domain.model.PaginatedEntry

/**
 * Reference: [Map PagingSource<Int, Entities> to PagingSource<RepositoryModel>](https://stackoverflow.com/a/73674908)
 */
internal class MappingPagingSource<Key : Any, Entity : Any, DomainModel : PaginatedEntry>(
  private val originalSource: PagingSource<Key, Entity>,
  private val mapper: (Entity) -> DomainModel,
) : PagingSource<Key, DomainModel>() {
  override fun getRefreshKey(state: PagingState<Key, DomainModel>): Key? = originalSource.getRefreshKey(
    PagingState(
      pages = emptyList(),
      leadingPlaceholderCount = 0,
      anchorPosition = state.anchorPosition,
      config = state.config,
    ),
  )

  override suspend fun load(params: LoadParams<Key>): LoadResult<Key, DomainModel> = when (val originalResult = originalSource.load(params)) {
    is LoadResult.Error -> LoadResult.Error(originalResult.throwable)
    is LoadResult.Invalid -> LoadResult.Invalid()
    is LoadResult.Page ->
      LoadResult.Page(
        data = originalResult.data.map(mapper),
        prevKey = originalResult.prevKey,
        nextKey = originalResult.nextKey,
      )
  }

  override val jumpingSupported: Boolean
    get() = originalSource.jumpingSupported
}
