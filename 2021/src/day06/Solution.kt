package day06
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  val mem = Array(2) {
    Array(9) {
      LongArray(257) { -1 }
    }
  }
  fun model(daysLeft: Int, day: Int, part: Int): Long {
    require(daysLeft in 0..8)
    require(part in 0..1)
    val lastDay = if (part == 0) 80 else 256
    require(day in 0..lastDay)

    mem[part][daysLeft][day].let {
      if (it != -1L) return it
    }

    val res = if (day >= lastDay) 1 else if (daysLeft == 0) {
      model(6, day + 1, part) + model(8, day + 1, part)
    } else {
      model(daysLeft - 1, day + 1, part)
    }

    return res.also {
      mem[part][daysLeft][day] = res
    }
  }

  fun part1(input: Input): Long {
    return input.sumOf { model(it, 0, 0) }
  }

  fun part2(input: Input): Long {
    return input.sumOf { model(it, 0, 1) }
  }

  check(part1(readInput("test-input.txt")) == 5934L)
  println(part1(readInput("input.txt")))

  part2(readInput("test-input.txt")).let {
    check(it == 26984457539L) { it }
  }
  println(part2(readInput("input.txt")))

}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day06/$s")).readLines().first().split(",").map { it.toInt() }
}

private typealias Input = List<Int>