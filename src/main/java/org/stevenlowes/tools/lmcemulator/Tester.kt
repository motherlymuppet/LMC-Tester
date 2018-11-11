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

    val threads = (1..Runtime.getRuntime().availableProcessors()).map { _ ->
        Thread(Emulator(mailboxes, testIterator))
    }

    val time = measureTimeMillis {
        threads.forEach { it.start() }
        threads.forEach { it.join() }
    }

    println("${time.toDouble()/1000} seconds")
}