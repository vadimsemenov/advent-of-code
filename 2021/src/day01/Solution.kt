package day01

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun part1(input: Input): Int {
    return input
      .zipWithNext()
      .count { (prev, next) -> next > prev }
  }

  fun part2(input: Input): Int {
    return input
      .zip(input.drop(3))
      .count { (prev, next) -> next > prev }
  }

  check(part1(readInput("test-input.txt")) == 7)
  check(part2(readInput("test-input.txt")) == 5)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day01/$s")).readLines().map { it.toInt() }
}

private typealias Input = List<Int>