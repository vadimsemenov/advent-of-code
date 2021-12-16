package day03

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun part1(numbers: List<String>): Int {
    val count = IntArray(numbers[0].length)
    for (num in numbers) {
      for ((index, bit) in num.withIndex()) {
        count[index] += if (bit == '1') 1 else 0
      }
    }
    val gamma = count
      .map { if (it + it > numbers.size) 1 else 0 }
      .joinToString("") { it.toString() }
      .toInt(2)
    val epsilon = count
      .map { if (it + it > numbers.size) 0 else 1 }
      .joinToString("") { it.toString() }
      .toInt(2)
    return gamma * epsilon
  }
  fun part2(report: List<String>): Int {
    tailrec fun filter(list: List<String>, position: Int, predicate: (char: Char, mostCommon: Char) -> Boolean): String {
      if (list.size == 1) return list.first()
      val sum = list.sumOf { it[position].code - '0'.code }
      val mostCommon = if (sum + sum >= list.size) '1' else '0'
      return filter(
        list.filter {
          predicate(it[position], mostCommon)
        },
        position + 1,
        predicate
      )
    }

    val oxygenGeneratorRating = filter(report, 0) { char, mostCommon ->
      char == mostCommon
    }.toInt(2)
    val co2ScrubberRating = filter(report, 0) { char, mostCommon ->
      char != mostCommon
    }.toInt(2)
    return oxygenGeneratorRating * co2ScrubberRating
  }

  check(part1(readInput("test-input.txt")) == 198)
  check(part2(readInput("test-input.txt")) == 230)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}

private fun readInput(s: String): List<String> {
  return Files.newBufferedReader(Paths.get("src/day03/$s")).readLines().filterNot { it.isBlank() }.also { list ->
    check(list.all { it.length == list[0].length })
  }
}