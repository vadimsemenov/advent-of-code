package day10

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  val matching = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
  )

  fun part1(lines: Input): Int {
    val points = mapOf(
      ')' to 3,
      ']' to 57,
      '}' to 1197,
      '>' to 25137,
    )
    return lines.sumOf {
      val stack = mutableListOf<Char>()
      for (elem in it) {
        if (matching.containsKey(elem)) {
          stack.add(elem)
        } else {
          if (matching[stack.last()] != elem) {
            return@sumOf points[elem]!!
          } else {
            stack.removeAt(stack.lastIndex)
          }
        }
      }
      0
    }
  }

  fun part2(lines: Input): Long {
    val points = mapOf(
      ')' to 1,
      ']' to 2,
      '}' to 3,
      '>' to 4,
    )
    return lines.map { line ->
      val stack = mutableListOf<Char>()
      for (elem in line) {
        if (matching.containsKey(elem)) {
          stack.add(elem)
        } else {
          if (matching[stack.last()] != elem) {
            return@map 0
          } else {
            stack.removeAt(stack.lastIndex)
          }
        }
      }
      stack.reversed().map { matching[it] }.fold(0L) { acc, c -> acc * 5 + points[c]!! }
    }.filter { it != 0L }.sorted().let {
      it[it.size / 2]
    }
  }

  check(part1(readInput("test-input.txt")) == 26397)
  check(part2(readInput("test-input.txt")) == 288957L)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day10/$s")).readLines()
}

private typealias Input = List<String>