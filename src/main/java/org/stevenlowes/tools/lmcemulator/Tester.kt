package org.stevenlowes.tools.lmcemulator

import java.io.File
import kotlin.coroutines.experimental.buildSequence
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val mailboxes = File("lmc.lmc")
            .readLines()[1]
            .split("%")[2]
            .split(",")
            .dropLast(1)
            .map { it.toInt() }
            .toIntArray()

    val testIterator = TestIterator()

    val emulators = (1..Runtime.getRuntime().availableProcessors()).map { _ ->
        Emulator(mailboxes, testIterator)
    }

    val threads = emulators.map { Thread(it) }

    val time = measureTimeMillis {
        threads.forEach { it.start() }
        threads.forEach { it.join() }
    }

    val (tests, ticks) = emulators.fold(0L to 0L) { (tests, ticks) , emulator ->
        (tests + emulator.totalTests) to (ticks + emulator.totalTicks)
    }

    println("$tests tests completed")
    println("${ticks/tests} fetch execute cycles average")
    println("${time.toDouble()/1000} seconds total runtime")
}