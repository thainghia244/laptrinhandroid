package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class BasicCalculatorFragment : Fragment() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private lateinit var tvError: TextView
    private lateinit var calculatorEngine: CalculatorEngine

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_basic_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calculatorEngine = arguments?.getSerializable("calculator") as? CalculatorEngine
            ?: CalculatorEngine()

        // Khởi tạo các TextView
        tvExpression = view.findViewById(R.id.tv_expression)
        tvResult = view.findViewById(R.id.tv_result)
        tvError = view.findViewById(R.id.tv_error)

        // Khởi tạo các nút
        setupNumberButtons(view)
        setupOperationButtons(view)

        // Nút Clear
        val btnClear = view.findViewById<Button>(R.id.btn_clear)
        btnClear.setOnClickListener {
            clearCalculator()
        }

        // Nút Backspace
        val btnBackspace = view.findViewById<Button>(R.id.btn_backspace)
        btnBackspace.setOnClickListener {
            onBackspacePressed()
        }
    }

    private fun setupNumberButtons(view: View) {
        val numberButtonIds = intArrayOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
            R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
            R.id.btn_8, R.id.btn_9
        )

        for (id in numberButtonIds) {
            val button = view.findViewById<Button>(id)
            button.setOnClickListener {
                onNumberPressed(button.text.toString())
            }
        }

        // Nút dấu chấm
        val btnDot = view.findViewById<Button>(R.id.btn_dot)
        btnDot.setOnClickListener {
            onDotPressed()
        }
    }

    private fun setupOperationButtons(view: View) {
        val btnPlus = view.findViewById<Button>(R.id.btn_plus)
        val btnMinus = view.findViewById<Button>(R.id.btn_minus)
        val btnMultiply = view.findViewById<Button>(R.id.btn_multiply)
        val btnDivide = view.findViewById<Button>(R.id.btn_divide)
        val btnEqual = view.findViewById<Button>(R.id.btn_equal)

        btnPlus.setOnClickListener { onOperationPressed("+") }
        btnMinus.setOnClickListener { onOperationPressed("-") }
        btnMultiply.setOnClickListener { onOperationPressed("×") }
        btnDivide.setOnClickListener { onOperationPressed("/") }
        btnEqual.setOnClickListener { onEqualPressed() }
    }

    private fun onNumberPressed(number: String) {
        val currentText = tvResult.text.toString()
        val newText = calculatorEngine.appendNumber(number, currentText)
        tvResult.text = newText
        clearError()
    }

    private fun onDotPressed() {
        val currentText = tvResult.text.toString()
        val newText = calculatorEngine.appendDecimal(currentText)
        tvResult.text = newText
        clearError()
    }

    private fun onOperationPressed(op: String) {
        val currentText = tvResult.text.toString()
        val (resultText, expressionText) = calculatorEngine.onOperationPressed(op, currentText)
        tvResult.text = resultText
        tvExpression.text = expressionText
        clearError()
    }

    private fun onEqualPressed() {
        val currentText = tvResult.text.toString()
        val (resultText, expressionText, error) = calculatorEngine.onEqual(currentText)

        tvResult.text = resultText
        tvExpression.text = expressionText

        if (error != null) {
            showError(error)
        } else {
            clearError()
        }
    }

    private fun onBackspacePressed() {
        val currentText = tvResult.text.toString()
        if (currentText.isNotEmpty()) {
            val newText = currentText.dropLast(1)
            tvResult.text = if (newText.isEmpty()) "0" else newText
        }
        clearError()
    }

    private fun clearCalculator() {
        tvExpression.text = "0"
        tvResult.text = "0"
        calculatorEngine.clear()
        clearError()
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }

    private fun clearError() {
        tvError.visibility = View.GONE
        tvError.text = ""
    }

    companion object {
        fun newInstance(calculatorEngine: CalculatorEngine): BasicCalculatorFragment {
            return BasicCalculatorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("calculator", calculatorEngine)
                }
            }
        }
    }
}
