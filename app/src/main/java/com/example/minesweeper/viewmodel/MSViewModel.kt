package com.example.minesweeper.viewmodel

import androidx.lifecycle.ViewModel
import com.example.minesweeper.data.SharedPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MSViewModel : ViewModel() {
    val prefs = SharedPrefs.getInstance()

    private val _flags = MutableStateFlow(emptyList<Pair<Int, Int>>())
    val flags = _flags.asStateFlow()

    private val _flagCount = MutableStateFlow(prefs.mineCount)
    val flagCount = _flagCount.asStateFlow()

    fun updateFlag(row: Int, col: Int) {
        val list = flags.value.toMutableList()
        if (list.contains(row to col)) {
            list.remove(row to col)
        } else {
            list.add(row to col)
        }
        _flags.value = list

    }

    private val _mines = MutableStateFlow(emptyList<Pair<Int, Int>>())
    val mines = _mines.asStateFlow()
    fun setMines(minesList: MutableList<Pair<Int, Int>>) {
        _mines.value = minesList
        _flagCount.value = prefs.mineCount - flags.value.size

    }

    fun reset() {
        _flags.value = emptyList()
        _mines.value = emptyList()
    }
}