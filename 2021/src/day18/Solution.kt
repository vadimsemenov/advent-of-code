package day18

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun part1(input: Input): Int {
    return input.reduce { acc, snailfish ->
      SnailfishPair(acc, snailfish).reduce()
    }.magnitude()
  }

  fun part2(input: Input): Int {
    return input.maxOf { lhs ->
      input.maxOf { rhs ->
        if (lhs != rhs) SnailfishPair(lhs, rhs).reduce().magnitude()
        else Int.MIN_VALUE
      }
    }
  }

  check(part1(readInput("test-input.txt")) == 4140)
  check(part2(readInput("test-input.txt")) == 3993)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}

private fun Snailfish.reduce(): Snailfish =
  reduceExplode(0)?.first?.reduce()
    ?: reduceSplit()?.reduce()
    ?: this

private fun Snailfish.reduceExplode(depth: Int): Triple<Snailfish, Int, Int>? {
  fun propagateRight(snailfish: Snailfish, add: Int): Snailfish = if (add == 0) snailfish else
    when (snailfish) {
      is SnailfishNumber -> SnailfishNumber(snailfish.value + add)
      is SnailfishPair -> SnailfishPair(snailfish.lhs, propagateRight(snailfish.rhs, add))
    }

  fun propagateLeft(snailfish: Snailfish, add: Int): Snailfish = if (add == 0) snailfish else
    when (snailfish) {
      is SnailfishNumber -> SnailfishNumber(snailfish.value + add)
      is SnailfishPair -> SnailfishPair(propagateLeft(snailfish.lhs, add), snailfish.rhs)
    }

  return when (this) {
    is SnailfishNumber -> null
    is SnailfishPair -> if (depth == 4) {
      check(lhs is SnailfishNumber && rhs is SnailfishNumber)
      Triple(SnailfishNumber(0), lhs.value, rhs.value)
    } else {
      lhs.reduceExplode(depth + 1)
        ?.let { (snailfish, left, right) ->
          Triple(SnailfishPair(snailfish, propagateLeft(rhs, right)), left, 0)
        }
        ?: rhs.reduceExplode(depth + 1)
          ?.let { (snailfish, left, right) ->
            Triple(SnailfishPair(propagateRight(lhs, left), snailfish), 0, right)
          }
    }
  }
}

private fun Snailfish.reduceSplit(): SnailfishPair? = when(this) {
  is SnailfishNumber -> if (value < 10) null else
    SnailfishPair(SnailfishNumber(value / 2), SnailfishNumber((value + 1) / 2))
  is SnailfishPair ->
    lhs.reduceSplit()?.let { SnailfishPair(it, rhs) } ?:
      rhs.reduceSplit()?.let { SnailfishPair(lhs, it) }
}

private fun Snailfish.magnitude(): Int = when(this) {
  is SnailfishNumber -> value
  is SnailfishPair -> 3 * lhs.magnitude() + 2 * rhs.magnitude()
}

private sealed interface Snailfish
private data class SnailfishPair(
  val lhs: Snailfish,
  val rhs: Snailfish,
) : Snailfish
private data class SnailfishNumber(
  val value: Int,
) : Snailfish

private class Parser(private val input: String) : AutoCloseable {
  private var ptr = 0

  fun parseSnailfish(): Snailfish = if (input[ptr] == '[') {
    parsePair()
  } else {
    parseNumber()
  }

  private fun parseNumber(): SnailfishNumber {
    check(input[ptr] in '0'..'9')
    var res = 0
    while (ptr < input.length && input[ptr].isDigit()) {
      res = 10 * res + input[ptr++].code - '0'.code
    }
    return SnailfishNumber(res)
  }

  private fun parsePair(): SnailfishPair {
    check(input[ptr++] == '[')
    val lhs = parseSnailfish()
    check(input[ptr++] == ',')
    val rhs = parseSnailfish()
    check(input[ptr++] == ']')
    return SnailfishPair(lhs, rhs)
  }

  override fun close() {
    check(ptr == input.length)
  }
}

private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day18/$s")).readLines().map { line ->
    Parser(line).use { it.parseSnailfish() }
  }
}

private typealias Input = List<Snailfish>