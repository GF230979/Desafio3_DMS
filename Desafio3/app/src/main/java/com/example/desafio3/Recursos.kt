package com.example.desafio3

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Recursos(context: Context, private val recurso: Model? = null, private val onSave: (Model) -> Unit) : Dialog(context) {
    private lateinit var titulo: EditText
    private lateinit var descripcion: EditText
    private lateinit var tipo: EditText
    private lateinit var enlace: EditText
    private lateinit var imagen: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnCancelar: Button

    init {
        setContentView(R.layout.recurso_activity)
        setup()
        recurso?.let { populateFields(it) }
    }

    private fun setup() {
        titulo = findViewById(R.id.titulo)
        descripcion = findViewById(R.id.descripcion)
        tipo = findViewById(R.id.tipo)
        enlace = findViewById(R.id.enlace)
        imagen = findViewById(R.id.imagen)
        btnAgregar = findViewById(R.id.btnagg)
        btnCancelar = findViewById(R.id.btncancel)

        btnAgregar.setOnClickListener {
            if (validateFields()) {
                val nuevoRecurso = Model(
                    id = recurso?.id ?: 0,
                    titulo = titulo.text.toString(),
                    descripcion = descripcion.text.toString(),
                    tipo = tipo.text.toString(),
                    enlace = enlace.text.toString(),
                    imagen = imagen.text.toString()
                )
                onSave(nuevoRecurso)
                dismiss()
            }
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }
    }

    private fun populateFields(recurso: Model) {
        titulo.setText(recurso.titulo)
        descripcion.setText(recurso.descripcion)
        tipo.setText(recurso.tipo)
        enlace.setText(recurso.enlace)
        imagen.setText(recurso.imagen)
    }

    private fun validateFields(): Boolean {
        return when {
            titulo.text.isEmpty() -> {
                showToast("Por favor ingresa un título")
                false
            }
            descripcion.text.isEmpty() -> {
                showToast("Por favor ingresa una descripción")
                false
            }
            tipo.text.isEmpty() -> {
                showToast("Por favor ingresa un tipo")
                false
            }
            enlace.text.isEmpty() -> {
                showToast("Por favor ingresa un enlace")
                false
            }
            imagen.text.isEmpty() -> {
                showToast("Por favor ingresa una URL de imagen")
                false
            }
            else -> true
        }
    }

    private fun showToast(mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
    }
}
