package day19

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

fun main() {
  val rotations = createRotations()

  fun fullMap(input: Input): MutableList<Pair<Point, List<Point>>> {
    val fixed = mutableListOf(Point(0, 0, 0) to input.first())
    val remaining = input.withIndex().drop(1).toMutableList()
    val tried = Array(input.size) { HashSet<Int>() }
    while (remaining.isNotEmpty()) {
      remaining.removeAll { (index, beacons) ->
        for ((scannerIndex, scannerAndBeacons) in fixed.withIndex()) {
          if (scannerIndex in tried[index]) continue
          tried[index] += scannerIndex
          for (rotation in rotations) {
            val rotated = beacons.map { it.rotate(rotation) }
            val (shift, frequency) = buildMap {
              for (a in rotated) {
                for (b in scannerAndBeacons.second) {
                  this.compute(b - a) { _, prev ->
                    1 + (prev ?: 0)
                  }
                }
              }
            }.maxByOrNull { it.value }!!
            if (frequency >= 12) {
              fixed += shift to rotated.map { it + shift }
              return@removeAll true
            }
          }
        }
        return@removeAll false
      }
    }
    return fixed
  }

  fun part1(input: Input): Int {
    return fullMap(input).map { it.second }.flatten().toSet().size
  }

  fun part2(input: Input): Int {
    val scanners = fullMap(input).map { it.first }
    return scanners.maxOf { a ->
      scanners.maxOf { b ->
        (a - b).magnitude()
      }
    }
  }

  check(part1(readInput("test-input.txt")) == 79)
  check(part2(readInput("test-input.txt")) == 3621)

  val millis = measureTimeMillis {
    println(part1(readInput("input.txt")))
    println(part2(readInput("input.txt")))
  }
  System.err.println("Done in $millis ms.")
}

private fun createRotations(): List<List<List<Int>>> {
  fun createMatrices(sin: Int, cos: Int): List<List<List<Int>>> = listOf(
    listOf( // Rx
      listOf(1,    0,   0),
      listOf(0,  cos, sin),
      listOf(0, -sin, cos),
    ),
    listOf( // Ry
      listOf(cos, 0, -sin),
      listOf(  0, 1,    0),
      listOf(sin, 0,  cos),
    ),
    listOf( // Rz
      listOf( cos, sin, 0),
      listOf(-sin, cos, 0),
      listOf(   0,   0, 1),
    ),
  )

  return listOf(
    1 to 0,
    0 to 1,
    -1 to 0,
    0 to -1,
  ).flatMap { (cos, sin) ->
    createMatrices(sin, cos)
  }.let {
    buildSet {
      for (a in it) for (b in it) add(a * b)
      check(size == 24)
    }.toList()
  }
}

private operator fun List<List<Int>>.times(other: List<List<Int>>): List<List<Int>> {
  require(this.first().size == other.size)
  return List(this.size) { i ->
    List(other.first().size) { j ->
      other.indices.sumOf { k ->
        this[i][k] * other[k][j]
      }
    }
  }
}

private data class Point(val x: Int, val y: Int, val z: Int)

private operator fun Point.plus(other: Point) =
  Point(x + other.x, y + other.y, z + other.z)

private operator fun Point.minus(other: Point) =
  Point(x - other.x, y - other.y, z - other.z)

private fun Point.rotate(rotation: List<List<Int>>): Point {
  val (x, y, z) = (listOf(listOf(x, y, z)) * rotation).first()
  return Point(x, y, z)
}

private fun Point.magnitude(): Int = x.absoluteValue + y.absoluteValue + z.absoluteValue

private fun readInput(s: String): Input {
  return buildList<MutableList<Point>> {
    Files.newBufferedReader(Paths.get("src/day19/$s")).forEachLine { line ->
      if (line == "--- scanner $size ---") {
        add(mutableListOf())
      } else if (line.isNotBlank()) {
        val (x, y, z) = line.split(",").map { it.toInt() }
        last().add(Point(x, y, z))
      }
    }
  }
}

private typealias Input = List<List<Point>>