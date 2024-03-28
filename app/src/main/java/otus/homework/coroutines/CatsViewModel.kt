package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsViewModel.Result.Success

class CatsViewModel : ViewModel() {

    lateinit var catsService: CatsService
    val catsLiveData = MutableLiveData<Result>()

    fun fetchCat() {
        try {
            viewModelScope.launch(
                CoroutineExceptionHandler { _, _ ->
                    catsLiveData.value = Result.Error
                    CrashMonitor.trackWarning()
                }
            ) {
                val fact = async { catsService.getCatFact().first().id }
                val imageUrl = async { catsService.getImage().first().url }

                catsLiveData.value = Success(
                    CatContainer(
                        imageUrl = imageUrl.await(),
                        fact = fact.await()
                    )
                )
            }
        } catch (e: Exception) {
            catsLiveData.value = Result.Error
            CrashMonitor.trackWarning()
            throw IllegalStateException(e)
        }
    }

    sealed class Result {
        data class Success<T>(val data: T) : Result()
        object Error : Result()
    }
}