package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProgrammingCalculatorFragment : Fragment() {

    private lateinit var tvResult: TextView
    private lateinit var tvError: TextView
    private lateinit var rgEquationType: RadioGroup
    private lateinit var containerQuadratic: View
    private lateinit var containerCubic: View
    private lateinit var containerSystem: View
    private lateinit var calculatorEngine: CalculatorEngine

    // Quadratic inputs
    private lateinit var etQuadA: EditText
    private lateinit var etQuadB: EditText
    private lateinit var etQuadC: EditText

    // Cubic inputs
    private lateinit var etCubA: EditText
    private lateinit var etCubB: EditText
    private lateinit var etCubC: EditText
    private lateinit var etCubD: EditText

    // System of equations inputs
    private lateinit var etSys1A: EditText
    private lateinit var etSys1B: EditText
    private lateinit var etSys1C: EditText
    private lateinit var etSys2A: EditText
    private lateinit var etSys2B: EditText
    private lateinit var etSys2C: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_programming_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calculatorEngine = arguments?.getSerializable("calculator") as? CalculatorEngine
            ?: CalculatorEngine()

        // Khởi tạo các view
        tvResult = view.findViewById(R.id.tv_result)
        tvError = view.findViewById(R.id.tv_error)
        rgEquationType = view.findViewById(R.id.rg_equation_type)

        // Containers
        containerQuadratic = view.findViewById(R.id.container_quadratic)
        containerCubic = view.findViewById(R.id.container_cubic)
        containerSystem = view.findViewById(R.id.container_system)

        // Quadratic EditTexts
        etQuadA = view.findViewById(R.id.et_quad_a)
        etQuadB = view.findViewById(R.id.et_quad_b)
        etQuadC = view.findViewById(R.id.et_quad_c)

        // Cubic EditTexts
        etCubA = view.findViewById(R.id.et_cub_a)
        etCubB = view.findViewById(R.id.et_cub_b)
        etCubC = view.findViewById(R.id.et_cub_c)
        etCubD = view.findViewById(R.id.et_cub_d)

        // System EditTexts
        etSys1A = view.findViewById(R.id.et_sys1_a)
        etSys1B = view.findViewById(R.id.et_sys1_b)
        etSys1C = view.findViewById(R.id.et_sys1_c)
        etSys2A = view.findViewById(R.id.et_sys2_a)
        etSys2B = view.findViewById(R.id.et_sys2_b)
        etSys2C = view.findViewById(R.id.et_sys2_c)

        // Setup radio group listener
        rgEquationType.setOnCheckedChangeListener { _, checkedId ->
            updateContainerVisibility(checkedId)
        }

        // Solve buttons
        val btnSolveQuad = view.findViewById<Button>(R.id.btn_solve_quad)
        val btnSolveCub = view.findViewById<Button>(R.id.btn_solve_cub)
        val btnSolveSys = view.findViewById<Button>(R.id.btn_solve_sys)
        val btnClear = view.findViewById<Button>(R.id.btn_clear)

        btnSolveQuad.setOnClickListener { solveQuadratic() }
        btnSolveCub.setOnClickListener { solveCubic() }
        btnSolveSys.setOnClickListener { solveSystemOfEquations() }
        btnClear.setOnClickListener { clearAll() }

        // Set default visibility
        updateContainerVisibility(R.id.rb_quadratic)
    }

    private fun updateContainerVisibility(checkedId: Int) {
        containerQuadratic.visibility = if (checkedId == R.id.rb_quadratic) View.VISIBLE else View.GONE
        containerCubic.visibility = if (checkedId == R.id.rb_cubic) View.VISIBLE else View.GONE
        containerSystem.visibility = if (checkedId == R.id.rb_system) View.VISIBLE else View.GONE
        clearResult()
    }

    private fun solveQuadratic() {
        try {
            val a = etQuadA.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số a")
            val b = etQuadB.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số b")
            val c = etQuadC.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số c")

            val (result, error) = calculatorEngine.solveQuadratic(a, b, c)

            if (error != null) {
                showError(error)
            } else {
                showResult("Phương trình: ${a}x² + ${b}x + ${c} = 0\n\nKết quả:\n$result")
            }
        } catch (e: Exception) {
            showError("Lỗi: ${e.message}")
        }
    }

    private fun solveCubic() {
        try {
            val a = etCubA.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số a")
            val b = etCubB.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số b")
            val c = etCubC.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số c")
            val d = etCubD.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số d")

            val (result, error) = calculatorEngine.solveCubic(a, b, c, d)

            if (error != null) {
                showError(error)
            } else {
                showResult("Phương trình: ${a}x³ + ${b}x² + ${c}x + ${d} = 0\n\nKết quả:\n$result")
            }
        } catch (e: Exception) {
            showError("Lỗi: ${e.message}")
        }
    }

    private fun solveSystemOfEquations() {
        try {
            // Hệ phương trình:
            // a1*x + b1*y = c1
            // a2*x + b2*y = c2
            val a1 = etSys1A.text.toString().toDoubleOrNull() ?: return showError("Nhập a1")
            val b1 = etSys1B.text.toString().toDoubleOrNull() ?: return showError("Nhập b1")
            val c1 = etSys1C.text.toString().toDoubleOrNull() ?: return showError("Nhập c1")

            val a2 = etSys2A.text.toString().toDoubleOrNull() ?: return showError("Nhập a2")
            val b2 = etSys2B.text.toString().toDoubleOrNull() ?: return showError("Nhập b2")
            val c2 = etSys2C.text.toString().toDoubleOrNull() ?: return showError("Nhập c2")

            // Tính định thức
            val det = a1 * b2 - a2 * b1

            val result = when {
                det == 0.0 && a1 * c2 == a2 * c1 -> {
                    "Hệ có vô số nghiệm"
                }
                det == 0.0 -> {
                    "Hệ vô nghiệm"
                }
                else -> {
                    val x = (c1 * b2 - c2 * b1) / det
                    val y = (a1 * c2 - a2 * c1) / det
                    "Hệ phương trình:\n${a1}x + ${b1}y = ${c1}\n${a2}x + ${b2}y = ${c2}\n\nKết quả:\nx = ${calculatorEngine.formatResult(x)}\ny = ${calculatorEngine.formatResult(y)}"
                }
            }

            showResult(result)
        } catch (e: Exception) {
            showError("Lỗi: ${e.message}")
        }
    }

    private fun clearAll() {
        etQuadA.text.clear()
        etQuadB.text.clear()
        etQuadC.text.clear()

        etCubA.text.clear()
        etCubB.text.clear()
        etCubC.text.clear()
        etCubD.text.clear()

        etSys1A.text.clear()
        etSys1B.text.clear()
        etSys1C.text.clear()
        etSys2A.text.clear()
        etSys2B.text.clear()
        etSys2C.text.clear()

        clearResult()
    }

    private fun showResult(message: String) {
        tvResult.text = message
        tvResult.visibility = View.VISIBLE
        tvError.visibility = View.GONE
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
        tvResult.visibility = View.GONE
    }

    private fun clearResult() {
        tvResult.text = ""
        tvError.text = ""
        tvResult.visibility = View.GONE
        tvError.visibility = View.GONE
    }

    companion object {
        fun newInstance(calculatorEngine: CalculatorEngine): ProgrammingCalculatorFragment {
            return ProgrammingCalculatorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("calculator", calculatorEngine)
                }
            }
        }
    }
}
