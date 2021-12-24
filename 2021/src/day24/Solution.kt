package day24

import java.nio.file.Files
import java.nio.file.Paths
import java.util.TreeMap
import kotlin.system.measureTimeMillis

fun main() {
  fun Char.registerId() = code - 'w'.code

  val alu = longArrayOf(0, 0, 0, 0)
  var nextInput = -1

  fun String.parseCommand(): Command {
    return if (startsWith("inp ")) {
      Command { alu[this[4].registerId()] = nextInput.toLong().also { check(it in 1..9) } }
    } else {
      val (cmd, a, b) = split(" ")
      check(a.length == 1)
      val register = a.first().registerId()
      val value = { if (b.first() in "wxyz") alu[b.first().registerId()] else b.toLong() }
      when (cmd) {
        "add" -> Command { alu[register] += value() }
        "mul" -> Command { alu[register] *= value() }
        "div" -> Command { alu[register] /= value() }
        "mod" -> Command { alu[register] %= value() }
        "eql" -> Command { alu[register] = if (alu[register] == value()) 1 else 0 }
        else -> error("Unknown command $cmd")
      }
    }
  }

  fun solve(input: Input, comparator: Comparator<Int>): Long {
    check(input.size % 14 == 0)
    val commands = input.map(String::parseCommand).chunked(input.size / 14)

    data class State(val digit: Int, val prevZ: Long) : Comparable<State> {
      override fun compareTo(other: State) = comparator.compare(this.digit, other.digit)
    }

    val mem = Array(15) { TreeMap<Long, State>() }
    mem[0][0] = State(0, Long.MIN_VALUE)
    val pow26 = generateSequence(1L) { it * 26 }.takeWhile { it < Int.MAX_VALUE }.toList()
    for (len in 1..14) {
      val threshold = pow26.getOrElse(15 - len) { Long.MAX_VALUE }
      for (digit in 1..9) {
        nextInput = digit
        for (z in mem[len - 1].keys) {
          if (z > threshold) break // We won't get Z=0 in the end if current Z is too big.
          alu.fill(0); alu['z'.registerId()] = z
          commands[len - 1].forEach { it.execute() }
          mem[len].merge(alu['z'.registerId()], State(digit, z), ::maxOf)
        }
      }
    }

    val (answer, _, _) = mem.foldRight(Triple(0L, 1L, 0L)) { map, (acc, pow10, z) ->
      val (digit, prevZ) = map[z]!!
      Triple(acc + digit * pow10, pow10 * 10, prevZ)
    }
    return answer
  }

  fun part1(input: Input): Long {
    return solve(input, Comparator.naturalOrder())
  }

  fun part2(input: Input): Long {
    return solve(input, Comparator.reverseOrder())
  }

  val millis = measureTimeMillis {
    println(part1(readInput("input.txt")))
    println(part2(readInput("input.txt")))
  }
  System.err.println("Done in $millis ms")
}

private fun interface Command {
  fun execute()
}

private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day24/$s")).readLines()
}

private typealias Input = List<String>

