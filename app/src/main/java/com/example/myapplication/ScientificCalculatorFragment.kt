package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ScientificCalculatorFragment : Fragment() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private lateinit var tvError: TextView
    private lateinit var rgAngleMode: RadioGroup
    private lateinit var calculatorEngine: CalculatorEngine
    private var isDegree = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_scientific_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calculatorEngine = arguments?.getSerializable("calculator") as? CalculatorEngine
            ?: CalculatorEngine()

        // Khởi tạo các TextView
        tvExpression = view.findViewById(R.id.tv_expression)
        tvResult = view.findViewById(R.id.tv_result)
        tvError = view.findViewById(R.id.tv_error)

        // Chế độ góc
        rgAngleMode = view.findViewById(R.id.rg_angle_mode)
        rgAngleMode.setOnCheckedChangeListener { _, checkedId ->
            isDegree = checkedId == R.id.rb_degree
        }

        // Khởi tạo các nút
        setupNumberButtons(view)
        setupBasicOperationButtons(view)
        setupScientificButtons(view)

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

    private fun setupBasicOperationButtons(view: View) {
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

    private fun setupScientificButtons(view: View) {
        // Trigonometric functions
        val btnSin = view.findViewById<Button>(R.id.btn_sin)
        val btnCos = view.findViewById<Button>(R.id.btn_cos)
        val btnTan = view.findViewById<Button>(R.id.btn_tan)

        btnSin.setOnClickListener { onTrigonometric("sin") }
        btnCos.setOnClickListener { onTrigonometric("cos") }
        btnTan.setOnClickListener { onTrigonometric("tan") }

        // Logarithm functions
        val btnLog = view.findViewById<Button>(R.id.btn_log)
        val btnLn = view.findViewById<Button>(R.id.btn_ln)

        btnLog.setOnClickListener { onLogarithm("log") }
        btnLn.setOnClickListener { onLogarithm("ln") }

        // Power and root functions
        val btnPower = view.findViewById<Button>(R.id.btn_power)
        val btnSqrt = view.findViewById<Button>(R.id.btn_sqrt)
        val btnFactorial = view.findViewById<Button>(R.id.btn_factorial)

        btnPower.setOnClickListener { onOperationPressed("^") }
        btnSqrt.setOnClickListener { onSqrt() }
        btnFactorial.setOnClickListener { onFactorial() }

        // Percentage
        val btnPercent = view.findViewById<Button>(R.id.btn_percent)
        btnPercent.setOnClickListener { onPercent() }

        // Pi and e
        val btnPi = view.findViewById<Button>(R.id.btn_pi)
        val btnE = view.findViewById<Button>(R.id.btn_e)

        btnPi.setOnClickListener { onConstant(Math.PI.toString()) }
        btnE.setOnClickListener { onConstant(Math.E.toString()) }
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
        
        // Xử lý power operation
        if (op == "^") {
            onPowerOperation(currentText)
            return
        }

        val (resultText, expressionText) = calculatorEngine.onOperationPressed(op, currentText)
        tvResult.text = resultText
        tvExpression.text = expressionText
        clearError()
    }

    private fun onTrigonometric(function: String) {
        val currentValue = tvResult.text.toString().toDoubleOrNull() ?: return
        val result = when (function) {
            "sin" -> calculatorEngine.sin(currentValue, isDegree)
            "cos" -> calculatorEngine.cos(currentValue, isDegree)
            "tan" -> calculatorEngine.tan(currentValue, isDegree)
            else -> 0.0
        }

        val angleMode = if (isDegree) "°" else " rad"
        tvExpression.text = "$function($currentValue$angleMode)"
        tvResult.text = calculatorEngine.formatResult(result)
        clearError()
    }

    private fun onLogarithm(function: String) {
        val currentValue = tvResult.text.toString().toDoubleOrNull() ?: return
        val result = when (function) {
            "log" -> calculatorEngine.log(currentValue)
            "ln" -> calculatorEngine.ln(currentValue)
            else -> 0.0
        }

        if (calculatorEngine.getLastError() != null) {
            showError(calculatorEngine.getLastError()!!)
            return
        }

        tvExpression.text = "$function($currentValue)"
        tvResult.text = calculatorEngine.formatResult(result)
        clearError()
    }

    private fun onSqrt() {
        val currentValue = tvResult.text.toString().toDoubleOrNull() ?: return
        val result = calculatorEngine.sqrt(currentValue)

        if (calculatorEngine.getLastError() != null) {
            showError(calculatorEngine.getLastError()!!)
            return
        }

        tvExpression.text = "√($currentValue)"
        tvResult.text = calculatorEngine.formatResult(result)
        clearError()
    }

    private fun onFactorial() {
        val currentValue = tvResult.text.toString().toDoubleOrNull() ?: return
        val result = calculatorEngine.factorial(currentValue)

        if (calculatorEngine.getLastError() != null) {
            showError(calculatorEngine.getLastError()!!)
            return
        }

        tvExpression.text = "$currentValue!"
        tvResult.text = calculatorEngine.formatResult(result)
        clearError()
    }

    private fun onPercent() {
        val currentValue = tvResult.text.toString().toDoubleOrNull() ?: return
        val result = calculatorEngine.percentage(currentValue)

        tvExpression.text = "$currentValue%"
        tvResult.text = calculatorEngine.formatResult(result)
        clearError()
    }

    private fun onConstant(value: String) {
        tvResult.text = value
        clearError()
    }

    private fun onPowerOperation(currentText: String) {
        val (resultText, expressionText) = calculatorEngine.onOperationPressed("^", currentText)
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
        fun newInstance(calculatorEngine: CalculatorEngine): ScientificCalculatorFragment {
            return ScientificCalculatorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("calculator", calculatorEngine)
                }
            }
        }
    }
}
