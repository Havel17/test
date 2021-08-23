package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_gif.view.*

class MainAdapter(private val callback: OnItemClick) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    var list = mutableListOf<String>()
    var favList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_gif, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val url = list[position]
        holder.bind(url,favList)

        holder.itemView.cbGifFavorite.setOnClickListener {
            callback.onFavoriteClick(url)
        }
    }

    override fun getItemCount(): Int = list.size

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gifImage = itemView.findViewById<ImageView>(R.id.gifImage)
        private val gifFavoryte = itemView.findViewById<CheckBox>(R.id.cbGifFavorite)

        fun bind(url: String, favlist:MutableList<String>) {
            if(favlist.contains(url)){
                gifFavoryte.isChecked = true
            }else{
                gifFavoryte.isChecked = false
            }
            Glide.with(itemView).load(url).into(gifImage)
        }
    }

    interface OnItemClick {
        fun onFavoriteClick(gifUrl:String)
    }
}