package com.tinfive.comicfilter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinfive.comicfilter.adapter.ComicAdapter
import com.tinfive.comicfilter.adapter.MultiChooseAdapter
import com.tinfive.comicfilter.common.Common
import com.tinfive.comicfilter.retrofit.IComicAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_category_filter.*

class CategoryFilter : AppCompatActivity() {
    internal val compositeDisposable = CompositeDisposable()
    internal lateinit var restApi: IComicAPI

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_filter)

        restApi = Common.api

        Handler().post{ fetchCategories()}

        recycler_filter.setHasFixedSize(true)
        recycler_filter.layoutManager = GridLayoutManager(this,2)

        btn_filter_category.setOnClickListener{
            if(Common.categories!!.isNotEmpty())
                showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        val alertDialog = AlertDialog.Builder(this@CategoryFilter)
        alertDialog.setTitle("Select Category")

        val inflater = this.layoutInflater
        val filter_options_layout = inflater.inflate(R.layout.dialog_filter, null)

        val recycler_options = filter_options_layout.findViewById(R.id.recycler_options) as RecyclerView
        recycler_options.setHasFixedSize(true)
        recycler_options.layoutManager = LinearLayoutManager(this)
        val adapter = MultiChooseAdapter(baseContext, Common.categories!!)
        recycler_options.adapter = adapter

        alertDialog.setView(filter_options_layout)

        alertDialog.setNegativeButton("CANCEL", { dialogInterface, _-> dialogInterface.dismiss()})
        alertDialog.setPositiveButton("FILTER", { dialogInterface, _-> fetchFilterCategory(adapter.filterArray)})
        alertDialog.show()
    }

    private fun fetchFilterCategory(filterArray: String) {
        compositeDisposable.add(restApi.getFilteredComic(filterArray)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ filterCategories ->
                recycler_filter.visibility = View.VISIBLE
                recycler_filter.adapter = ComicAdapter(baseContext,filterCategories)
            },
                {
                    Toast.makeText(baseContext, "", Toast.LENGTH_SHORT).show()
                    recycler_filter.visibility = View.VISIBLE
                }))
    }

    private fun fetchCategories() {
        compositeDisposable.add(restApi.categoryList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                categories -> Common.categories = categories
            },
                {
                    Toast.makeText(baseContext, "", Toast.LENGTH_SHORT).show()
                }))
    }
}
