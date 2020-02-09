package com.tinfive.comicfilter.adapter

import com.tinfive.comicfilter.model.Banner
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class MainSliderAdapter (private val bannerList:List<Banner>) : SliderAdapter() {
    override fun getItemCount(): Int {
        return bannerList.size
    }

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        imageSlideViewHolder!!.bindImageSlide(bannerList[position].Link)
    }
}