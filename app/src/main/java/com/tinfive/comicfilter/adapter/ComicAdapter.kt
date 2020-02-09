package com.tinfive.comicfilter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tinfive.comicfilter.R
import com.tinfive.comicfilter.`interface`.IRecyclerOnClick
import com.tinfive.comicfilter.common.Common
import com.tinfive.comicfilter.model.Comic
import kotlinx.android.synthetic.main.comic_item.view.*

class ComicAdapter (internal var context: Context,
                    internal var mangaList: List<Comic>) : RecyclerView.Adapter<ComicAdapter.ComicViewHolder>() {

    inner class ComicViewHolder (itemView : View) :  RecyclerView.ViewHolder(itemView),View.OnClickListener{

        internal var comic_image : ImageView
        internal var comic_name: TextView
        lateinit var iRecyclerOnClick : IRecyclerOnClick

        fun setClick(iRecyclerOnClick: IRecyclerOnClick)
        {
            this.iRecyclerOnClick = iRecyclerOnClick
        }

        init {
            comic_image = itemView.findViewById(R.id.comic_image) as ImageView
            comic_name = itemView.findViewById(R.id.comic_name) as TextView
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!, adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.comic_item,parent, false)
        return ComicViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mangaList.size
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        Picasso.get().load(mangaList[position].Image).into(holder.comic_image)
        holder.comic_name.text = mangaList[position].Name
        holder.setClick(object : IRecyclerOnClick{
            override fun onClick(view: View, position: Int) {
                Common.selected_comic = mangaList[position]
            }

        })
    }
}