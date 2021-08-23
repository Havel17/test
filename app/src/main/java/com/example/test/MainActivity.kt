package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.retrofit.RemoteRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), MainAdapter.OnItemClick {
    private val API = "KvRGAK2AGT6UhLgboJD1C4ydiWHGQAyf"
    private val LIMIT = 24
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private lateinit var mainAdapter: MainAdapter
    private val FAVORITESIZE = "favorite_size"
    var savedList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainAdapter = MainAdapter(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = mainAdapter

        if(!getPreferences(MODE_PRIVATE).contains(FAVORITESIZE)){
            getPreferences(MODE_PRIVATE).edit()
                .putInt(FAVORITESIZE,0)
        }else {
            favoriteLoad()
            mainAdapter.favList.addAll(savedList)
        }


        bClear.setOnClickListener { etSearch.text.clear(); getGif() }
        bSearch.setOnClickListener { getGif(etSearch.text.toString()) }
        cbShowFavorite.setOnClickListener {
            if(cbShowFavorite.isChecked) {
                mainAdapter.list.clear()
                mainAdapter.list.addAll(savedList)
                mainAdapter.notifyDataSetChanged()
            }else{
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
                    mainAdapter.list.add(i, getUrl(gifObj))
                }
                mainAdapter.notifyDataSetChanged()
            }
        }
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
                        mainAdapter.list.add(i, getUrl(gifObj))
                    }
                    mainAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    fun favoriteLoad() {
        for (i in 0 until getPreferences(MODE_PRIVATE).getInt(FAVORITESIZE,0)){
            savedList.add(getPreferences(MODE_PRIVATE).getString(i.toString(),"")!!)
        }
    }

    override fun onFavoriteClick(gifUrl: String) {
        if(!savedList.contains(gifUrl)) {
            savedList.add(gifUrl)
            mainAdapter.favList.add(gifUrl)
            getPreferences(MODE_PRIVATE)
                .edit()
                .putString((savedList.size - 1).toString(), gifUrl)
                .putInt(FAVORITESIZE, savedList.size)
                .apply()
        }else{
            getPreferences(MODE_PRIVATE)
                .edit()
                .remove(savedList.indexOf(gifUrl).toString())
                .apply()
            Log.d("test1",savedList.indexOf(gifUrl).toString())
            mainAdapter.favList.remove(gifUrl)
            savedList.remove(gifUrl)

        }
    }

}