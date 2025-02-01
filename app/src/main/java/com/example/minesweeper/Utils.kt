package com.example.minesweeper

import com.example.minesweeper.ui.CustomCellView

fun CustomCellView.isMine(): Boolean {
    return isMine
}

fun CustomCellView.isCovered(): Boolean {
    return cellState == CustomCellView.CellState.COVERED
}

fun CustomCellView.isUncovered(): Boolean {
    return cellState == CustomCellView.CellState.UNCOVERED
}