package day08

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun part1(lines: Input): Int {
    return lines.sumOf { entry ->
      val (_, reading) = entry.split("|")
      reading.split(" ")
        .filter { it.isNotBlank() }
        .count { it.length in setOf(2, 3, 4, 7) }
    }
  }

  fun part2(lines: Input): Int {
    fun String.normalize() = String(this.toCharArray().sortedArray())
    fun decode(digits: List<String>): Array<String> {
      val map = arrayOfNulls<String>(10)
      map[1] = digits.find { it.length == 2 }
      map[7] = digits.find { it.length == 3 }
      map[4] = digits.find { it.length == 4 }
      map[8] = digits.find { it.length == 7 }

      val zeroSixOrNine = digits.filter { it.length == 6 }
      check(zeroSixOrNine.size == 3)
      map[6] = zeroSixOrNine.find { !it.toSet().containsAll(map[1]!!.toSet()) }
      map[9] = zeroSixOrNine.find { it.toSet().containsAll(map[4]!!.toSet()) }
      map[0] = zeroSixOrNine.find { it != map[6] && it != map[9] }

      val twoThreeOrFive = digits.filter { it.length == 5 }
      check(twoThreeOrFive.size == 3)
      map[3] = twoThreeOrFive.find { it.toSet().containsAll(map[1]!!.toSet()) }
      map[5] = twoThreeOrFive.find { map[6]!!.toSet().containsAll(it.toSet()) }
      map[2] = twoThreeOrFive.find { it != map[3] && it != map[5] }

      return map.requireNoNulls()
    }
    return lines.sumOf { entry ->
      val (digits, number) = entry.split("|")
      val map = decode(digits.split(" ").filter { it.isNotBlank() }.map { it.normalize() })
      number.split(" ")
        .filter { it.isNotBlank() }
        .map { it.normalize() }
        .map { map.indexOf(it) }
        .fold(0) { acc, i -> acc * 10 + i } as Int
    }
  }

  check(part1(readInput("test-input.txt")) == 26)
  check(part2(readInput("test-input.txt")) == 61229)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day08/$s")).readLines()
}

private typealias Input = List<String>