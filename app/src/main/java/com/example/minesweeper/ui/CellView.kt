package com.example.minesweeper.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.example.minesweeper.R

class CustomCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    enum class CellState {
        COVERED, UNCOVERED, FLAGGED, MINE
    }

    var cellState: CellState = CellState.COVERED
    var adjacentMines: Int = 0 // Number of adjacent mines, used when the cell is uncovered.
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

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (cellState == CellState.COVERED) {
                uncoverCell() // Custom logic to uncover the cell
                performClick() // Handle accessibility
                return true
            }
            return false
        }

        override fun onLongPress(e: MotionEvent) {
            if (cellState == CellState.COVERED) {
                toggleFlag() // Custom logic to flag the cell
            }
        }
    })

    init {
        setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cellSize = width.coerceAtMost(height).toFloat()
        when (cellState) {
            CellState.COVERED -> {
                drawBitmap(canvas, coveredImage, cellSize)
            }
            CellState.UNCOVERED -> {
                drawBitmap(canvas, tileMap[adjacentMines]!!, cellSize)
            }
            CellState.FLAGGED -> {
                drawBitmap(canvas, flaggedImage, cellSize)
            }
            CellState.MINE -> {
                drawBitmap(canvas, mineImage, cellSize)
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

    // Optional: method to uncover the cell
    fun uncoverCell() {
        if (cellState == CellState.COVERED) {
            cellState = CellState.UNCOVERED
            invalidate()
        }
    }

    fun plantMine() {
        isMine = true
    }
}
