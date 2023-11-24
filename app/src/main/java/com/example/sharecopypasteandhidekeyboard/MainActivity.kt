package com.example.sharecopypasteandhidekeyboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.sharecopypasteandhidekeyboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shareBtn.setOnClickListener{
            if (binding.editText.text.toString().isNotEmpty()){
                shareMsg(binding.editText.text.toString())
            }else{
                binding.editText.error = "Required"
            }
        }
        binding.copyBtn.setOnClickListener {
            if (binding.editText.text.toString().isNotEmpty()){
                copyToClipBoard(binding.editText.text.toString())
            }else{
                binding.editText.error = "Required"
            }
        }
        binding.pastBtn.setOnClickListener {
            binding.editText.setText(pasteFromClipBoard())
        }
        binding.hideKeyboardBtn.setOnClickListener { 
            hideKeyboard(it)
        }
    }

    private fun hideKeyboard(it: View) {
        try {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun pasteFromClipBoard(): String {
        val clipBoard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        return clipBoard.primaryClip!!.getItemAt(0).text.toString()
    }

    private fun copyToClipBoard(message: String) {
        val clipBoard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", message)
        clipBoard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied", Toast.LENGTH_LONG).show()
    }

    private fun shareMsg(message: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(intent, "Share Message"))
    }
}