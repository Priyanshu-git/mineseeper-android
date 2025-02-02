package com.example.minesweeper

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minesweeper.data.SharedPrefs
import com.example.minesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var customGridLayout: MineGridLayout
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPref = SharedPrefs.getInstance().apply {
            setContext(this@MainActivity)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        customGridLayout = binding.mineGrid.apply {
            setGridSize(sharedPref.gridSize)
        }
        binding.btnReset.setOnClickListener {
            customGridLayout.reset()
        }
    }
}