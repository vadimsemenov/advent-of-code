package day16

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  fun part1(input: Input): Int {
    fun dfs(packet: Packet): Int {
      var sum = packet.version
      if (packet is Operation) {
        for (subPacket in packet.subPackets) {
          sum += dfs(subPacket)
        }
      }
      return sum
    }

    Parser(input).use { parser ->
      return dfs(parser.parsePacket())
    }
  }

  fun part2(input: Input): Long {
    fun evaluate(packet: Packet): Long = when(packet) {
      is Literal -> packet.value
      is Operation -> when(packet.typeId) {
        0 -> packet.subPackets.sumOf { evaluate(it) }
        1 -> packet.subPackets.fold(1L) { acc, subPacket -> acc * evaluate(subPacket) }
        2 -> packet.subPackets.minOf { evaluate(it) }
        3 -> packet.subPackets.maxOf { evaluate(it) }
        5, 6, 7 -> {
          check(packet.subPackets.size == 2)
          val (lhs, rhs) = packet.subPackets.map { evaluate(it) }
          when (packet.typeId) {
            5 -> if (lhs > rhs) 1 else 0
            6 -> if (lhs < rhs) 1 else 0
            7 -> if (lhs == rhs) 1 else 0
            else -> error("unexpected packet type ID: ${packet.typeId}")
          }
        }
        else -> error("unexpected packet type ID: ${packet.typeId}")
      }
    }

    Parser(input).use { parser ->
      return evaluate(parser.parsePacket())
    }
  }

  check(part1("D2FE28".toBinaryString()) == 6)
  check(part1("38006F45291200".toBinaryString()) == 9)
  check(part1("EE00D40C823060".toBinaryString()) == 14)
  check(part1("8A004A801A8002F478".toBinaryString()) == 16)
  check(part1("620080001611562C8802118E34".toBinaryString()) == 12)
  check(part1("C0015000016115A2E0802F182340".toBinaryString()) == 23)
  check(part1("A0016C880162017C3686B18A3D4780".toBinaryString()) == 31)
  check(part2("C200B40A82".toBinaryString()) == 3L)
  check(part2("04005AC33890".toBinaryString()) == 54L)
  check(part2("880086C3E88112".toBinaryString()) == 7L)
  check(part2("CE00C43D881120".toBinaryString()) == 9L)
  check(part2("D8005AC2A8F0".toBinaryString()) == 1L)
  check(part2("F600BC2D8F".toBinaryString()) == 0L)
  check(part2("9C005AC2F8F0".toBinaryString()) == 0L)
  check(part2("9C0141080250320F1802104A08".toBinaryString()) == 1L)

  println(part1(readInput("input.txt")))
  println(part2(readInput("input.txt")))
}

private sealed interface Packet {
  val version: Int
  val typeId: Int
}
private data class Literal(
  override val version: Int,
  override val typeId: Int,
  val value: Long
) : Packet
private data class Operation(
  override val version: Int,
  override val typeId: Int,
  val subPackets: List<Packet>
) : Packet


private class Parser(val string: String): AutoCloseable {
  private var ptr = 0

  fun parsePacket(): Packet {
    val version = parseNum(3)
    val typeId = parseNum(3)
    return if (typeId == 4) {
      Literal(version, typeId, parseLiteralValue())
    } else {
      Operation(version, typeId, parseSubPackets())
    }
  }

  private fun parseSubPackets(): List<Packet> {
    val lengthType = parseNum(1)
    val length = parseNum(if (lengthType == 0) 15 else 11)
    val subPacketsStart = ptr
    val subPackets = mutableListOf<Packet>()
    fun parseNext(): Boolean = when (lengthType) {
      0 -> ptr - subPacketsStart < length
      1 -> subPackets.size < length
      else -> error("unexpected length type: $lengthType")
    }
    while (parseNext()) {
      subPackets.add(parsePacket())
    }
    when (lengthType) {
      0 -> check(ptr - subPacketsStart == length)
      1 -> check(subPackets.size == length)
    }
    return subPackets
  }

  private fun parseLiteralValue(): Long {
    var value = 0L
    do {
      val last = parseNum(1) == 0
      value = (value shl 4) + parseNum(4)
    } while (!last)
    return value
  }

  private fun parseNum(len: Int): Int {
    return string.substring(ptr, ptr + len).toInt(2).also {
      ptr += len
    }
  }

  override fun close() {
    while (ptr < string.length) {
      check(string[ptr++] == '0')
    }
  }
}

private fun String.toBinaryString() =
  map {
    it.toString()
      .toInt(16)
      .toString(2)
      .padStart(4, '0')
  }.joinToString("") { it }

private fun readInput(s: String): Input {
  return Files.newBufferedReader(Paths.get("src/day16/$s")).readLines().first().toBinaryString()
}

private typealias Input = String