package fastcampus.part1.vocabulary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var wordAdapter: WordApater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dummyList = mutableListOf<Word>(
            Word("Weather", "날씨", "명사"),
            Word("Honey", "꿀", "명사"),
            Word("Run", "실행하다", "동사")
        )

        wordAdapter = WordApater(dummyList)
        binding.wordRecyclerView.apply {
            adapter = wordAdapter // RecyclerView - Adapter 연결
            layoutManager =
                LinearLayoutManager(
                    applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                ) // LayoutManager 지정

            // itemView의 구분자 생성 (UI, xml 넣어도 무관)
            val dividerItemDecoration =
                DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }
}