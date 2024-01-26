package fastcampus.part1.vocabulary

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.part1.vocabulary.databinding.ItemWordBinding

// Adapter : Data Collection 필요
class WordApater(
    val list: MutableList<Word>,
    private val itemClickListener: ItemClickListener? = null
) :
    RecyclerView.Adapter<WordApater.WordViewHolder>() {

    // onCreateViewHolder : ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemWordBinding.inflate(inflater, parent, false)
        return WordViewHolder(binding)
    }

    // onBindViewHolder : UI - Data 연결
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = list[position]
        holder.bind(word)
        holder.itemView.setOnClickListener { itemClickListener?.onClick(word) }
    }

    // getItemCount : Adapter 가진 Data 개수 반환
    override fun getItemCount(): Int {
        return list.size
    }

    // ViewHolder : 화면에 그려질 View 보유
    class WordViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.apply {
                wordTextView.text = word.word
                meanTextView.text = word.mean
                typeChip.text = word.type
            }
        }
    }

    interface ItemClickListener {
        fun onClick(word: Word)
    }
}