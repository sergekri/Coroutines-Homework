package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("images/search")
    suspend fun getCatFact(): List<Image>

    @GET("images/search")
    suspend fun getImage(): List<Image>
}