package day13

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun log(field: Array<BooleanArray>) = buildString {
    for (y in field[0].indices) {
      for (x in field.indices) {
        append(if (field[x][y]) '#' else '.')
      }
      append('\n')
    }
  }.let { System.err.println(it) }

  fun fold(field: Array<BooleanArray>, fold: Pair<Boolean, Int>): Array<BooleanArray> =
    if (fold.first) {
      val foldX = fold.second
      check(field[foldX].none { it })
      val maxX = maxOf(foldX, field.size - 1 - foldX)
      Array(maxX) { xx ->
        BooleanArray(field[xx].size) { yy ->
          val x1 = foldX - (maxX - xx)
          val x2 = foldX + (maxX - xx)
          (x1 >= 0 && field[x1][yy]) || (x2 < field.size && field[x2][yy])
        }
      }
    } else {
      val foldY = fold.second
      val maxY = maxOf(foldY, field[0].size - foldY - 1)
      check(field.none { it[foldY] })
      Array(field.size) { xx ->
        BooleanArray(maxY) { yy ->
          val y1 = foldY - (maxY - yy)
          val y2 = foldY + (maxY - yy)
          (y1 >= 0 && field[xx][y1]) || (y2 < field[xx].size && field[xx][y2])
        }
      }
    }

  fun part1(input: Input): Int {
    val maxX = input.first.maxOf { it.first }
    val maxY = input.first.maxOf { it.second }
    val field = Array(maxX + 1) {
      BooleanArray(maxY + 1)
    }
    for ((x, y) in input.first) {
      field[x][y] = true
    }

    return fold(field, input.second.first()).sumOf { it.count { it } }
  }

  fun part2(input: Input): Int {
    val maxX = input.first.maxOf { it.first }
    val maxY = input.first.maxOf { it.second }
    var field = Array(maxX + 1) {
      BooleanArray(maxY + 1)
    }
    for ((x, y) in input.first) {
      field[x][y] = true
    }

    for (fold in input.second) {
      field = fold(field, fold)
    }
    log(field)
    return 42
  }

  check(part1(readInput("test-input.txt")) == 17)
  check(part2(readInput("test-input.txt")) == 42)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day13/$s")).readLines().let { lines ->
    val coordinates = lines
      .filter { !it.startsWith("fold along ") }
      .filter { it.isNotBlank() }
      .map {
        val (x, y) = it.split(",").map { it.toInt() }
        Pair(x, y)
      }
    val folds = lines
      .filter { it.startsWith("fold along ") }
      .map {
        val (axis, coordinate) = it.substring("fold along ".length).split("=")
        val axisRes = when(axis) {
          "x" -> true
          "y" -> false
          else -> error("unknown axis: $axis")
        }
        Pair(axisRes, coordinate.toInt())
      }
    Pair(coordinates, folds)
  }
}

private typealias Input = Pair<List<Pair<Int, Int>>, List<Pair<Boolean, Int>>>