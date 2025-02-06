package com.nexxlab.minesweeper

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.GridLayout
import android.widget.Toast
import com.nexxlab.minesweeper.data.SharedPrefs
import com.nexxlab.minesweeper.ui.CustomCellView
import com.nexxlab.minesweeper.viewmodel.MSViewModel

class MineGridLayout(
    context: Context,
    attrs: AttributeSet? = null
) : GridLayout(context, attrs) {

    private var gridSize = 0
    private val sharedPref = SharedPrefs.getInstance()
    var minesCreated= false
    val mineCount = sharedPref.mineCount
    private var viewModel : MSViewModel? = null
    private val TAG = "MineGridLayout"

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
            handleCellClick(row, col)
        }
        cellView.setOnLongClickListener {
            handleCellLongClick(cellView, row, col)
            true
        }
    }

    private fun handleCellClick(row: Int, col: Int) {
        if (!minesCreated) {
            setupMines(row, col)
            minesCreated = true
        }

        val clickedCell = cell(row, col)

        if (clickedCell.isFlagged()) return

        if (clickedCell.isMine()) {
            revealAllMines()
            Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show()
            return
        }

        uncoverCells(row, col)
    }

    private fun uncoverCells(row: Int, col: Int) {
        if (row !in 0 until gridSize || col !in 0 until gridSize) return

        val currentCell = cell(row, col)
        if (!currentCell.isCovered()) return

        val mineCount = countAdjacentMines(row, col)
        currentCell.adjacentMines = mineCount

        if (mineCount == 0) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    if (dx == 0 && dy == 0) continue
                    uncoverCells(row + dx, col + dy)
                }
            }
        }
    }

    private fun countAdjacentMines(row: Int, col: Int): Int {
        var count = 0
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx == 0 && dy == 0) continue // Skip self
                val newRow = row + dx
                val newCol = col + dy
                if (newRow in 0 until gridSize && newCol in 0 until gridSize
                    && cell(newRow, newCol).isMine()) {
                    count++
                }
            }
        }
        return count
    }

    private fun revealAllMines() {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                val currentCell = cell(i, j)
                if (currentCell.isMine()) {
                    currentCell.revealMine()
                }
            }
        }
    }


    private fun cell(i: Int, j: Int): CustomCellView {
        return getChildAt((i * columnCount) + j) as CustomCellView
    }

    private fun handleCellLongClick(cellView: CustomCellView, row: Int, col: Int) {
        if (!cellView.isRevealed()) {
            if (!cellView.isFlagged() && sharedPref.mineCount <= viewModel!!.flags.value.size) return
            viewModel?.updateFlag(row, col)
            cellView.toggleFlag()
        }
    }

    private fun setupMines(row: Int, col:Int) {
        val mines = mutableListOf<Pair<Int, Int>>()
        val random = java.util.Random()

        while (mines.size < mineCount) {
            val i = random.nextInt(gridSize)
            val j = random.nextInt(gridSize)
            if (i != row && j != col)
                mines.add(i to j)
        }
        Log.i(TAG, "setupMines ${mines}")
        mines.forEach{
            cell(it.first, it.second).plantMine()
        }
        viewModel?.setMines(mines)
    }

    fun reset() {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                cell(i, j).reset()
            }
        }
        minesCreated = false
    }

    fun setViewModel(viewModel: MSViewModel) {
        this.viewModel = viewModel
    }
}
