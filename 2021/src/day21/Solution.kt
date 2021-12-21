package day21

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun part1(input: Input): Int {
    var rolled = 0
    val score = intArrayOf(0, 0)
    val position = input.map { it - 1 }.toIntArray()
    var current = 0
    while (score.all { it < 1000 }) {
      var move = 0
      repeat(3) {
        move += (rolled++) % 100 + 1
      }
      position[current] = (position[current] + move) % 10
      score[current] += position[current] + 1
      current = 1 - current
    }
    return rolled * score.find { it < 1000 }!!
  }

  fun part2(input: Input): Long {
    val mem = Array(10) {
      Array(10) {
        Array(21) {
          arrayOfNulls<Pair<Long, Long>?>(21)
        }
      }
    }

    fun rec(
      firstPosition: Int,
      secondPosition: Int,
      firstScore: Int,
      secondScore: Int,
    ): Pair<Long, Long> {
      check(firstScore < 21)
      if (secondScore >= 21) return Pair(0L, 1L)
      mem[firstPosition][secondPosition][firstScore][secondScore]?.let { return it }

      var firstWins = 0L
      var secondWins = 0L
      for (i in 1..3) {
        for (j in 1..3) {
          for (k in 1..3) {
            val newPosition = (firstPosition + (i + j + k)) % 10
            val (second, first) = rec(
              firstPosition = secondPosition,
              secondPosition = newPosition,
              firstScore = secondScore,
              secondScore = firstScore + newPosition + 1
            )
            firstWins += first
            secondWins += second
          }
        }
      }
      return Pair(firstWins, secondWins).also {
        mem[firstPosition][secondPosition][firstScore][secondScore] = it
      }
    }

    val (first, second) = rec(
      firstPosition = input[0] - 1,
      secondPosition = input[1] - 1,
      firstScore = 0,
      secondScore = 0
    )
    return maxOf(first, second)
  }

  check(part1(readInput("test-input.txt")) == 739785)
  check(part2(readInput("test-input.txt")) == 444356092776315L)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}

private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day21/$s")).readLines().mapIndexed { index, line ->
    check(line.startsWith("Player ${index + 1} starting position: "))
    line.substring("Player ${index + 1} starting position: ".length).toInt()
  }
}

private typealias Input = List<Int>