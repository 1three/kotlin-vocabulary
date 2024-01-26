package fastcampus.part1.vocabulary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.children
import com.google.android.material.chip.Chip
import fastcampus.part1.vocabulary.databinding.ActivityAddEditBinding

class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private var originWord: Word? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        binding.addButton.setOnClickListener {
            if (originWord == null) {
                addItem()
            } else {
                editItem()
            }
        }
    }

    private fun initViews() {
        val types = listOf("명사", "대명사", "동사", "형용사", "부사", "감탄사", "전치사", "접속사")
        binding.typeChipGroup.apply {
            types.forEach { text ->
                addView(createChip(text))
            }
        }

        originWord = intent.getParcelableExtra("originWord")
        originWord?.let { word ->
            binding.titleTextView.text = "단어 수정"
            binding.addButton.text = "수정하기"
            binding.wordInputEditText.setText(word.word)
            binding.meanInputEditText.setText(word.mean)
            val selectedChip = binding.typeChipGroup.children.firstOrNull {
                (it as Chip).text == word.type
            } as? Chip
            selectedChip?.isChecked = true
        }
    }

    private fun createChip(text: String): Chip {
        return Chip(
            this,
            null,
            com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
        ).apply {
            setText(text)
            isCheckable = true
            isClickable = true
        }
    }

    private fun addItem() {
        val word = binding.wordInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()
        val item = Word(word, mean, type)

        Thread {
            AppDatabase.getInstance(this)?.wordDao()?.insertItem(item)
            runOnUiThread {
                Toast.makeText(this, "단어가 저장되었습니다.", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent().putExtra("isUpdated", true)
            setResult(RESULT_OK, intent) // ↔️ registerForActivityResult(Main)
            finish()
        }.start()
    }

    private fun editItem() {
        val word = binding.wordInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()
        val editItem = originWord?.copy(word = word, mean = mean, type = type)

        Thread {
            editItem?.let { word ->
                AppDatabase.getInstance(this)?.wordDao()?.updateItem(editItem)
                val intent = Intent().putExtra("editWord", editItem)
                setResult(RESULT_OK, intent) // ↔️ registerForActivityResult(Main)
                runOnUiThread {
                    Toast.makeText(this, "단어가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }.start()
    }
}