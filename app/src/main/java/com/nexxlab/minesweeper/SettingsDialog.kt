package com.nexxlab.minesweeper

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.nexxlab.minesweeper.data.SharedPrefs
import com.nexxlab.minesweeper.databinding.SettingsDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsDialog(val callback: DialogCallback):DialogFragment() {
    private lateinit var binding: SettingsDialogBinding
    private lateinit var mContext: Context
    private val sharedPref = SharedPrefs.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = requireContext()
        binding = SettingsDialogBinding.inflate(LayoutInflater.from(mContext))

        dialog?.setOnShowListener {
            dialog?.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textBoxMines.setText(sharedPref.mineCount.toString())
        binding.textBoxTiles.setText(sharedPref.gridSize.toString())

        //add validation - only numbers
        binding.textBoxMines.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().toIntOrNull()
                if (input == null || input <= 1) {
                    binding.textBoxMines.error = "Number must be greater than 1"
                } else if (input > 0.8*sharedPref.gridSize*sharedPref.gridSize){
                    binding.textBoxMines.error = "Number must be less than ${(0.8*sharedPref.gridSize*sharedPref.gridSize).toInt()}"
                } else{
                    binding.textBoxMines.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.textBoxTiles.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().toIntOrNull()
                if (input == null || input <= 7 || input > 25) {
                    binding.textBoxTiles.error = "Number must be between 7 and 25"
                } else {
                    binding.textBoxTiles.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.btnSave.setOnClickListener {
            // Retrieve and validate inputs
            val mineCountStr = binding.textBoxMines.text.toString()
            val gridSizeStr = binding.textBoxTiles.text.toString()

            // Check if inputs are empty
            if (mineCountStr.isEmpty()) {
                binding.textBoxMines.error = "Field cannot be empty"
                return@setOnClickListener
            }

            if (gridSizeStr.isEmpty()) {
                binding.textBoxTiles.error = "Field cannot be empty"
                return@setOnClickListener
            }

            // Convert inputs to integers
            val mineCount = mineCountStr.toIntOrNull()
            val gridSize = gridSizeStr.toIntOrNull()

            // Validate input values
            if (gridSize == null || gridSize <= 7 || gridSize > 25) {
                binding.textBoxTiles.error = "Number must be between 7 and 25"
                return@setOnClickListener
            }

            if (mineCount == null || mineCount <= 1) {
                binding.textBoxMines.error = "Number must be greater than 1"
                return@setOnClickListener
            }

            val currentMineCount = sharedPref.mineCount
            val currentGridSize = sharedPref.gridSize
            sharedPref.mineCount = mineCount
            sharedPref.gridSize = gridSize

            lifecycleScope.launch(Dispatchers.Main) {
                callback.onDialogDismiss(minesChanged = mineCount != currentMineCount, gridSizeChanged = gridSize != currentGridSize)
            }
            Toast.makeText(mContext, "Settings saved", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.imgClose.setOnClickListener {
            dismiss()
        }

    }
}

interface DialogCallback{
    fun onDialogDismiss(gridSizeChanged: Boolean, minesChanged:Boolean)
}