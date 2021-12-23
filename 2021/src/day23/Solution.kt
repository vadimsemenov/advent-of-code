package day23

import java.nio.file.Files
import java.nio.file.Paths
import java.util.TreeSet
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

fun main() {
  val amphipods = "ABCD"
  val step = listOf(1, 10, 100, 1000)
  val rooms = listOf(3, 5, 7, 9)

  fun Input.isFinal(): Boolean {
    for (i in this[0]) {
      if (i != '.' && i != '#') return false
    }
    for (i in amphipods.indices) {
      for (j in 1 until this.size) {
        if (this[j][rooms[i]] != amphipods[i]) {
          return false
        }
      }
    }
    return true
  }

  fun Input.swap(x1: Int, y1: Int, x2: Int, y2: Int): Input = map { it.toCharArray() }.run {
    this[x1][y1] = this[x2][y2].also {
      this[x2][y2] = this[x1][y1]
    }
    map { String(it) }
  }

  fun generateNextStates(list: List<String>) = buildList {
    for (i in list[0].indices) {
      if (list[0][i] !in amphipods) continue
      val amphipod = list[0][i].code - 'A'.code // move from hallway to the final room.
      val roomId = rooms[amphipod]
      val target = list.indexOfLast { it[roomId] == '.' }
      if (target == -1) continue
      if (list.subList(0, target).any { it[roomId] != '.' }) continue
      if (list.subList(target + 1, list.size).any { it[roomId] != amphipods[amphipod] }) continue
      if (list[0].substring(minOf(i + 1, roomId), maxOf(roomId, i - 1)).any { it != '.' }) continue
      val nextState = list.swap(0, i, target, roomId)
      val distance = (i - roomId).absoluteValue + target
      add(nextState to distance * step[amphipod])
    }

    for ((index, roomId) in rooms.withIndex()) {
      if (list.all { it[roomId] == '.' || it[roomId] == amphipods[index] }) continue
      for (i in 1 until list.size) {
        if (list[i][roomId] == '.') continue
        val amphipod = list[i][roomId].code - 'A'.code // move from starting room to the hallway.
        for (range in listOf(roomId + 1 until list[0].length, roomId - 1 downTo 0)) {
          for (j in range) {
            if (list[0][j] != '.') break
            if (j in rooms) continue
            val nextState = list.swap(0, j, i, roomId)
            val distance = (j - roomId).absoluteValue + i
            add(nextState to distance * step[amphipod])
          }
        }
        break
      }
    }
  }

  fun part1(input: Input): Int {
    val stateIds = mutableMapOf<List<String>, Int>()
    val states = mutableListOf<List<String>>()
    val costs = mutableListOf<Int>()
    val queue = TreeSet(Comparator.comparingInt<Int> { costs[it] }.thenComparingInt { it })

    stateIds[input] = states.size
    states.add(input)
    costs.add(0)
    queue.add(0)

    while (queue.isNotEmpty()) {
      val id = queue.pollFirst()!!
      if (states[id].isFinal()) {
        return costs[id]
      }
      for ((state, delta) in generateNextStates(states[id])) {
        val newCost = costs[id] + delta
        val stateId = stateIds.computeIfAbsent(state) { states.size }
        if (stateId == states.size) {
          states.add(state)
          costs.add(newCost)
          queue.add(stateId)
        } else if (newCost < costs[stateId]) {
          check(queue.remove(stateId))
          costs[stateId] = newCost
          queue.add(stateId)
        }
      }
    }
    error("Polundra!")
  }

  fun part2(input: Input): Int {
    return part1(
      buildList {
        add(input[0])
        add(input[1])
        add("  #D#C#B#A#")
        add("  #D#B#A#C#")
        add(input[2])
      }
    )
  }

  check(part1(readInput("test-input.txt")) == 12521)
  check(part2(readInput("test-input.txt")) == 44169)

  val millis = measureTimeMillis {
    println(part1(readInput("input.txt")))
    println(part2(readInput("input.txt")))
  }
  System.err.println("Done in $millis ms")
}

private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day23/$s")).readLines().subList(1, 4)
}

private typealias Input = List<String>
