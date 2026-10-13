package com.example.moviesapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.util.Patterns
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar


class RegistroActivity : AppCompatActivity() {

    private var fechaNacimiento: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val tilNombre = findViewById<TextInputLayout>(R.id.til_nombre)
        val tilCorreo = findViewById<TextInputLayout>(R.id.til_correo)
        val tilContra = findViewById<TextInputLayout>(R.id.til_contra)
        val tilVerificar = findViewById<TextInputLayout>(R.id.til_verificar)
        val tilFecha = findViewById<TextInputLayout>(R.id.til_fecha)

        val campoNombre = findViewById<TextInputEditText>(R.id.campo_nombre)
        val campoCorreo = findViewById<TextInputEditText>(R.id.campo_correo_reg)
        val campoContra = findViewById<TextInputEditText>(R.id.campo_contra_reg)
        val campoVerificar = findViewById<TextInputEditText>(R.id.campo_verificar)
        val campoFecha = findViewById<TextInputEditText>(R.id.campo_fecha)

        val btnRegistrarse = findViewById<Button>(R.id.btn_registrarse)
        val btnVolver = findViewById<TextView>(R.id.btn_volver_login)

        // Abrir el selector de fecha (tocando el campo o el icono)
        val abrirDatePicker = {
            val hoy = Calendar.getInstance()
            val picker = DatePickerDialog(
                this,
                { _, anio, mes, dia ->
                    fechaNacimiento = Calendar.getInstance().apply {
                        set(anio, mes, dia, 0, 0, 0)
                    }
                    campoFecha.setText(String.format("%02d/%02d/%04d", dia, mes + 1, anio))
                    tilFecha.error = null
                },
                hoy.get(Calendar.YEAR) - 18,
                hoy.get(Calendar.MONTH),
                hoy.get(Calendar.DAY_OF_MONTH)
            )
            picker.datePicker.maxDate = System.currentTimeMillis()  // no fechas futuras
            picker.show()
        }
        campoFecha.setOnClickListener { abrirDatePicker() }
        tilFecha.setEndIconOnClickListener { abrirDatePicker() }

        btnRegistrarse.setOnClickListener {
            // Limpiar errores previos
            listOf(tilNombre, tilCorreo, tilContra, tilVerificar, tilFecha).forEach { it.error = null }

            val nombre = campoNombre.text.toString().trim()
            val correo = campoCorreo.text.toString().trim()
            val contra = campoContra.text.toString()
            val verificar = campoVerificar.text.toString()

            var valido = true

            // 1) Ningun campo vacio
            if (nombre.isEmpty()) { tilNombre.error = "El nombre es obligatorio"; valido = false }
            if (correo.isEmpty()) {
                tilCorreo.error = "El correo es obligatorio"; valido = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                tilCorreo.error = "Correo no valido"; valido = false
            }
            if (contra.isEmpty()) { tilContra.error = "La contrasena es obligatoria"; valido = false }
            if (verificar.isEmpty()) { tilVerificar.error = "Verifica tu contrasena"; valido = false }
            if (fechaNacimiento == null) { tilFecha.error = "La fecha de nacimiento es obligatoria"; valido = false }

            // 2) Contrasenas iguales
            if (contra.isNotEmpty() && verificar.isNotEmpty() && contra != verificar) {
                tilVerificar.error = "Las contrasenas no coinciden"; valido = false
            }

            // 3) Mayor de 18 anios
            fechaNacimiento?.let {
                if (calcularEdad(it) < 18) {
                    tilFecha.error = "Debes tener al menos 18 anios"; valido = false
                }
            }

            if (valido) {
                Toast.makeText(this, "Registro exitoso. Bienvenido, $nombre!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Revisa los campos marcados", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener { finish() }
    }

    private fun calcularEdad(nacimiento: Calendar): Int {
        val hoy = Calendar.getInstance()
        var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
        // Si aun no cumple anios este anio, restar 1
        if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) edad--
        return edad
    }
}
