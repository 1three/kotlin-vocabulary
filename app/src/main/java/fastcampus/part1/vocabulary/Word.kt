package fastcampus.part1.vocabulary

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * data class
 *
 * 1. Data 홀딩하기 위한 클래스
 * 2. 하나 이상의 프로터피 필요
 * 3. 상속 불가
 * 4. toString, hashCode,equals 등 기본적으로 선언된 상태
 * */

@Entity(tableName = "word")
data class Word(
    val word: String,
    val mean: String,
    val type: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
