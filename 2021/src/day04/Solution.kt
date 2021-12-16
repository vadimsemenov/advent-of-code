package day04

import java.io.BufferedReader
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun solveBoard(board: Board, sequence: List<Int>): Pair<Int, Int> {
    val rows = IntArray(5) { 5 }
    val cols = IntArray(5) { 5 }
    var sum = board.sumOf { it.sum() }
    for ((index, number) in sequence.withIndex()) {
      for (r in rows.indices) {
        for (c in cols.indices) {
          if (board[r][c] == number) {
            rows[r]--
            cols[c]--
            sum -= number
          }
        }
      }
      if (rows.any { it == 0 } || cols.any { it == 0 }) {
        return Pair(index, sum * number)
      }
    }
    error("Polundra!")
  }

  fun part1(input: Pair<List<Int>, List<Board>>): Int {
    val (sequence, boards) = input

    return boards
      .map { solveBoard(it, sequence) }
      .minByOrNull { it.first }!!
      .second
  }
  fun part2(input: Pair<List<Int>, List<Board>>): Int {
    val (sequence, boards) = input

    return boards
      .map { solveBoard(it, sequence) }
      .maxByOrNull { it.first }!!
      .second
  }

  check(part1(readInput("test-input.txt")) == 4512)
  check(part2(readInput("test-input.txt")) == 1924)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}

private fun readInput(s: String): Pair<List<Int>, List<Board>> {
  val reader = Files.newBufferedReader(Paths.get("src/day04/$s"))
  val sequence = reader.readLine()!!.split(",").map { it.toInt() }
  val boards = ArrayList<Board>()
  while (true) boards.add(
    List(5) {
      val row = reader.readNonBlankLine() ?: return Pair(sequence, boards)
      check(row.isNotBlank())
      row.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    }
  )
}

private fun BufferedReader.readNonBlankLine(): String? = readLine()?.ifBlank { readNonBlankLine() }

typealias Board = List<List<Int>>