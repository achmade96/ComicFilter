package com.tinfive.comicfilter

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.tinfive.comicfilter.adapter.ComicAdapter
import com.tinfive.comicfilter.adapter.MainSliderAdapter
import com.tinfive.comicfilter.common.Common
import com.tinfive.comicfilter.retrofit.IComicAPI
import com.tinfive.comicfilter.service.PicassoImageLoadingService
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import ss.com.bannerslider.Slider

class MainActivity : AppCompatActivity() {

    internal val compositeDisposable = CompositeDisposable()
    internal lateinit var restApi: IComicAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init api
        restApi = Common.api

        Slider.init(PicassoImageLoadingService(this))

        //view
        btn_search.setOnClickListener{startActivity(Intent(this@MainActivity,CategoryFilter::class.java))}

        //view setup
        recycler_comic.setHasFixedSize(true)
        recycler_comic.layoutManager = GridLayoutManager(this, 2)

        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_orange_dark,
            android.R.color.background_dark
        )
        swipe_refresh.setOnRefreshListener {
            if (Common.isConnectedToInternet(baseContext)) {
                fetchBanner()
                fetchComic()
            } else {
                Toast.makeText(baseContext, "Please check your connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        //default load first time
        swipe_refresh.post(Runnable {
            if (Common.isConnectedToInternet(baseContext)) {
                fetchBanner()
                fetchComic()
            } else {
                Toast.makeText(baseContext, "Please check your connection", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        )
    }

    private fun fetchComic() {
        val dialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage(" Please wait... ")
            .build()
        if (!swipe_refresh.isRefreshing)
            dialog.show()
        compositeDisposable.add(restApi.comicList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ comicList ->
                txt_comic.text = StringBuilder("NEW COMIC (")
                    .append(comicList.size)
                    .append(")")
                recycler_comic.adapter = ComicAdapter(baseContext, comicList)
                if (!swipe_refresh.isRefreshing)
                    dialog.dismiss()
                swipe_refresh.isRefreshing = false
            },
            {thr ->
                Toast.makeText(baseContext,""+thr.message,Toast.LENGTH_SHORT).show()
        }))

    }

    private fun fetchBanner() {
        compositeDisposable.add(
            restApi.bannerList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ banners ->
                    banner_slider.setAdapter(MainSliderAdapter(banners))
                },
                    {
                        Toast.makeText(
                            baseContext,
                            "Please check your connection ya",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
        )
    }
}

