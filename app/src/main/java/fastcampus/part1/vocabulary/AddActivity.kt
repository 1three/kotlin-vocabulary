package fastcampus.part1.vocabulary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.chip.Chip
import fastcampus.part1.vocabulary.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
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
}