package org.stevenlowes.tools.lmcemulator

import kotlin.math.abs

class Emulator(mailboxes: IntArray, private val testIterator: TestIterator) : Runnable {
    private val mailboxes = mailboxes.clone()
    var totalTicks = 0L
    var totalTests = 0L

    override fun run() {
        if (mailboxes.size > 100) {
            throw LmcException(null, "More than 100 mailboxes")
        }

        if (mailboxes.any { it > 999 || it < 0 }) {
            throw LmcException(null, "Mailbox not in range 0-999")
        }

        while (true) {
            val nextTest = synchronized(testIterator) {
                testIterator.next() ?: return
            }

            try {
                totalTicks += runTest(nextTest)
                totalTests++
            }
            catch (ex: LmcException) {
                println(ex.message)
            }
        }
    }

    private fun runTest(test: Test): Int {
        val inputs = test.inputs.toMutableList()
        val outputs = test.outputs.toMutableList()

        if(inputs == listOf(5,0,0).toMutableList()){
            println("Here")
        }

        var programCounter = 0
        var calculator = 0
        var tickCount = 0
        var negative = false

        var done = false

        while (!done) {
            val mailbox = mailboxes[programCounter]

            tickCount++
            programCounter = (programCounter + 1) % 100

            if (tickCount > test.maxTicks) {
                throw LmcException(test, "Max ticks exceeded")
            }

            val instruction = mailbox / 100
            val address = mailbox % 100

            when (instruction) {
                0 -> {
                    done = true
                }
                1 -> {
                    val toAdd = mailboxes[address]
                    calculator += toAdd
                    calculator %= 1000
                }
                2 -> {
                    val toSub = mailboxes[address]
                    calculator -= toSub
                    if (calculator < 0) {
                        negative = true
                        calculator = abs(calculator % 1000)
                    }
                }
                3 -> {
                    mailboxes[address] = calculator
                }
                5 -> {
                    calculator = mailboxes[address]
                    negative = false
                }
                6 -> {
                    programCounter = address
                }
                7 -> {
                    if (calculator == 0) {
                        programCounter = address
                    }
                }
                8 -> {
                    if (!negative) {
                        programCounter = address
                    }
                }
                9 -> {
                    when (address) {
                        1 -> {
                            val firstInput = inputs.firstOrNull() ?: throw LmcException(test,
                                                                                        "Requested nonexistent Input")
                            inputs.removeAt(0)

                            calculator = firstInput
                            negative = false
                        }
                        2 -> {
                            val firstOutput = outputs.firstOrNull() ?: throw LmcException(test,
                                                                                          "Output more values than expected")
                            outputs.removeAt(0)

                            if (firstOutput != calculator) {
                                throw LmcException(test,
                                                   "Output unexpected value, expected $firstOutput actual $calculator")
                            }
                        }
                    }
                }
            }
        }

        if (outputs.isNotEmpty()) {
            throw LmcException(test, "Not all expected outputs were returned")
        }

        return tickCount
    }
}