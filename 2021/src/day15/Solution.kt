package day15

import java.nio.file.Files
import java.nio.file.Paths
import java.util.TreeSet
import kotlin.system.measureTimeMillis

fun main() {
  fun part1(input: Input): Int {
    val costs = Array(input.size) {
      IntArray(input[it].size) { Int.MAX_VALUE }
    }
    val priorityQueue = TreeSet(
      Comparator
        .comparingInt<Pair<Int, Int>> { costs[it.first][it.second] }
        .thenComparingInt { it.first }
        .thenComparingInt { it.second }
    )
    costs[0][0] = 0
    priorityQueue.add(0 to 0)
    val dx = arrayOf(-1, 0, 1, 0)
    val dy = arrayOf(0, 1, 0, -1)
    while (priorityQueue.isNotEmpty()) {
      val (x, y) = priorityQueue.pollFirst()!!
      if (x == input.lastIndex && y == input[x].lastIndex) {
        return costs[x][y]
      }
      for (d in 0 until 4) {
        val xx = x + dx[d]
        val yy = y + dy[d]
        if (xx in input.indices && yy in input[xx].indices) {
          val newCost = costs[x][y] + input[xx][yy]
          if (newCost < costs[xx][yy]) {
            priorityQueue.remove(xx to yy)
            costs[xx][yy] = newCost
            priorityQueue.add(xx to yy)
          }
        }
      }
    }
    error("Polundra!")
  }

  fun part2(input: Input): Int {
    val newInput = List(input.size * 5) { xx ->
      val x = xx % input.size
      List(input[x].size * 5) { yy ->
        val y = yy % input[x].size
        val diagonal = xx / input.size + yy / input[x].size
        (input[x][y] - 1 + diagonal) % 9 + 1
      }
    }
    return part1(newInput)
  }

  check(part1(readInput("test-input.txt")) == 40)
  check(part2(readInput("test-input.txt")) == 315)

  val timeSpent = measureTimeMillis {
    println(part1(readInput("input.txt")))
    println(part2(readInput("input.txt")))
  }
  System.err.println("In ${timeSpent}ms")
}

private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day15/$s")).readLines().map { it.map { it.code - '0'.code } }
}

private typealias Input = List<List<Int>>