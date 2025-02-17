package com.nexxlab.minesweeper.data

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs {
    private var mContext:Context? = null

    companion object {
        private const val GRID_SIZE_KEY = "grid_size"
        private const val MINE_COUNT_KEY = "mine_count"
        private var instance: SharedPrefs? = null

        fun getInstance(): SharedPrefs {
            if (instance == null) {
                instance = SharedPrefs()
            }
            return instance!!
        }
    }

    private val prefs by lazy { mContext!!.getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE) }

    private val editor by lazy { prefs.edit() }

    fun setContext(context: Context){
        this.mContext = context
    }


    var mineCount:Int
        get() {
            return prefs.getInt(MINE_COUNT_KEY, 10)
        }
        set(value) {
            editor.putInt(MINE_COUNT_KEY, value)
            editor.apply()
        }

    var gridSize:Int
        get() {
            return prefs.getInt(GRID_SIZE_KEY, 8)
        }
        set(value) {
            editor.putInt(GRID_SIZE_KEY, value)
            editor.apply()
        }

}
