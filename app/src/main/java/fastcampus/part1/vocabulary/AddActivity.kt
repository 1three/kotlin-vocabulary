package fastcampus.part1.vocabulary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.chip.Chip
import fastcampus.part1.vocabulary.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        binding.addButton.setOnClickListener {
            addItem()
        }
    }

    private fun initViews() {
        val types = listOf("명사", "대명사", "동사", "형용사", "부사", "감탄사", "전치사", "접속사")
        binding.typeChipGroup.apply {
            types.forEach { text ->
                addView(createChip(text))
            }
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
}