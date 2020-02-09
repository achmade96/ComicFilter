package com.tinfive.comicfilter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tinfive.comicfilter.R
import com.tinfive.comicfilter.`interface`.IRecyclerOnClick
import com.tinfive.comicfilter.model.Chapter
import kotlinx.android.synthetic.main.chapter_item.view.*

class MyChapterAdapter (internal var context: Context,
                        internal var chapterList:List<Chapter>) : RecyclerView.Adapter<MyChapterAdapter.ChapterViewHolder>() {

    inner class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal var txt_chapter_name : TextView
        internal lateinit var iRecyclerOnClick: IRecyclerOnClick

        fun setClick(iRecyclerOnClick: IRecyclerOnClick)
        {
            this.iRecyclerOnClick = iRecyclerOnClick
        }
        init {
            txt_chapter_name = itemView.findViewById(R.id.txt_chapter_number) as TextView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.chapter_item,parent,false)
        return ChapterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.txt_chapter_name.text = chapterList[position].Name
    }
}