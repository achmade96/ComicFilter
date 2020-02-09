package com.tinfive.comicfilter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinfive.comicfilter.adapter.MyChapterAdapter
import com.tinfive.comicfilter.common.Common
import com.tinfive.comicfilter.retrofit.IComicAPI
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_chapter.*
import kotlinx.android.synthetic.main.activity_main.*

class ChapterActivity : AppCompatActivity() {

    internal val compositeDisposable = CompositeDisposable()
    internal lateinit var restApi: IComicAPI

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        //api
        restApi = Common.api

        toolbar.title = Common.selected_comic!!.Name
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp)
        toolbar.setNavigationOnClickListener { finish() }

        //view
        recycler_chapter.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recycler_chapter.layoutManager = layoutManager
        recycler_chapter.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

        fetchChapter(Common.selected_comic!!.ID)
    }

    private fun fetchChapter(id: Int) {
        val dialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage(" Please wait... ")
            .build()
            dialog.show()
        compositeDisposable.add(
            restApi.getChapterList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ chapterList ->
                    txt_chapter.text = StringBuilder("CHAPTER (")
                        .append(chapterList.size)
                        .append(")")

                    recycler_comic.adapter = MyChapterAdapter(baseContext, chapterList)
                    Common.chapter_list = chapterList // save prev-next nav
                    dialog.dismiss()
                },
                    { thr ->
                        Toast.makeText(baseContext, "" + thr.message, Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    })
        )
    }
}
