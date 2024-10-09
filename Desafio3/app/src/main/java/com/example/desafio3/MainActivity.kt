package com.example.desafio3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val baseUrl = "https://6700c2924da5bd237554bc2f.mockapi.io/"
    private lateinit var recursosListView: RecyclerView
    private lateinit var recursosAdapter: Adaptador
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeUI()
        loadRecursos()
    }

    private fun initializeUI() {
        recursosListView = findViewById(R.id.recursos)
        loadingIndicator = findViewById(R.id.progress)

        recursosListView.layoutManager = LinearLayoutManager(this)
        recursosAdapter = Adaptador(emptyList(), ::editRecurso, ::deleteRecurso)
        recursosListView.adapter = recursosAdapter

        val addResourceButton: Button = findViewById(R.id.btnadd)
        addResourceButton.setOnClickListener {
            displayCreateRecursoDialog()
        }
    }

    private fun loadRecursos() {
        lifecycleScope.launch {
            loadingIndicator.visibility = View.VISIBLE
            recursosListView.visibility = View.GONE

            val apiService = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SolicitudesAPI::class.java)

            try {
                val fetchedResources = apiService.getRecursos()
                recursosAdapter.actualizarRecursos(fetchedResources)
                recursosListView.visibility = View.VISIBLE

                if (fetchedResources.isEmpty()) {
                    showToastMessage("No se encontraron recursos")
                }
            } catch (exception: Exception) {
                handleException(exception)
            } finally {
                loadingIndicator.visibility = View.GONE
            }
        }
    }

    private fun displayCreateRecursoDialog() {
        val dialog = Recursos(this) { nuevoRecurso ->
            addNewRecurso(nuevoRecurso)
        }
        dialog.show()
    }

    private fun editRecurso(recurso: Model) {
        val dialog = Recursos(this, recurso) { updatedRecurso ->
            modifyRecurso(updatedRecurso)
        }
        dialog.show()
    }

    private fun deleteRecurso(recurso: Model) {
        removeRecurso(recurso)
    }

    private fun addNewRecurso(nuevoRecurso: Model) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(SolicitudesAPI::class.java)

        lifecycleScope.launch {
            try {
                val recursoCreado = apiService.createRecurso(nuevoRecurso)
                showToastMessage("Recurso creado")
                loadRecursos() // Actualiza la lista de recursos
            } catch (e: Exception) {
                showToastMessage("Error: ${e.message}")
            }
        }
    }

    private fun modifyRecurso(recurso: Model) {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val apiService = retrofit.create(SolicitudesAPI::class.java)

        lifecycleScope.launch {
            try {
                apiService.updateRecurso(recurso.id, recurso)
                showToastMessage("Recurso actualizado correctamnete")
                loadRecursos()
            } catch (e: Exception) {
                showToastMessage("Error: ${e.message}")
            }
        }
    }

    private fun removeRecurso(recurso: Model) {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val apiService = retrofit.create(SolicitudesAPI::class.java)

        lifecycleScope.launch {
            try {
                apiService.deleteRecurso(recurso.id)
                showToastMessage("Recurso eliminado correctamente")
                loadRecursos()
            } catch (e: Exception) {
                showToastMessage("Error: ${e.message}")
            }
        }
    }


    private fun showToastMessage(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleException(exception: Exception) {
        val message = when (exception) {
            is IOException -> "verifica tu conexión de internet"
            is HttpException -> "Error de la api: ${exception.code()}"
            else -> "Error: ${exception.message}"
        }
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }
}
