package com.example.listasuper.ui.detalle

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.listasuper.data.local.entities.ProductoEntity
import com.example.listasuper.databinding.ItemProductoBinding

class ProductoAdapter(
    private val onToggle: (ProductoEntity) -> Unit,
    private val onDelete: (ProductoEntity) -> Unit
) : ListAdapter<ProductoEntity, ProductoAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ProductoEntity>() {
            override fun areItemsTheSame(a: ProductoEntity, b: ProductoEntity) = a.id == b.id
            override fun areContentsTheSame(a: ProductoEntity, b: ProductoEntity) = a == b
        }
    }

    inner class VH(val binding: ItemProductoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = getItem(position)
        with(holder.binding) {
            tvNombre.text = p.nombre
            tvMarca.text = p.marca ?: "Sin marca"
            cbComprado.isChecked = p.comprado

            // Tachado si está comprado
            tvNombre.paintFlags = if (p.comprado)
                tvNombre.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
                tvNombre.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            if (!p.imagenUrl.isNullOrEmpty()) {
                ivImagen.load(p.imagenUrl)
            }

            cbComprado.setOnClickListener { onToggle(p) }
            ivEliminar.setOnClickListener { onDelete(p) }
        }
    }
}