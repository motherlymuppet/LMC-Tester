package org.stevenlowes.tools.lmcemulator

class TestIterator {
    private var value = 0
    private val maxValue = 999
    private var fromBase = 0
    private val maxFromBase = 15
    private var toBase = 0
    private val maxToBase = 15

    private val errorOutputs = listOf(999)
    private val maxTicks = 1000 * 1000

    fun next(): Test? {
        while(true){
            if(value == maxValue && fromBase == maxFromBase && toBase == maxToBase){
                return null
            }

            if(value.toString().all { it.toInt() < fromBase }){ //Only check valid inputs
                val inputs = listOf(value, fromBase, toBase)

                if(fromBase < 2 || fromBase > 10 || toBase < 2 || toBase > 10){ //Invalid bases should return error
                    return Test(inputs, errorOutputs, maxTicks)
                }

                val answerString =
                        try {
                            Integer.toString(Integer.parseInt(value.toString(), fromBase), toBase)
                        }
                        catch (ex: Exception) {
                            println("$value, $fromBase, $toBase, ${ex.message}")
                            "000"
                        }

                if (answerString.length > 3 || answerString.any { it.toInt() >= toBase }) {
                    Test(inputs, errorOutputs, maxTicks)
                }

                Test(inputs, listOf(answerString.toInt()), maxTicks)
            }

            increment()
        }
    }

    private fun increment(){
        value++
        if (value > maxValue) {
            value = 0
            fromBase++
            if (fromBase > maxFromBase) {
                fromBase = 0
                toBase++
            }
        }
    }
}