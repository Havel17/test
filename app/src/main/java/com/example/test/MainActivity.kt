package com.example.test

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.DataBase.DataBase
import com.example.test.DataBase.FavGifDao
import com.example.test.model.Gif
import com.example.test.retrofit.RemoteRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity(), MainAdapter.OnItemClick {
    private val API = "KvRGAK2AGT6UhLgboJD1C4ydiWHGQAyf"
    private val LIMIT = 24
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private lateinit var mainAdapter: MainAdapter
    var favList: MutableList<Gif> = mutableListOf()
    lateinit var favDao: FavGifDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainAdapter = MainAdapter(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        DataBase.getDatabase(this)

        favDao = DataBase.database.getGifDao()
        recyclerview.adapter = mainAdapter

        favoriteLoad()

        bClear.setOnClickListener { etSearch.text.clear(); getGif() }
        bSearch.setOnClickListener { getGif(etSearch.text.toString()) }
        cbShowFavorite.setOnClickListener {
            if (cbShowFavorite.isChecked) {
                mainAdapter.list.clear()
                mainAdapter.list.addAll(favList)
                mainAdapter.notifyDataSetChanged()
            } else {
                getGif()
            }
        }


        getGif()
    }

    fun getUrl(gifObj: String): String {
        var imagesObj = JSONObject(gifObj).getString("images")
        var originalObj = JSONObject(imagesObj).getString("fixed_height_downsampled")
        return JSONObject(originalObj).getString("url")
    }

    fun getGif() {
        mainAdapter.list.clear()
        lateinit var sucresp: String
        coroutineScope.launch {
            try {
                sucresp = RemoteRepository
                    .getRepositoryService()
                    .trending(API, LIMIT, "g")
            } catch (e: Exception) {
                Log.d("test1", "Ошибка запроса популярных гифок")
                e.printStackTrace()
            }
            withContext(Dispatchers.Main) {

                var Data = JSONObject(sucresp).getString("data")
                for (i in 0 until LIMIT) {

                    var gifObj = JSONArray(Data).getString(i)
                    mainAdapter.list.add(i, createGifModel(gifObj))
                }
                mainAdapter.notifyDataSetChanged()
            }
        }
    }

    fun createGifModel(gifObj: String): Gif {
        var url = getUrl(gifObj)
        var name = JSONObject(gifObj).getString("title")
        var idGif = JSONObject(gifObj).getString("id")
        return Gif(idGif, name, url)
    }

    fun getGif(findWord: String) {
        lateinit var resSearch: String
        if (findWord == "") {
            getGif()
        } else {
            coroutineScope.launch {
                try {
                    resSearch = RemoteRepository
                        .getRepositoryService()
                        .search(API, findWord, LIMIT, "ru")
                } catch (e: Exception) {
                    Log.d("test1", "Ошибка запроса поиска")
                }
                withContext(Dispatchers.Main) {
                    var Data = JSONObject(resSearch).getString("data")
                    for (i in 0 until LIMIT) {
                        var gifObj = JSONArray(Data).getString(i)

                        mainAdapter.list.add(i, createGifModel(gifObj))
                    }
                    mainAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    fun favoriteLoad() {
        coroutineScope.launch {
            favList.addAll(favDao.getAllFavGif())
            withContext(Dispatchers.Main){
                mainAdapter.favList.addAll(favList)
                favList.forEach {
                    Log.d("test1",it.idGif!!)
                }
            }
        }
    }


    override fun onFavoriteClick(gif: Gif,isFav:Boolean) {
        Log.d("test1",isFav.toString())
        if (isFav) {
            favList.add(gif)
            mainAdapter.favList.add(gif)
            coroutineScope.launch {
                favDao.insertFavGif(gif)
            }
        } else {
            Log.d("test1",gif.idGif.toString())
            favList.remove(gif)
            mainAdapter.favList.remove(gif)
            coroutineScope.launch {
                favDao.removeFavGif(gif.idGif)
            }
        }
    }
}