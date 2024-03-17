package otus.homework.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }
}