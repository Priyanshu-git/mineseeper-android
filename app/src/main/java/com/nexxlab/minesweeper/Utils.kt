package com.nexxlab.minesweeper

import com.nexxlab.minesweeper.ui.CustomCellView

fun CustomCellView.isMine(): Boolean {
    return isMine
}

fun CustomCellView.isCovered(): Boolean {
    return cellState == CustomCellView.CellState.COVERED
}

fun CustomCellView.isRevealed(): Boolean {
    return cellState == CustomCellView.CellState.REVEALED
}

fun CustomCellView.isFlagged(): Boolean {
    return cellState == CustomCellView.CellState.FLAGGED
}