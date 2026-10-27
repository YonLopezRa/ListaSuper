package com.example.listasuper.ui.listas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listasuper.data.local.entities.ListaEntity
import com.example.listasuper.databinding.ItemListaBinding
import java.text.SimpleDateFormat
import java.util.*

class ListasAdapter(
    private val onClick: (ListaEntity) -> Unit,
    private val onDelete: (ListaEntity) -> Unit
) : ListAdapter<ListaEntity, ListasAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ListaEntity>() {
            override fun areItemsTheSame(a: ListaEntity, b: ListaEntity) = a.id == b.id
            override fun areContentsTheSame(a: ListaEntity, b: ListaEntity) = a == b
        }
    }

    inner class ViewHolder(val binding: ItemListaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lista = getItem(position)
        with(holder.binding) {
            tvNombreLista.text = lista.nombre
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            tvFecha.text = sdf.format(Date(lista.fechaCreacion))
            root.setOnClickListener { onClick(lista) }
            ivEliminar.setOnClickListener { onDelete(lista) }
        }
    }
}