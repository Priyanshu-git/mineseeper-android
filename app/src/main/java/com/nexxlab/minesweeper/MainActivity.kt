package com.nexxlab.minesweeper

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.nexxlab.minesweeper.data.SharedPrefs
import com.nexxlab.minesweeper.databinding.ActivityMainBinding
import com.nexxlab.minesweeper.viewmodel.MSViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var customGridLayout: MineGridLayout
    lateinit var binding: ActivityMainBinding
    val viewModel:MSViewModel by viewModels()
    private val TAG = "MainActivity"
    lateinit var sharedPref:SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sharedPref = SharedPrefs.getInstance().apply {
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
            setViewModel(viewModel)
        }
        binding.btnReset.setOnClickListener {
            customGridLayout.reset()
            viewModel.reset()
        }

        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.flags.collect{
                Log.i(TAG, "flagCount: $it")
                val count = sharedPref.mineCount - it.size
                binding.tvFlagCount.text = "$count"
                if (count == 0){
                    verifyFlags()
                }
            }
        }
    }

    private fun verifyFlags() {
        val flags = viewModel.flags.value
        val mines = viewModel.mines.value
        if (flags.size == mines.size && flags.containsAll(mines)){
            Toast.makeText(this, "You Won", Toast.LENGTH_SHORT).show()
            customGridLayout.isEnabled = false
        }
    }
}