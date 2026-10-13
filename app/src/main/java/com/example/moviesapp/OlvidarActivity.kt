package com.example.moviesapp

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class OlvidarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvidar)

        val tilCorreo = findViewById<TextInputLayout>(R.id.til_correo_rec)
        val campoCorreo = findViewById<TextInputEditText>(R.id.campo_correo_rec)
        val btnEnviar = findViewById<Button>(R.id.btn_enviar_enlace)
        val btnCancelar = findViewById<Button>(R.id.btn_cancelar)

        btnEnviar.setOnClickListener {
            tilCorreo.error = null
            val correo = campoCorreo.text.toString().trim()

            when {
                correo.isEmpty() ->
                    tilCorreo.error = "Ingresa tu correo electronico"
                !Patterns.EMAIL_ADDRESS.matcher(correo).matches() ->
                    tilCorreo.error = "Correo no valido"
                else ->
                    // Simulacion del envio
                    Toast.makeText(
                        this,
                        "Te enviamos un enlace de recuperacion a $correo",
                        Toast.LENGTH_LONG
                    ).show()
            }
        }

        btnCancelar.setOnClickListener { finish() }  // regresa al Login
    }
}
