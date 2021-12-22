package day22

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

fun main() {
  fun MutableList<Int>.inPlaceDistinct() {
    sort()
    var ptr = 1
    for (i in 1 until this.size) {
      if (this[i] != this[i - 1]) {
        this[ptr++] = this[i]
      }
    }
    while (size > ptr) {
      removeLast()
    }
  }

  fun solve(input: Input): Long {
    val all = Array(3) { mutableListOf<Int>() }
    for ((_, cuboid) in input) {
      for (i in cuboid.indices) {
        all[i].addAll(cuboid[i])
      }
    }
    all.forEach { it.inPlaceDistinct() }
    val (xs, ys, zs) = all
    val state = Array(xs.size - 1) { Array(ys.size - 1) { BooleanArray(zs.size - 1) } }
    for ((on, cuboid) in input) {
      val (xRange, yRange, zRange) = cuboid.mapIndexed { index, interval ->
        val (from, to) = interval.map { all[index].binarySearch(it) }
        from until to
      }
      for (x in xRange) {
        for (y in yRange) {
          for (z in zRange) {
            state[x][y][z] = on
          }
        }
      }
    }
    var answer = 0L
    for (i in state.indices) {
      for (j in state[i].indices) {
        for (k in state[i][j].indices) {
          if (state[i][j][k]) {
            answer += 1L * (xs[i + 1] - xs[i]) * (ys[j + 1] - ys[j]) * (zs[k + 1] - zs[k])
          }
        }
      }
    }
    return answer
  }

  fun part1(input: Input): Int {
    return solve(
      input.filter { (_, cuboid) ->
        cuboid.all { (from, to) -> from in -50..50 && to in -49..51 }
      }
    ).toInt()
  }

  fun part2(input: Input): Long {
    return solve(input)
  }

  check(part1(readInput("test-input.txt")) == 590784)
  check(part2(readInput("test-input2.txt")) == 2758514936282235L)

  val millis = measureTimeMillis {
    println(part1(readInput("input.txt")))
    println(part2(readInput("input.txt")))
  }
  System.err.println("Done in $millis ms")
}

private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day22/$s")).readLines().map { line ->
    val on = line.startsWith("on ")
    if (!on) check(line.startsWith("off "))
    val list = line.substring(if (on) 3 else 4).split(",").mapIndexed { index, range ->
      check(range.matches("${"xyz"[index]}=-?\\d+..-?\\d+".toRegex()))
      val (from, to) = range.substring(2).split("..").map { it.toInt() }
      listOf(from, to + 1)
    }
    Pair(on, list)
  }
}

private typealias Input = List<Pair<Boolean, List<List<Int>>>>