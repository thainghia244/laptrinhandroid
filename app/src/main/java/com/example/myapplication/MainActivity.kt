package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView

    private var firstNumber = 0.0
    private var secondNumber = 0.0
    private var operation = ""
    private var isOperationPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo các TextView
        tvExpression = findViewById(R.id.tv_expression)
        tvResult = findViewById(R.id.tv_result)

        // Khởi tạo các nút số
        setupNumberButtons()

        // Khởi tạo các nút phép toán
        setupOperationButtons()

        // Khởi tạo nút Clear
        val btnClear = findViewById<Button>(R.id.btn_clear)
        btnClear.setOnClickListener {
            clearCalculator()
        }
    }

    private fun setupNumberButtons() {
        val numberButtonIds = intArrayOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
            R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
            R.id.btn_8, R.id.btn_9
        )

        for (id in numberButtonIds) {
            val button = findViewById<Button>(id)
            button.setOnClickListener {
                onNumberPressed(button.text.toString())
            }
        }

        // Nút dấu chấm
        val btnDot = findViewById<Button>(R.id.btn_dot)
        btnDot.setOnClickListener {
            onDotPressed()
        }
    }

    private fun setupOperationButtons() {
        val btnPlus = findViewById<Button>(R.id.btn_plus)
        val btnMinus = findViewById<Button>(R.id.btn_minus)
        val btnMultiply = findViewById<Button>(R.id.btn_multiply)
        val btnDivide = findViewById<Button>(R.id.btn_divide)
        val btnEqual = findViewById<Button>(R.id.btn_equal)

        btnPlus.setOnClickListener { onOperationPressed("+") }
        btnMinus.setOnClickListener { onOperationPressed("-") }
        btnMultiply.setOnClickListener { onOperationPressed("×") }
        btnDivide.setOnClickListener { onOperationPressed("/") }
        btnEqual.setOnClickListener { onEqualPressed() }
    }

    private fun onNumberPressed(number: String) {
        val currentText = tvResult.text.toString()

        // Nếu vừa nhấn phép toán, xóa số hiện tại
        if (isOperationPressed) {
            tvResult.text = number
            isOperationPressed = false
        } else {
            // Ngăn chặn thêm số 0 vào đầu
            if (currentText == "0") {
                tvResult.text = number
            } else {
                tvResult.append(number)
            }
        }
    }

    private fun onDotPressed() {
        val currentText = tvResult.text.toString()

        // Ngăn chặn thêm nhiều dấu chấm
        if (!currentText.contains(".")) {
            tvResult.append(".")
        }
    }

    private fun onOperationPressed(op: String) {
        val currentNumber = tvResult.text.toString().toDoubleOrNull() ?: 0.0

        // Nếu đã có phép toán trước đó, tính kết quả trước
        if (operation.isNotEmpty() && !isOperationPressed) {
            secondNumber = currentNumber
            val result = calculateResult(firstNumber, secondNumber, operation)
            tvResult.text = formatResult(result)
            firstNumber = result
        } else {
            firstNumber = currentNumber
        }

        operation = op
        isOperationPressed = true

        // Cập nhật biểu thức
        updateExpression()
    }

    private fun onEqualPressed() {
        if (operation.isEmpty()) {
            return
        }

        val secondNumber = tvResult.text.toString().toDoubleOrNull() ?: 0.0
        val result = calculateResult(firstNumber, secondNumber, operation)

        // Cập nhật biểu thức hiển thị
        tvExpression.text = "$firstNumber $operation $secondNumber"

        // Hiển thị kết quả
        tvResult.text = formatResult(result)

        // Đặt lại các biến
        firstNumber = result
        operation = ""
        isOperationPressed = true
    }

    private fun calculateResult(num1: Double, num2: Double, op: String): Double {
        return when (op) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "/" -> if (num2 != 0.0) num1 / num2 else 0.0
            else -> 0.0
        }
    }

    private fun formatResult(result: Double): String {
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            String.format("%.6f", result).trimEnd('0').trimEnd('.')
        }
    }

    private fun updateExpression() {
        val currentExpression = tvExpression.text.toString()
        if (currentExpression == "0") {
            tvExpression.text = "$firstNumber $operation"
        } else {
            tvExpression.text = "$firstNumber $operation"
        }
    }

    private fun clearCalculator() {
        tvExpression.text = "0"
        tvResult.text = "0"
        firstNumber = 0.0
        secondNumber = 0.0
        operation = ""
        isOperationPressed = false
    }
}
