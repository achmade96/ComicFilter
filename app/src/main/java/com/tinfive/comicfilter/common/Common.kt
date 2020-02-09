@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")

package com.tinfive.comicfilter.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.tinfive.comicfilter.model.Category
import com.tinfive.comicfilter.model.Chapter
import com.tinfive.comicfilter.model.Comic
import com.tinfive.comicfilter.retrofit.IComicAPI
import com.tinfive.comicfilter.retrofit.RetrofitClient

object Common {
    var categories: List<Category>?=ArrayList()
    var chapter_list: List<Chapter> = ArrayList()
    var selected_comic: Comic?=null



    fun isConnectedToInternet(baseContext: Context?): Boolean {
        val cm = baseContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < 23) {
            val ni = cm.activeNetworkInfo
            if (ni != null)
                return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
        } else {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

            }
        }
        return false
    }

    val api: IComicAPI
        get() {
            val retrofit = RetrofitClient.instance
            return retrofit.create(IComicAPI::class.java)
        }
}