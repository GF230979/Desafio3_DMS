package com.example.desafio3

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adaptador(
    private var listaRecursos: List<Model>,
    private val onEdit: (Model) -> Unit,
    private val onDelete: (Model) -> Unit
) : RecyclerView.Adapter<Adaptador.RecursoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_recurso, parent, false)
        return RecursoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recursoActual = listaRecursos[position]
        holder.bindData(recursoActual)
    }

    override fun getItemCount(): Int = listaRecursos.size

    fun actualizarRecursos(nuevosRecursos: List<Model>) {
        listaRecursos = nuevosRecursos
        notifyDataSetChanged()
    }

    inner class RecursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tituloTextView: TextView = itemView.findViewById(R.id.titulo)
        private val descripcionTextView: TextView = itemView.findViewById(R.id.descripcion)
        private val imagenView: ImageView = itemView.findViewById(R.id.imagen)
        private val botonEditar: Button = itemView.findViewById(R.id.btnedit)
        private val botonEliminar: Button = itemView.findViewById(R.id.btn_delete)
        private val botonInfo: Button = itemView.findViewById(R.id.btn_info)

        fun bindData(recurso: Model) {
            tituloTextView.text = recurso.titulo
            descripcionTextView.text = recurso.descripcion


            Picasso.get()
                .load(recurso.imagen)
                .into(imagenView)

            botonEditar.setOnClickListener {
                onEdit(recurso)
            }

            botonEliminar.setOnClickListener {
                onDelete(recurso)
            }

            botonInfo.setOnClickListener {
                val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse(recurso.enlace))
                itemView.context.startActivity(intentWeb)
            }
        }
    }
}
