package com.example.calculadora



class calculatorLogic {

    var currentNumber: String = ""
    var previousNumber: String = ""
    var operation: String? = null

    fun onInput(value: String): String {
        return when {
            value == "CE" -> clearEntry()
            value in listOf("+", "-", "×", "÷") -> handleOperation(value)
            value == "=" -> calculateResult()
            value == "." -> handleDecimal()
            else -> handleNumber(value)
        }
    }

    fun handleNumber(value: String): String {
        currentNumber += value
        return currentNumber
    }

    fun handleOperation(op: String): String {
        if (currentNumber.isNotEmpty()) {
            previousNumber = currentNumber
            currentNumber = ""
        }
        operation = op
        return previousNumber
    }

    fun handleDecimal(): String {
        if (!currentNumber.contains(".")) {
            currentNumber += if (currentNumber.isEmpty()) "0." else "."
        }
        return currentNumber
    }

    fun calculateResult(): String {
        if (previousNumber.isEmpty() || currentNumber.isEmpty() || operation == null) {
            return currentNumber
        }

        val num1 = previousNumber.toDoubleOrNull()
        val num2 = currentNumber.toDoubleOrNull()

        if (num1 == null || num2 == null) {
            return "Error"
        }

        return try {
            val result = when (operation) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "×" -> num1 * num2
                "÷" -> if (num2 != 0.0) num1 / num2 else "Error"
                else -> null
            }

            currentNumber = result.toString()
            previousNumber = ""
            operation = null
            currentNumber
        } catch (e: ArithmeticException) {
            "Error"
        }
    }

    fun clearEntry(): String {
        currentNumber = ""
        return "0"
    }

}
