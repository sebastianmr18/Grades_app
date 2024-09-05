package com.example.grades_app

import android.os.Bundle
import android.widget.Toast
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.grades_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        changeOnFocus()
        calculateFinalGrade()
        activateButton()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    class Student {
        var name: String
        var course: String
        var grade1: Double
        var grade2: Double
        var finalGrade: Double

        constructor(name: String, course: String, grade1: Double, grade2: Double, isRepeater: Boolean){
            this.name = name
            this.course = course
            this.grade1 = grade1
            this.grade2 = grade2

            this.finalGrade = calculateFinalGrade(isRepeater)
        }

        fun calculateFinalGrade(isRepeater: Boolean): Double{
            return (grade1 + grade2) / 2 - if (isRepeater) 0.5 else 0.0
        }
    }

    private fun changeOnFocus(){
        binding.editTextStudentName.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editTextStudentName.text.toString() == "Nombre estudiante") {
                binding.editTextStudentName.text = null
            }
        }
        binding.editTextCourseName.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editTextCourseName.text.toString() == "Nombre curso") {
                binding.editTextCourseName.text = null
            }
        }
        binding.editTextGrade1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editTextGrade1.text.toString() == "Nota 1") {
                binding.editTextGrade1.text = null
            }
        }
        binding.editTextGrade2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editTextGrade2.text.toString() == "Nota 2") {
                binding.editTextGrade2.text = null
            }
        }
    }

    private fun cleanForm(){
        binding.editTextStudentName.text = null
        binding.editTextCourseName.text = null
        binding.editTextGrade1.text = null
        binding.editTextGrade2.text = null
        binding.checkBoxRepeater.isChecked = false
        binding.buttonCalculate.isEnabled = false
    }

    private fun printInformation(student: Student){
        var feedbackText: String
        if (student.grade1 in 0.0..5.0 && student.grade2 in 0.0..5.0) {
            binding.textViewInformation.text =
                "Nombre: ${student.name}\n" +
                        "Curso: ${student.course}\n" +
                        "Nota 1: ${student.grade1}\n" +
                        "Nota 2: ${student.grade2}\n" +
                        "Nota Final: ${student.finalGrade}"
            if (student.finalGrade < 3.0) {
                feedbackText = "Has reprobado"
            } else
                feedbackText = "Has aprobado"
        } else feedbackText = "Ingrese notas entre 0.0 y 5.0"
        Toast.makeText(this, feedbackText, Toast.LENGTH_LONG).show()
    }

    private fun calculateFinalGrade(){
        binding.buttonCalculate.setOnClickListener{
            val student = Student(
                binding.editTextStudentName.text.toString(),
                binding.editTextCourseName.text.toString(),
                binding.editTextGrade1.text.toString().toDouble(),
                binding.editTextGrade2.text.toString().toDouble(),
                binding.checkBoxRepeater.isChecked
            )
            printInformation(student)
            cleanForm()
        }
    }

    // Función de extensión para validar los campos
    private fun isValidField(editText: EditText, defaultText: String): Boolean {
        return editText.text?.isNotEmpty() == true && editText.text.toString() != defaultText
    }

    private fun activateButton() {
        val textWatcher = object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                checkFields()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        // Agregar text watchers a los campos
        binding.editTextStudentName.addTextChangedListener(textWatcher)
        binding.editTextCourseName.addTextChangedListener(textWatcher)
        binding.editTextGrade1.addTextChangedListener(textWatcher)
        binding.editTextGrade2.addTextChangedListener(textWatcher)
    }

    private fun checkFields() {
        // Verificar todos los campos de una vez usando la función de extensión
        binding.buttonCalculate.isEnabled = isValidField(binding.editTextStudentName, "Nombre estudiante") &&
                isValidField(binding.editTextCourseName, "Nombre curso") &&
                isValidField(binding.editTextGrade1, "Nota 1") &&
                isValidField(binding.editTextGrade2, "Nota 2")
    }



}