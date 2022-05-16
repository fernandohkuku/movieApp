package com.fernando.core.data.remote.paging


import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fernando.core.data.local.AppDatabase
import com.fernando.core.data.local.models.MovieModel
import com.fernando.core.data.local.models.RemoteKeys
import com.fernando.core.data.mappers.toModel
import com.fernando.core.data.remote.api.MovieService
import okio.IOException
import retrofit2.HttpException


@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val service: MovieService,
    private val db: AppDatabase
) : RemoteMediator<Int, MovieModel>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieModel>
    ): MediatorResult {

        val page = when(loadType){
            LoadType.APPEND ->{
                val remoteKeys = getLastKey(state)
                remoteKeys?.nextKey?:return MediatorResult.Success(  true)
            }
            LoadType.PREPEND -> {
                return  MediatorResult.Success( true)
            }
            LoadType.REFRESH -> {
                val remoteKeys = getClosedKey(state)
                remoteKeys?.nextKey?.minus(1)?:1
            }
        }
        try {
            val envelope = service.getDiscoverMovies(page).results
            val endOfPaginationReached = envelope.size < state.config.pageSize
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.movieDao().clearMovies()
                }
                val prevKey = if (page == START_PAGE) START_PAGE else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = envelope.map { movie ->
                    RemoteKeys(movie.id, prevKey, nextKey)
                }
                db.remoteKeysDao().insertAll(keys)
                db.movieDao().insertAll(envelope.map { it.toModel() })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


//    val page = when (loadType) {
//        LoadType.REFRESH -> {
//            val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//            remoteKeys?.nextKey?.minus(1) ?: START_PAGE
//        }
//        LoadType.PREPEND -> {
//            val remoteKeys = getRemoteKeyForFirstItem(state)
//            // If remoteKeys is null, that means the refresh result is not in the database yet.
//            // We can return Success with `endOfPaginationReached = false` because Paging
//            // will call this method again if RemoteKeys becomes non-null.
//            // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
//            // the end of pagination for prepend.
//            val prevKey = remoteKeys?.prevKey
//            if (prevKey == null) {
//                return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//            }
//            prevKey
//        }
//        LoadType.APPEND -> {
//            val remoteKeys = getRemoteKeyForLastItem(state)
//            // If remoteKeys is null, that means the refresh result is not in the database yet.
//            // We can return Success with `endOfPaginationReached = false` because Paging
//            // will call this method again if RemoteKeys becomes non-null.
//            // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
//            // the end of pagination for append.
//            val nextKey = remoteKeys?.nextKey
//            if (nextKey == null) {
//                return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//            }
//            nextKey
//        }
//    }


    private suspend fun LoadType.getPage(state: PagingState<Int, MovieModel>): Any {
        return when (this) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys !=null)
                prevKey
            }
        }
    }



    private suspend fun getLastKey(state: PagingState<Int, MovieModel>):RemoteKeys?{
        return state.lastItemOrNull()?.let {
            db.remoteKeysDao().remoteKeysRepoId(it.id)
        }
    }

    private suspend fun getClosedKey(state: PagingState<Int, MovieModel>):RemoteKeys?{
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                db.remoteKeysDao().remoteKeysRepoId(it.id)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieModel>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                db.remoteKeysDao().remoteKeysRepoId(movie.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieModel>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                db.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieModel>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }

    companion object {
        private const val START_PAGE = 1
    }

}