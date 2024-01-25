package fastcampus.part1.vocabulary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fastcampus.part1.vocabulary.databinding.ActivityMainBinding

/**
 * 단어장 앱
 *
 * 1. 단어의 추가, 수정, 삭제
 * */

/**
 * Room (↔️ SharedPreferences)
 * RecyclerView - RecyclerViewAdapter
 * TextInputLayout - TextInputEditText
 * ChipGroup - Chip
 * data class
 * registerForActivityResult
 * Parcelize
 * Barrier
 * */

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}