package fr.isen.siham.thehreatestcocktailapp.Network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("random.php")
    fun getRandomCocktail(): Call<Drinks>

    @GET("list.php?c=list")
    fun getListCategory(): Call<Drinks>

    @GET("filter.php")
    fun getDrinkByCategory(@Query(value="c") category: String): Call<Drinks>

    @GET("lookup.php")
    fun getCocktailById(@Query("i") id: String): Call<Drinks>

    @GET("search.php")
    fun searchCocktail(@Query("s") name: String): Call<Drinks>
}