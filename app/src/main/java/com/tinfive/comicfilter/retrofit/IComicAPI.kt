package com.tinfive.comicfilter.retrofit

import com.tinfive.comicfilter.model.Banner
import com.tinfive.comicfilter.model.Category
import com.tinfive.comicfilter.model.Chapter
import com.tinfive.comicfilter.model.Comic
import io.reactivex.Observable
import retrofit2.http.*

interface IComicAPI {
    @get:GET("banner")
    val bannerList: Observable<List<Banner>>

    @get:GET("comic")
    val comicList:Observable<List<Comic>>

    @get:GET("categories")
    val categoryList:Observable<List<Category>>

    @GET ("chapter/{mangaid}")
    fun getChapterList(@Path("mangaid")mangaId : Int):Observable<List<Chapter>>

    @POST("filter")
    @FormUrlEncoded
    fun getFilteredComic(@Field("data")data : String) : Observable<List<Comic>>

    @POST("search")
    @FormUrlEncoded
    fun getSearchComic(@Field("search")data : String) : Observable<List<Comic>>
}