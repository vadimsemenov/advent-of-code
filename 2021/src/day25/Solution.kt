package day25

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

fun main() {
  fun part1(input: Input): Int {
    val field = input.map { it.toCharArray() }
    var moves = 0
    val herds = ">v"
    val di = intArrayOf(0, 1)
    val dj = intArrayOf(1, 0)
    val moved = arrayOf<ArrayList<Pair<Int, Int>>>(arrayListOf(), arrayListOf())
    do {
      moves++
      moved.forEach { it.clear() }
      repeat(2) { herd ->
        for (i in field.indices) {
          for (j in field[i].indices) {
            if (field[i][j] == herds[herd]) {
              val ii = (i + di[herd]) % field.size
              val jj = (j + dj[herd]) % field[ii].size
              if (field[ii][jj] == '.') {
                moved[herd].add(i to j)
              }
            }
          }
        }
        for ((i, j) in moved[herd]) {
          val ii = (i + di[herd]) % field.size
          val jj = (j + dj[herd]) % field[ii].size
          field[ii][jj] = field[i][j]
          field[i][j] = '.'
        }
      }
    } while (moved.any { it.isNotEmpty() })
    return moves
  }

  check(part1(readInput("test-input.txt")) == 58)

  val millis = measureTimeMillis {
    println(part1(readInput("input.txt")))
  }
  System.err.println("Done in $millis ms")
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day25/$s")).readLines()
}

private typealias Input = List<String>
