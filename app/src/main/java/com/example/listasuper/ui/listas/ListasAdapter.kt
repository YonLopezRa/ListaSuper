package com.example.listasuper.ui.listas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listasuper.R
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
        val context = holder.itemView.context

        with(holder.binding) {
            tvNombreLista.text = lista.nombre

            // Formatear el gasto como moneda
            val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("es", "MX"))
            tvPrecio.text = format.format(lista.gastoTotal)

            // Formatear fecha
            val sdf = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
            tvFecha.text = sdf.format(java.util.Date(lista.fechaCreacion))

            // 🎨 MAGIA: Cambiar colores según la tienda
            when (lista.tienda) {
                "walmart" -> {
                    cardLista.setCardBackgroundColor(ContextCompat.getColor(context, R.color.walmartBg))
                    ivTiendaLogo.setBackgroundColor(ContextCompat.getColor(context, R.color.walmartBlue))
                    ivTiendaLogo.setImageResource(R.drawable.ic_walmart)
                    ivTiendaLogo.setColorFilter(ContextCompat.getColor(context, android.R.color.white))
                }

                "chedraui" -> {
                    cardLista.setCardBackgroundColor(ContextCompat.getColor(context, R.color.chedrauiBg))
                    ivTiendaLogo.setBackgroundColor(ContextCompat.getColor(context, R.color.chedrauiOrange))
                    ivTiendaLogo.setImageResource(R.drawable.ic_chedraui)
                    ivTiendaLogo.setColorFilter(ContextCompat.getColor(context, android.R.color.white))
                }

                "target" -> {
                    cardLista.setCardBackgroundColor(ContextCompat.getColor(context, R.color.targetBg))
                    ivTiendaLogo.setBackgroundColor(ContextCompat.getColor(context, R.color.targetRed))
                    ivTiendaLogo.setImageResource(R.drawable.ic_target)
                    ivTiendaLogo.setColorFilter(ContextCompat.getColor(context, android.R.color.white))
                }

                "lacomer" -> {
                    cardLista.setCardBackgroundColor(ContextCompat.getColor(context, R.color.laComerBg))
                    ivTiendaLogo.setBackgroundColor(ContextCompat.getColor(context, R.color.laComerGreen))
                    ivTiendaLogo.setImageResource(R.drawable.ic_shopping_cart_foreground)
                    ivTiendaLogo.setColorFilter(ContextCompat.getColor(context, android.R.color.white))
                }

                "soriana" -> {
                    cardLista.setCardBackgroundColor(ContextCompat.getColor(context, R.color.sorianaHiperBg))
                    ivTiendaLogo.setBackgroundColor(ContextCompat.getColor(context, R.color.sorianaHiperRed))
                    ivTiendaLogo.setImageResource(R.drawable.ic_soriana)
                    ivTiendaLogo.setColorFilter(ContextCompat.getColor(context, android.R.color.white))
                }

                else -> {
                    cardLista.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surfaceCard))
                    ivTiendaLogo.setBackgroundColor(ContextCompat.getColor(context, R.color.borderColor))
                    ivTiendaLogo.setColorFilter(ContextCompat.getColor(context, R.color.textSecondary))
                }
            }

            root.setOnClickListener { onClick(lista) }
            ivEliminar.setOnClickListener { onDelete(lista) }
        }
    }
}