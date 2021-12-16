package day14

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun part1(input: Input): Int {
    fun iterate(template: String, rules: Map<String, String>) = buildString {
      append(template[0])
      for (i in 1 until template.length) {
        rules[template.substring(i - 1, i + 1)]?.let {
          append(it)
        }
        append(template[i])
      }
    }
    val rules = input.second.toMap()
    var template = input.first
    repeat(10) {
      template = iterate(template, rules)
    }
    return template
      .groupBy { it }
      .values
      .map { it.size }
      .let { it.maxOrNull()!! - it.minOrNull()!! }
  }

  fun part2(input: Input): Long {
    val rules = input.second.toMap()
    val mem = Array(41) {
      mutableMapOf<String, Map<Char, Long>>()
    }

    fun combine(
      lhs: Map<Char, Long>,
      rhs: Map<Char, Long>,
    ): Map<Char, Long> {
      val sum = (lhs.keys + rhs.keys).associateWith {
        lhs.getOrDefault(it, 0) + rhs.getOrDefault(it, 0)
      }
      return sum
    }

    fun rec(pair: String, depth: Int): Map<Char, Long> {
      mem[depth][pair]?.let { return it }
      if (depth == 0 || !rules.containsKey(pair)) {
        return mapOf(pair.first() to 1L).also { mem[depth][pair] = it }
      }
      val between = rules[pair]!!
      return combine(
        rec(pair[0] + between, depth - 1),
        rec(between + pair[1], depth - 1)
      ).also { mem[depth][pair] = it }
    }

    val origin = input.first + "$"
    var answer = emptyMap<Char, Long>()
    for (i in 0 until origin.lastIndex) {
      answer = combine(answer, rec(origin.substring(i, i + 2), 40))
    }
    return answer.values.let {
      it.maxOrNull()!! - it.minOrNull()!!
    }
  }

  check(part1(readInput("test-input.txt")) == 1588)
  check(part2(readInput("test-input.txt")) == 2188189693529L)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day14/$s")).readLines().let { lines ->
    val origin = lines.first()
    val rules = lines.drop(2).map {
      val (a, b) = it.split(" -> ")
      a to b
    }
    origin to rules
  }
}

private typealias Input = Pair<String, List<Pair<String, String>>>