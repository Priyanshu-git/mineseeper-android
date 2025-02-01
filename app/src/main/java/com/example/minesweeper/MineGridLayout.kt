package com.example.minesweeper

import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout
import androidx.core.view.marginLeft
import com.example.minesweeper.ui.CustomCellView

class MineGridLayout(
    context: Context,
    attrs: AttributeSet? = null
) : GridLayout(context, attrs) {

    private var gridSize = 0

    fun setGridSize(size: Int) {
        gridSize = size
        rowCount = size
        columnCount = size
        orientation = HORIZONTAL
        createGrid()
    }

    private fun createGrid() {
        removeAllViews()
        val screenWidth = resources.displayMetrics.widthPixels
        val cellWidth = screenWidth / gridSize
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val cellView = CustomCellView(context).apply {
                    layoutParams = LayoutParams().apply {
                        width = cellWidth
                        height = cellWidth
                        rowSpec = spec(row, 1f)
                        columnSpec = spec(col, 1f)
                    }
                }
                addView(cellView)
                setupClickListener(cellView, row, col)
            }
        }
    }

    private fun setupClickListener(cellView: CustomCellView, row: Int, col: Int) {
        cellView.setOnClickListener {
            handleCellClick(cellView, row, col)
        }
        cellView.setOnLongClickListener {
            handleCellLongClick(cellView, row, col)
            true
        }
    }

    private fun handleCellClick(cellView: CustomCellView, row: Int, col: Int) {
        uncoverMines(row, col)
    }

    private fun uncoverMines(row: Int, col: Int) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize || cell(row, col).isUncovered())
            return
        setCellCount(row, col)
        setCellCount(row-1, col)
        setCellCount(row+1, col)
        setCellCount(row, col+1)
        setCellCount(row, col-1)
        setCellCount(row+1, col+1)
        setCellCount(row+1, col-1)
        setCellCount(row-1, col+1)
        setCellCount(row-1, col-1)
    }

    private fun setCellCount(row: Int, col: Int) {
        var count = 0
        if (row>0 && cell(row-1, col).isMine()) { count++
            if (col>0 && cell(row-1, col-1).isMine()) count++
            if (col<gridSize-1 && cell(row-1, col+1).isMine()) count++
        }
        if (row<gridSize-1 && cell(row+1, col).isMine()) { count++
            if (col>0 && cell(row+1, col-1).isMine()) count++
            if (col<gridSize-1 && cell(row+1, col+1).isMine()) count++
        }

        if (col>0 && cell(row, col-1).isMine()) count++
        if (col<gridSize-1 && cell(row, col+1).isMine()) count++

        cell(row, col).adjacentMines = count
    }

    private fun cell(i: Int, j: Int): CustomCellView {
        return getChildAt((i * columnCount) + j) as CustomCellView
    }

    private fun handleCellLongClick(cellView: CustomCellView, row: Int, col: Int) {
        cellView.toggleFlag()
    }

    fun setMines(mines: MutableSet<Pair<Int, Int>>) {
        mines.forEach{
            cell(it.first, it.second).plantMine()
        }
    }
}
