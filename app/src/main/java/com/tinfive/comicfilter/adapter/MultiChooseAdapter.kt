package com.tinfive.comicfilter.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tinfive.comicfilter.R
import com.tinfive.comicfilter.model.Category
import kotlinx.android.synthetic.main.checkbox_item.view.*

class MultiChooseAdapter (internal var context: Context,
                          internal var categories:List<Category>) : RecyclerView.Adapter<MultiChooseAdapter.ChooseViewHolder>(){

    private val itemStateArray = SparseBooleanArray()
    internal var selected_category:MutableList<Category> = ArrayList()

    inner class ChooseViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        internal var ckb_options : CheckBox
        init {
            ckb_options = itemView.findViewById(R.id.ckb_text) as CheckBox
            ckb_options.setOnCheckedChangeListener{ _, b ->
                itemStateArray.put(adapterPosition,b)
                if(b)
                    selected_category.add(categories[adapterPosition])
                else
                    selected_category.remove(categories[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.checkbox_item,parent,false)
        return ChooseViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        holder.ckb_options.text = categories[position].Name
        holder.ckb_options.isChecked = itemStateArray.get(position)
    }

    val filterArray:String
    get(){
        val id_selected = ArrayList<Int>()
        for(category in selected_category)
            id_selected.add(category.ID)
        return Gson().toJson(id_selected)
    }
}