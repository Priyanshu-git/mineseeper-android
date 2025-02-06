package com.nexxlab.minesweeper.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.nexxlab.minesweeper.R

class CustomCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
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

    private val coveredImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.tile_covered)
    private val flaggedImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.tile_flagged)
    private val mineImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.tile_bomb)
    private val tileMap = mapOf(
        0 to BitmapFactory.decodeResource(resources, R.drawable.tile_0),
        1 to BitmapFactory.decodeResource(resources, R.drawable.tile_1),
        2 to BitmapFactory.decodeResource(resources, R.drawable.tile_2),
        3 to BitmapFactory.decodeResource(resources, R.drawable.tile_3),
        4 to BitmapFactory.decodeResource(resources, R.drawable.tile_4),
        5 to BitmapFactory.decodeResource(resources, R.drawable.tile_5),
        6 to BitmapFactory.decodeResource(resources, R.drawable.tile_6),
        7 to BitmapFactory.decodeResource(resources, R.drawable.tile_7),
        8 to BitmapFactory.decodeResource(resources, R.drawable.tile_8)
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cellSize = width.coerceAtMost(height).toFloat()
        when (cellState) {
            CellState.COVERED -> {
                drawBitmap(canvas, coveredImage, cellSize)
            }
            CellState.REVEALED -> {
                if (isMine)
                    drawBitmap(canvas, mineImage, cellSize)
                else
                    drawBitmap(canvas, tileMap[adjacentMines]!!, cellSize)
            }
            CellState.FLAGGED -> {
                drawBitmap(canvas, flaggedImage, cellSize)
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
