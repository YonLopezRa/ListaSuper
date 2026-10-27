package com.example.listasuper.ui.listas

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listasuper.R
import com.example.listasuper.databinding.FragmentListasBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListasFragment : Fragment() {

    private var _binding: FragmentListasBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListasViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListasAdapter(
            onClick = { lista ->
                val action = ListasFragmentDirections.actionListasToDetalle(lista.id)
                findNavController().navigate(action)
            },
            onDelete = { lista -> confirmarEliminar(lista) }
        )

        binding.rvListas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListas.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listas.collectLatest { listas ->
                adapter.submitList(listas)
                binding.tvVacio.visibility = if (listas.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.fabAgregarLista.setOnClickListener { mostrarDialogoNuevaLista() }
    }

    private fun mostrarDialogoNuevaLista() {
        val et = TextInputEditText(requireContext()).apply {
            hint = "Nombre de la lista"
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nueva Lista")
            .setView(et)
            .setPositiveButton("Crear") { _, _ ->
                val nombre = et.text.toString().trim()
                if (nombre.isNotEmpty()) viewModel.crearLista(nombre)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminar(lista: com.example.listasuper.data.local.entities.ListaEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar lista")
            .setMessage("¿Eliminar '${lista.nombre}'?")
            .setPositiveButton("Sí") { _, _ -> viewModel.eliminarLista(lista) }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}