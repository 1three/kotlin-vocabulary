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
    private var selectedWord: Word? = null
    private val updateWordListResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val isUpdated = result.data?.getBooleanExtra("isUpdated", false) ?: false

            if (result.resultCode == RESULT_OK && isUpdated) {
                updateWordList()
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
                updateWordListResult.launch(it)
            }
        }

        binding.deleteButton.setOnClickListener {
            deleteItem()
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

    private fun updateWordList() {
        Thread {
            AppDatabase.getInstance(this)?.wordDao()?.getLatestItem()?.let { word ->
                wordAdapter.list.add(0, word)
                runOnUiThread {
                    wordAdapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    private fun deleteItem() {
        if (selectedWord == null) {
            Toast.makeText(this, "삭제할 단어를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        Thread {
            selectedWord?.let { word ->
                AppDatabase.getInstance(this)?.wordDao()?.deleteItem(word)
                runOnUiThread {
                    wordAdapter.list.remove(word)
                    wordAdapter.notifyDataSetChanged()
                    binding.wordTextView.text = ""
                    binding.meanTextView.text = ""
                    Toast.makeText(this, "단어가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    override fun onClick(word: Word) {
        selectedWord = word
        binding.wordTextView.text = word.word
        binding.meanTextView.text = word.mean
    }
}