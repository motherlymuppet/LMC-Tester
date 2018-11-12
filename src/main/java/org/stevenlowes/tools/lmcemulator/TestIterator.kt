package org.stevenlowes.tools.lmcemulator

class TestIterator {
    private var value = 100
    private val maxValue = 999
    private var fromBase = 0
    private val maxFromBase = 15
    private var toBase = 0
    private val maxToBase = 15

    private val errorOutputs = listOf(999)
    private val maxTicks = 1000 * 1000

    fun next(): Test? {
        var returnVal: Test? = null
        while(returnVal == null){
            if(value == maxValue && fromBase == maxFromBase && toBase == maxToBase){
                return null
            }

            if(value.toString().all { it < fromBase }){ //Only check valid inputs
                val inputs = listOf(value, fromBase, toBase)

                returnVal = if(fromBase < 2 || fromBase > 10 || toBase < 2 || toBase > 10){ //Invalid bases should return error
                    Test(inputs, errorOutputs, maxTicks)
                }
                else{
                    val answerString = Integer.toString(Integer.parseInt(value.toString(), fromBase), toBase)

                    if (answerString.length > 3 || answerString.any { it >= toBase }) {
                        Test(inputs, errorOutputs, maxTicks)
                    }
                    else{
                        Test(inputs, listOf(answerString.toInt()), maxTicks)
                    }
                }
            }

            increment()
        }

        return returnVal
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

operator fun Char.compareTo(other: Int) = toString().toInt().compareTo(other)