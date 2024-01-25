package fastcampus.part1.vocabulary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

class MainActivity : AppCompatActivity(), WordApater.ItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordApater
    private val updateAddWordResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val isUpdated = result.data?.getBooleanExtra("isUpdated", false) ?: false

            if (result.resultCode == RESULT_OK && isUpdated) {
                updateAddWord()
            }
        }
    // registerForActivityResult (Activity 결과를 listen) ↔️ setResult (Add)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        binding.addButton.setOnClickListener {
            Intent(this, AddActivity::class.java).let {
                updateAddWordResult.launch(it)
            }
        }
    }

    private fun initRecyclerView() {
        wordAdapter = WordApater(mutableListOf(), this)
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

        Thread {
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll() ?: emptyList()
            // Thread.sleep(1000) // 1초 후 Data 로드 (화면에 list 출력 X)
            wordAdapter.list.addAll(list) // Adapter에 list 넣기 ≠ Data 로드
            runOnUiThread { // UI update이므로 runOnUiThread 내에서 선언
                wordAdapter.notifyDataSetChanged()// Adapter에 Data가 바뀜을 알림
            }
        }.start()
    }

    private fun updateAddWord() {
        Thread {
            AppDatabase.getInstance(this)?.wordDao()?.getLatestItem()?.let { word ->
                wordAdapter.list.add(0, word)
                runOnUiThread {
                    wordAdapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    override fun onClick(word: Word) {
        Toast.makeText(this, "${word.word}를 선택하였습니다.", Toast.LENGTH_SHORT).show()
    }
}