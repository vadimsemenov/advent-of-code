package day09

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  val dx = arrayOf(0, 1, 0, -1)
  val dy = arrayOf(-1, 0, 1, 0)
  fun part1(lines: Input): Int {
    var answer = 0
    for (x in lines.indices) {
      outer@for (y in lines[x].indices) {
        for (d in 0 until 4) {
          val xx = x + dx[d]
          val yy = y + dy[d]
          if (xx in lines.indices && yy in lines[xx].indices) {
            if (lines[xx][yy] <= lines[x][y]) continue@outer
          }
        }
        answer += lines[x][y].code - '0'.code + 1
      }
    }
    return answer
  }

  fun part2(lines: Input): Int {
    class DSU(size: Int) {
      private val parent = IntArray(size) { it }
      val size = IntArray(size) { 1 }
      fun getParent(v:  Int): Int = if (v == parent[v]) v else getParent(parent[v]).also { parent[v] = it }
      fun unite(v: Int, u: Int): Boolean {
        val vp = getParent(v)
        val up = getParent(u)
        if (up != vp) {
          parent[up] = vp
          size[vp] += size[up]
          return true
        }
        return false
      }
    }
    val dsu = DSU(lines.size * lines[0].length)
    for (x in lines.indices) {
      for (y in lines[x].indices) {
        if (lines[x][y] == '9') continue
        for (d in 0 until 4) {
          val xx = x + dx[d]
          val yy = y + dy[d]
          if (xx in lines.indices && yy in lines[xx].indices) {
            if (lines[xx][yy] <= lines[x][y]) {
              dsu.unite(yy * lines.size + xx, y * lines.size + x)
            }
          }
        }
      }
    }

    val map = buildMap {
      for (x in lines.indices) {
        for (y in lines[x].indices) {
          val root = dsu.getParent(y * lines.size + x)
          val size = dsu.size[root]
          put(root, size)
        }
      }
    }
    return map.values.sortedDescending().take(3).fold(1, Int::times)
  }

  check(part1(readInput("test-input.txt")) == 15)
  check(part2(readInput("test-input.txt")) == 1134)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}


private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day09/$s")).readLines()
}

private typealias Input = List<String>