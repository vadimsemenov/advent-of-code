package day07

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs

fun main() {
  fun part1(input: Input): Int {
    val from = input.minOrNull()!!
    val to = input.maxOrNull()!!
    return (from..to).minOf { target ->
      input.sumOf { abs(it - target) }
    }
  }

  fun part2(input: Input): Int {
    val from = input.minOrNull()!!
    val to = input.maxOrNull()!!
    return (from..to).minOf { target ->
      input.sumOf {
        val d = abs(it - target)
        d * (d + 1) / 2
      }
    }
  }

  check(part1(readInput("test-input.txt")) == 37)
  check(part2(readInput("test-input.txt")) == 168)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day07/$s")).readLines().first().split(",").map { it.toInt() }
}

private typealias Input = List<Int>