package com.nexxlab.minesweeper.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.nexxlab.minesweeper.R
import com.nexxlab.minesweeper.data.Constants

class CustomCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val tileMap: Map<Int, Bitmap>
) : View(context, attrs, defStyleAttr) {

    enum class CellState {
        COVERED, REVEALED, FLAGGED
    }

    var cellState: CellState = CellState.COVERED
    var adjacentMines: Int = 0
        set(value) {
            field = value
            cellState = CellState.REVEALED
            invalidate()
        }
    var isMine = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cellSize = width.coerceAtMost(height).toFloat()
        when (cellState) {
            CellState.COVERED -> {
                drawBitmap(canvas, tileMap[Constants.INDEX_COVERED]!!, cellSize)
            }
            CellState.REVEALED -> {
                if (isMine)
                    drawBitmap(canvas, tileMap[Constants.INDEX_BOMB]!!, cellSize)
                else
                    drawBitmap(canvas, tileMap[adjacentMines]!!, cellSize)
            }
            CellState.FLAGGED -> {
                drawBitmap(canvas, tileMap[Constants.INDEX_FLAGGED]!!, cellSize)
            }
        }
    }

    private fun drawBitmap(canvas: Canvas, bitmap: Bitmap, size: Float) {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, size.toInt(), size.toInt(), false)
        canvas.drawBitmap(scaledBitmap, 0f, 0f, null)
    }

    // Optional: method to toggle flag on long press
    fun toggleFlag() {
        cellState = if (cellState == CellState.FLAGGED) CellState.COVERED else CellState.FLAGGED
        invalidate()
    }

    fun plantMine() {
        isMine = true
    }

    fun revealMine() {
        cellState = CellState.REVEALED
        invalidate()
    }

    fun reset() {
        cellState = CellState.COVERED
        isMine = false
        invalidate()
    }
}
