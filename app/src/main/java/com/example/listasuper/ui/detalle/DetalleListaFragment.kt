package com.example.listasuper.ui.detalle

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listasuper.databinding.FragmentDetalleListaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetalleListaFragment : Fragment() {

    private var _binding: FragmentDetalleListaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetalleListaViewModel by viewModels()
    private val args: DetalleListaFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetalleListaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar
        binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        // Cargar nombre de la lista
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.obtenerLista(args.listaId).collectLatest { lista ->
                lista?.let { binding.toolbar.title = it.nombre }
            }
        }

        // Adapter de productos
        val adapter = ProductoAdapter(
            onToggle = { viewModel.toggleComprado(it) },
            onDelete = { confirmarEliminar(it) }
        )
        binding.rvProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductos.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.obtenerProductos(args.listaId).collectLatest { productos ->
                adapter.submitList(productos)
                binding.tvVacio.visibility = if (productos.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.fabAgregarProducto.setOnClickListener {
            val action = DetalleListaFragmentDirections
                .actionDetalleToAgregar(args.listaId)
            findNavController().navigate(action)
        }
    }

    private fun confirmarEliminar(p: com.example.listasuper.data.local.entities.ProductoEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar producto")
            .setMessage("¿Eliminar '${p.nombre}'?")
            .setPositiveButton("Sí") { _, _ -> viewModel.eliminarProducto(p) }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}