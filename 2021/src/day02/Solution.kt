package day02

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun part1(commands: List<Pair<String, Long>>): Long {
    var horizontal = 0L
    var depth = 0L
    for ((direction, qty) in commands) {
      when (direction) {
        "forward" -> horizontal += qty
        "down" -> depth += qty
        "up" -> depth -= qty
        else -> error("Unexpected '$direction'")
      }
    }
    return horizontal * depth
  }
  fun part2(commands: List<Pair<String, Long>>): Long {
    var horizontal = 0L
    var depth = 0L
    var aim = 0L
    for ((direction, qty) in commands) {
      when (direction) {
        "forward" -> {
          horizontal += qty
          depth += qty * aim
        }
        "down" -> aim += qty
        "up" -> aim -= qty
        else -> error("Unexpected '$direction'")
      }
    }
    return horizontal * depth
  }

  check(part1(readInput("test-input.txt")) == 150L)
  check(part2(readInput("test-input.txt")) == 900L)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}

private fun readInput(s: String): List<Pair<String, Long>> {
  return Files.newBufferedReader(Paths.get("src/day02/$s")).readLines().map {
    val (direction, qty) = it.split(" ")
    direction to qty.toLong()
  }
}