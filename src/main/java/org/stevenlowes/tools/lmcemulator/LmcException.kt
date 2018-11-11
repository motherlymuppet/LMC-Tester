package org.stevenlowes.tools.lmcemulator

class LmcException(test: Test?, message: String): Exception("$message; Test = $test")