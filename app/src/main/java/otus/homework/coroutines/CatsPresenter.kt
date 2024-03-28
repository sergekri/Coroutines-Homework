package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = CoroutineName("CatsCoroutine") + Dispatchers.Main
}

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        try {
            presenterScope.launch {
                val fact = async { catsService.getCatFact().first().id }
                val imageUrl = async { catsService.getImage().first().url }

                _catsView?.populate(
                    CatsViewModel.Result.Success(
                        CatContainer(
                            imageUrl = imageUrl.await(),
                            fact = fact.await()
                        )
                    )
                )
            }
        } catch (e: SocketTimeoutException) {
            _catsView?.showToast(R.string.exception_socket_timeout)
            throw SocketTimeoutException()
        } catch (e: CancellationException) {
            _catsView?.showToast(e.message)
            throw CancellationException()
        }
        catch (e: Exception) {
            _catsView?.showToast(e.message)
            CrashMonitor.trackWarning()
            throw IllegalStateException(e)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() {
        presenterScope.cancel()
        Log.d("CANCEL", "coroutine cancelled")
    }
}