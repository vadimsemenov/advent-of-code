package day12

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun buildGraph(lines: Input): Map<String, List<String>> = buildMap<String, MutableList<String>> {
    for (line in lines) {
      val (v, w) = line.split("-")
      val vList = getOrPut(v) { mutableListOf() }
      val wList = getOrPut(w) { mutableListOf() }
      vList.add(w)
      wList.add(v)
    }
  }

  fun dfs(vertex: String, graph: Map<String, List<String>>, visited: Set<String>, visitedTwice: Boolean): Int {
    if (vertex == "end") return 1
    var sum = 0
    for (next in graph[vertex]!!) {
      if (next !in visited) {
        val nextVisited = if (next.first().isLowerCase()) visited + next else visited
        sum += dfs(next, graph, nextVisited, visitedTwice)
      } else if (next != "start" && !visitedTwice) {
        sum += dfs(next, graph, visited, true)
      }
    }
    return sum
  }

  fun part1(lines: Input): Int {
    val graph = buildGraph(lines)
    return dfs("start", graph, setOf("start"), true)
  }

  fun part2(lines: Input): Int {
    val graph = buildGraph(lines)
    return dfs("start", graph, setOf("start"), false)
  }

  check(part1(readInput("test-input.txt")) == 226)
  check(part2(readInput("test-input.txt")) == 3509)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day12/$s")).readLines()
}

private typealias Input = List<String>