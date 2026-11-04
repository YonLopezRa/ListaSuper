package com.example.listasuper.ui.agregar

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.listasuper.databinding.FragmentAgregarProductoBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class AgregarProductoFragment : Fragment() {

    private var _binding: FragmentAgregarProductoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AgregarProductoViewModel by viewModels()
    private val args: AgregarProductoFragmentArgs by navArgs()

    // Guardar valores escaneados
    private var codigoBarras: String? = null
    private var imagenUrl: String? = null

    // Launcher para escanear
    private val scanLauncher = registerForActivityResult(ScanContract()) { result ->
        try {
            result.contents?.let { codigo ->
                codigoBarras = codigo
                viewModel.buscarPorCodigo(codigo, args.listaId)
            } ?: run {
                Toast.makeText(requireContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("SCAN_CRASH", "Error después de escanear", e)
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAgregarProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnEscanear.setOnClickListener { lanzarEscaner() }
        binding.btnGuardar.setOnClickListener { guardar() }

        // Observar producto encontrado
        viewModel.productoEncontrado.observe(viewLifecycleOwner) { producto ->
            if (producto != null) {
                binding.etNombre.setText(producto.nombre)
                binding.etMarca.setText(producto.marca ?: "")
                hideKeyboard()
                imagenUrl = producto.imagenUrl

                if (!producto.imagenUrl.isNullOrEmpty()) {
                    binding.ivPreview.visibility = View.VISIBLE
                    binding.ivPreview.setImageDrawable(null)
                    binding.ivPreview.load(producto.imagenUrl) {
                        crossfade(true)
                        // memoryCacheKey(producto.imagenUrl) // Si sigues viendo la misma imagen, descomenta esta línea
                    }
                } else {
                    binding.ivPreview.visibility = View.GONE
                    binding.ivPreview.setImageDrawable(null)
                }
                Toast.makeText(requireContext(), "Producto encontrado: ${producto.nombre}", Toast.LENGTH_SHORT).show()

            } else {
                // Si no se encontró ningún producto, limpiamos TODA la vista
                binding.ivPreview.visibility = View.GONE
                binding.ivPreview.setImageDrawable(null)
                imagenUrl = null
                Toast.makeText(requireContext(), "Producto no encontrado en la base de datos. Completa manualmente.", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.loadingOverlay.visibility = if (cargando) View.VISIBLE else View.GONE

            binding.btnEscanear.isEnabled = !cargando
            binding.btnGuardar.isEnabled = !cargando
        }
    }

    private fun lanzarEscaner() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            setPrompt("Apunta al código de barras del producto")
            setCameraId(0)
            setBeepEnabled(true)
            setOrientationLocked(false)
        }
        scanLauncher.launch(options)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
                as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun guardar() {
        val nombre = binding.etNombre.text.toString().trim()
        val marca = binding.etMarca.text.toString().trim().ifEmpty { null }
        val cantidad = binding.etCantidad.text.toString().toIntOrNull() ?: 1

        if (nombre.isEmpty()) {
            binding.tilNombre.error = "El nombre es obligatorio"
            return
        }
        binding.tilNombre.error = null

        viewModel.guardarProducto(nombre, marca, cantidad, args.listaId, codigoBarras, imagenUrl)
        Toast.makeText(requireContext(), "Producto agregado", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}