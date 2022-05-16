package com.fernando.movieapp.domain.usecases

import android.net.Uri
import com.fernando.core.domain.usecases.base.UseCase
import com.fernando.movieapp.domain.repositories.ImagesRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class SaveImagesUseCase @Inject constructor(
    private val repository: ImagesRepository,
    background:CoroutineDispatcher
):UseCase<Unit, List<Uri>>(background) {
    override suspend fun run(input: List<Uri>?) {
        requireNotNull(input) { "Images cant be null" }
        repository.saveImages(input)
    }

}