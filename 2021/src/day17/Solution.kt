package day17

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
  fun part1(input: Input): Int {
    val (xRange, yRange) = input.toList().map { it.first..it.second }
    require(xRange.first > 0 && xRange.last >= xRange.first)
    require(yRange.last < 0 && yRange.last >= yRange.first)
    // assume we can find X such that X * (X + 1) / 2 in xRange and X <= Y * 2 + 1
    // works for my test case, YMMW
    val y = yRange.first.absoluteValue - 1
    return y * (y + 1) / 2
  }

  fun part2(input: Input): Int {
    val (xRange, yRange) = input.toList().map { it.first..it.second }
    require(xRange.first > 0 && xRange.last >= xRange.first)
    require(yRange.last < 0 && yRange.last >= yRange.first)
    var answer = 0
    for (xv in 1..xRange.last) {
      for (yv in yRange.first..yRange.first.absoluteValue) {
        var (cx, cy) = 0 to 0
        var (cxv, cyv) = xv to yv
        while (true) {
          cx += cxv.also { cxv -= cxv.sign }
          cy += cyv--
          if (cx in xRange && cy in yRange) {
            answer++
            break
          }
          if (cx > xRange.last || cy < yRange.first) break
        }
      }
    }
    return answer
  }

//  illustrate(7, 2, readInput("test-input.txt"))
//  illustrate(6, 3, readInput("test-input.txt"))
//  illustrate(9, 0, readInput("test-input.txt"))
//  illustrate(17, -4, readInput("test-input.txt"))
//  illustrate(6, 9, readInput("test-input.txt"))
//  illustrate(6, 10, readInput("test-input.txt"))

  check(part1(readInput("test-input.txt")) == 45)
  check(part2(readInput("test-input.txt")) == 112)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}

private fun illustrate(xv: Int, yv: Int, target: Input) {
  val (txRange, tyRange) = target
  val tx = txRange.first..txRange.second
  val ty = tyRange.first..tyRange.second
  var (cx, cy) = 0 to 0
  var (cxv, cyv) = xv to yv
  val visited = mutableListOf<Pair<Int, Int>>()
  while (cx <= tx.last && cy >= ty.last) {
    cx += cxv.also { cxv -= cxv.sign }
    cy += cyv--
    visited.add(cx to cy)
  }
  val minX = minOf(minOf(0, tx.first), visited.minOf { it.first })
  val maxX = maxOf(maxOf(0, tx.last), visited.maxOf { it.first })
  val minY = minOf(minOf(0, ty.first), visited.minOf { it.second })
  val maxY = maxOf(maxOf(0, ty.last), visited.maxOf { it.second })
  val output = Array(maxY - minY + 1) {
    CharArray(maxX - minX + 1) { '.' }
  }
  for (x in tx) {
    for (y in ty) {
      output[maxY - y][x - minX] = 'T'
    }
  }
  output[maxY - 0][0 - minX] = 'S'
  for ((x, y) in visited) {
    output[maxY - y][x - minX] = '#'
  }
  println("($xv, $yv) -> $target")
  println(output.joinToString("\n") { String(it) })
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day17/$s")).readLine()!!.let { line ->
    val (xRange, yRange) = line.substring("target area: ".length).split(", ")
    val (xMin, xMax) = xRange.substring("x=".length).split("..").map { it.toInt() }
    val (yMin, yMax) = yRange.substring("y=".length).split("..").map { it.toInt() }
    Pair(xMin to xMax, yMin to yMax)
  }
}

private typealias Input = Pair<Pair<Int, Int>, Pair<Int, Int>>