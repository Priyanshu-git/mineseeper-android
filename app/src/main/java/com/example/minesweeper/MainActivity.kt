package com.example.minesweeper

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minesweeper.databinding.ActivityMainBinding
import com.example.minesweeper.ui.CustomCellView

class MainActivity : AppCompatActivity() {
    private lateinit var customGridLayout: MineGridLayout
    private val gridSize = 8 // Define the grid size (8x8, 10x10, etc.)
    lateinit var binding: ActivityMainBinding
    val minesCreated= false
    val  mineCount = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        customGridLayout = binding.mineGrid.apply {
            setGridSize(gridSize)
        }

        setupClickHandling()
    }

    private fun setupClickHandling() {
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val cellView = customGridLayout.getChildAt(row * gridSize + col) as? CustomCellView
                cellView?.setOnClickListener {
                    if (!minesCreated) setupMines(row, col)
                    handleCellClick(row, col)
                }
                cellView?.setOnLongClickListener {
                    handleCellLongClick(row, col)
                    true
                }
            }
        }
    }

    private fun setupMines(row: Int, col:Int) {
        val mines = mutableSetOf<Pair<Int, Int>>()
        val random = java.util.Random()

        while (mines.size < mineCount) {
            val i = random.nextInt(gridSize)
            val j = random.nextInt(gridSize)
            if (i != row && j != col)
                mines.add(i to j)
        }
        customGridLayout.setMines(mines)
    }

    private fun handleCellClick(row: Int, col: Int) {
        val cellView = customGridLayout.getChildAt(row * gridSize + col) as? CustomCellView
        cellView?.uncoverCell()

        // Example: Check if the cell contains a mine
        if (cellView?.cellState == CustomCellView.CellState.MINE) {
            Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleCellLongClick(row: Int, col: Int) {
        val cellView = customGridLayout.getChildAt(row * gridSize + col) as? CustomCellView
        cellView?.toggleFlag()
    }
}