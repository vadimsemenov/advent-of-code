package day05

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.sign

fun main() {
  fun part1(lines: List<Line>): Int {
    val maxX = lines.maxOf { maxOf(it.first.first, it.second.first) }
    val maxY = lines.maxOf { maxOf(it.first.second, it.second.second) }
    val map = Array(maxX + 1) {
      IntArray(maxY + 1)
    }
    lines
      .filter { it.first.first == it.second.first || it.first.second == it.second.second }
      .forEach { (from, to) ->
        for (x in minOf(from.first, to.first) .. maxOf(from.first, to.first)) {
          for (y in minOf(from.second, to.second) .. maxOf(from.second, to.second)) {
            map[x][y]++
          }
        }
      }
    return map.sumOf { row -> row.count { it > 1 } }
  }
  fun part2(lines: List<Line>): Int {
    val maxX = lines.maxOf { maxOf(it.first.first, it.second.first) }
    val maxY = lines.maxOf { maxOf(it.first.second, it.second.second) }
    val map = Array(maxX + 1) {
      IntArray(maxY + 1)
    }
    for ((from, to) in lines) {
      val dx = (to.first - from.first).sign
      val dy = (to.second - from.second).sign
      var x = from.first
      var y = from.second
      map[x][y]++
      while (x != to.first || y != to.second) {
        x += dx
        y += dy
        map[x][y]++
      }
    }
    return map.sumOf { row -> row.count { it > 1 } }
  }

  check(part1(readInput("test-input.txt")) == 5)
  check(part2(readInput("test-input.txt")) == 12)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}


private fun readInput(s: String): List<Line> {
  return Files.newBufferedReader(Paths.get("src/day05/$s")).readLines().map {
    val (from, to) = it.split(" -> ")
    val (x1, y1) = from.split(",").map { it.toInt() }
    val (x2, y2) = to.split(",").map { it.toInt() }
    Pair(x1, y1) to Pair(x2, y2)
  }
}

typealias Point = Pair<Int, Int>
typealias Line = Pair<Point, Point>