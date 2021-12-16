package day11

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun iterate(octopuses: Input): Int {
    var flashes = 0
    val queue = mutableListOf<Pair<Int, Int>>()
    val flashed = Array(octopuses.size) { BooleanArray(octopuses[it].size) }
    for ((i, row) in octopuses.withIndex()) {
      for (j in row.indices) {
        row[j]++
        if (row[j] > 9) {
          flashed[i][j] = true
          queue.add(i to j)
        }
      }
    }
    var head = 0
    while (head < queue.size) {
      val (i, j) = queue[head++]
      octopuses[i][j] = 0
      flashes++
      for (di in -1..1) {
        for (dj in -1..1) {
          val ii = i + di
          val jj = j + dj
          if (ii !in octopuses.indices || jj !in octopuses[ii].indices) continue
          if (!flashed[ii][jj]) {
            octopuses[ii][jj]++
            if (octopuses[ii][jj] > 9) {
              flashed[ii][jj] = true
              queue.add(ii to jj)
            }
          }
        }
      }
    }
    return flashes
  }

  fun part1(octopuses: Input): Int {
    var flashes = 0
    repeat(100) {
      flashes += iterate(octopuses)
    }
    return flashes
  }

  fun part2(octopuses: Input): Int {
    repeat(100500) {
      val flashes = iterate(octopuses)
      if (flashes == 100) return it + 1
    }
    error("POLUNDRA")
  }

  check(part1(readInput("test-input.txt")) == 1656)
  check(part2(readInput("test-input.txt")) == 195)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day11/$s"))
    .readLines()
    .map {
      it
        .map { it.code - '0'.code }
        .toMutableList()
    }
}

private typealias Input = List<MutableList<Int>>