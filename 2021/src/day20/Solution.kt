package day20

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun wrap(field: List<String>, surrounding: Char = '.'): List<String> {
    val length = field.first().length
    val darkRow = buildString(length) { repeat(length) { append(surrounding) } }
    val fix = List(3) { darkRow }
    return (fix + field + fix).map { "$surrounding$surrounding$surrounding$it$surrounding$surrounding$surrounding" }
  }

  fun iterate(wrapped: List<String>, decoder: String): List<String> {
    return buildList(wrapped.size - 2) {
      for (i in 1 until wrapped.lastIndex) {
        add(
          buildString(wrapped[i].length - 2) {
            for (j in 1 until wrapped[i].lastIndex) {
              var index = 0
              for (di in -1..1) {
                for (dj in -1..1) {
                  index = 2 * index + if (wrapped[i + di][j + dj] == '#') 1 else 0
                }
              }
              append(decoder[index])
            }
          }
        )
      }
    }
  }

  fun part1(input: Input): Int {
    return iterate(
      iterate(wrap(input.second), input.first).let { wrap(it, it.first().first()) },
      input.first
    ).sumOf {
      it.count { it == '#' }
    }
  }

  fun part2(input: Input): Int {
    var image = wrap(input.second)
    repeat(50) {
      val next = iterate(image, input.first)
      image = wrap(next, next.first().first())
    }
    return image.sumOf { it.count { it == '#' } }
  }

  check(part1(readInput("test-input.txt")) == 35)
  check(part2(readInput("test-input.txt")) == 3351)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}

private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day20/$s")).readLines().let { lines ->
    Pair(
      lines.first(),
      lines.drop(2).filter { it.isNotBlank() }
    )
  }
}

private typealias Input = Pair<String, List<String>>