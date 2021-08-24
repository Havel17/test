package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.model.Gif
import kotlinx.android.synthetic.main.item_gif.view.*

class MainAdapter(private val callback: OnItemClick) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    var list = mutableListOf<Gif>()
    var favList = mutableListOf<Gif>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_gif, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val gif = list[position]
        holder.bind(gif,favList)

        holder.itemView.cbGifFavorite.setOnClickListener {
            callback.onFavoriteClick(gif,holder.itemView.findViewById<CheckBox>(R.id.cbGifFavorite).isChecked)
        }
    }

    override fun getItemCount(): Int = list.size

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gifImage = itemView.findViewById<ImageView>(R.id.gifImage)
        private val gifFavoryte = itemView.findViewById<CheckBox>(R.id.cbGifFavorite)

        fun bind(gif: Gif, favlist:MutableList<Gif>) {
            var flag = false
            favlist.forEach {
                if(it.idGif == gif.idGif){
                    flag = true
                }
            }
            gifFavoryte.isChecked = flag
            Glide.with(itemView).load(gif.url).into(gifImage)
        }
    }

    interface OnItemClick {
        fun onFavoriteClick(gif:Gif,isFav:Boolean)
    }
}