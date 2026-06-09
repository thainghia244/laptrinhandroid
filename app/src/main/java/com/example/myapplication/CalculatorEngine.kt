package com.example.myapplication

import kotlin.math.*

/**
 * CalculatorEngine - Xử lý tất cả logic tính toán
 * Hỗ trợ cơ bản, khoa học và lập trình
 */
class CalculatorEngine {
    
    private var firstNumber = 0.0
    private var secondNumber = 0.0
    private var operation = ""
    private var isOperationPressed = false
    private var lastError: String? = null
    
    // Getter methods
    fun getFirstNumber() = firstNumber
    fun getSecondNumber() = secondNumber
    fun getOperation() = operation
    fun isOperationPressed() = isOperationPressed
    fun getLastError() = lastError
    
    /**
     * Thêm số vào kết quả
     */
    fun appendNumber(number: String, currentText: String): String {
        return when {
            isOperationPressed -> {
                isOperationPressed = false
                number
            }
            currentText == "0" && number != "." -> number
            currentText.contains(".") && number == "." -> currentText
            currentText.length >= 15 -> currentText // Giới hạn độ dài
            else -> currentText + number
        }
    }
    
    /**
     * Thêm dấu thập phân
     */
    fun appendDecimal(currentText: String): String {
        return if (!currentText.contains(".")) {
            currentText + "."
        } else {
            currentText
        }
    }
    
    /**
     * Xử lý khi nhấn phép toán
     */
    fun onOperationPressed(op: String, currentText: String): Pair<String, String> {
        val currentNumber = currentText.toDoubleOrNull() ?: 0.0
        lastError = null
        
        // Nếu đã có phép toán trước đó, tính kết quả trước
        if (operation.isNotEmpty() && !isOperationPressed) {
            secondNumber = currentNumber
            val result = calculateResult(firstNumber, secondNumber, operation)
            firstNumber = result
            isOperationPressed = true
            return Pair(formatResult(result), "$firstNumber $operation")
        }
        
        firstNumber = currentNumber
        operation = op
        isOperationPressed = true
        
        return Pair(currentText, "$firstNumber $operation")
    }
    
    /**
     * Xử lý khi nhấn bằng
     */
    fun onEqual(currentText: String): Triple<String, String, String?> {
        if (operation.isEmpty()) {
            return Triple(currentText, "", null)
        }
        
        val secondNum = currentText.toDoubleOrNull() ?: 0.0
        secondNumber = secondNum
        val result = calculateResult(firstNumber, secondNumber, operation)
        
        val expression = "$firstNumber $operation $secondNumber"
        val resultStr = formatResult(result)
        
        firstNumber = result
        operation = ""
        isOperationPressed = true
        
        return Triple(resultStr, expression, lastError)
    }
    
    /**
     * Tính toán cơ bản
     */
    private fun calculateResult(num1: Double, num2: Double, op: String): Double {
        return when (op) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "/" -> {
                if (num2 != 0.0) {
                    num1 / num2
                } else {
                    lastError = "Không thể chia cho 0"
                    0.0
                }
            }
            else -> 0.0
        }
    }
    
    /**
     * Các hàm khoa học
     */
    fun sin(value: Double, isDegree: Boolean = true): Double {
        val radians = if (isDegree) Math.toRadians(value) else value
        return sin(radians)
    }
    
    fun cos(value: Double, isDegree: Boolean = true): Double {
        val radians = if (isDegree) Math.toRadians(value) else value
        return cos(radians)
    }
    
    fun tan(value: Double, isDegree: Boolean = true): Double {
        val radians = if (isDegree) Math.toRadians(value) else value
        return tan(radians)
    }
    
    fun log(value: Double): Double {
        return if (value > 0) log10(value) else {
            lastError = "Log chỉ hỗ trợ số dương"
            0.0
        }
    }
    
    fun ln(value: Double): Double {
        return if (value > 0) ln(value) else {
            lastError = "Ln chỉ hỗ trợ số dương"
            0.0
        }
    }
    
    fun power(base: Double, exponent: Double): Double {
        return base.pow(exponent)
    }
    
    fun sqrt(value: Double): Double {
        return if (value >= 0) sqrt(value) else {
            lastError = "Căn bậc 2 chỉ hỗ trợ số không âm"
            0.0
        }
    }
    
    fun factorial(n: Double): Double {
        val intN = n.toInt()
        if (intN < 0 || intN.toDouble() != n) {
            lastError = "Giai thừa chỉ hỗ trợ số nguyên không âm"
            return 0.0
        }
        
        var result = 1.0
        for (i in 2..intN) {
            result *= i
        }
        return result
    }
    
    fun percentage(value: Double): Double {
        return value / 100
    }
    
    /**
     * Giải phương trình bậc 2: ax² + bx + c = 0
     */
    fun solveQuadratic(a: Double, b: Double, c: Double): Pair<String, String?> {
        if (a == 0.0) {
            return Pair("0", "Hệ số a phải khác 0")
        }
        
        val discriminant = b * b - 4 * a * c
        
        return when {
            discriminant < 0 -> {
                Pair("Vô nghiệm", null)
            }
            discriminant == 0.0 -> {
                val x = -b / (2 * a)
                Pair("x = ${formatResult(x)}", null)
            }
            else -> {
                val sqrtDis = sqrt(discriminant)
                val x1 = (-b + sqrtDis) / (2 * a)
                val x2 = (-b - sqrtDis) / (2 * a)
                Pair(
                    "x₁ = ${formatResult(x1)}\nx₂ = ${formatResult(x2)}",
                    null
                )
            }
        }
    }
    
    /**
     * Giải phương trình bậc 3: ax³ + bx² + cx + d = 0
     * Sử dụng công thức Cardano
     */
    fun solveCubic(a: Double, b: Double, c: Double, d: Double): Pair<String, String?> {
        if (a == 0.0) {
            return Pair("0", "Hệ số a phải khác 0")
        }
        
        // Normalize: chia cho a
        val b1 = b / a
        val c1 = c / a
        val d1 = d / a
        
        // Chuyển đổi sang dạng chuẩn
        val p = c1 - b1 * b1 / 3
        val q = 2 * b1 * b1 * b1 / 27 - b1 * c1 / 3 + d1
        
        val discriminant = -(4 * p * p * p + 27 * q * q)
        
        // Tính toán đơn giản cho phương trình bậc 3
        return try {
            val delta = b1 * b1 - 3 * c1
            val result = when {
                delta >= 0 -> {
                    val u = cbrt(-q / 2 + sqrt(q * q / 4 + p * p * p / 27))
                    val v = cbrt(-q / 2 - sqrt(q * q / 4 + p * p * p / 27))
                    val x = u + v - b1 / 3
                    "x ≈ ${formatResult(x)}"
                }
                else -> "Có 3 nghiệm phức"
            }
            Pair(result, null)
        } catch (e: Exception) {
            Pair("0", "Lỗi tính toán")
        }
    }
    
    /**
     * Format kết quả
     */
    fun formatResult(result: Double): String {
        return when {
            result.isNaN() || result.isInfinite() -> "Lỗi"
            result == result.toLong().toDouble() -> result.toLong().toString()
            else -> String.format("%.6f", result).trimEnd('0').trimEnd('.')
        }
    }
    
    /**
     * Xóa tất cả
     */
    fun clear() {
        firstNumber = 0.0
        secondNumber = 0.0
        operation = ""
        isOperationPressed = false
        lastError = null
    }
}
