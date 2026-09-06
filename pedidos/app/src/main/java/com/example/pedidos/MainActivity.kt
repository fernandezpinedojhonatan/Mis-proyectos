package com.example.pedidos

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Lista de productos con sus datos
    private val productos = mapOf(
        // Muebles
        "sillas" to Producto("Sillas", "Mueble", 250.0, "Sillas de madera modernas"),
        "mesa" to Producto("Mesa", "Mueble", 450.0, "Mesa de comedor de 4 puestos"),
        "escritorio" to Producto("Escritorio", "Mueble", 350.0, "Escritorio ejecutivo con cajones"),
        "sillon" to Producto("Sillón", "Mueble", 600.0, "Sillón reclinable de cuero"),

        // Electrónicos
        "mouse" to Producto("Mouse", "Electrónico", 35.0, "Mouse inalámbrico USB"),
        "laptop" to Producto("Laptop", "Electrónico", 850.0, "Laptop 16GB RAM, 512GB SSD"),
        "teclado" to Producto("Teclado", "Electrónico", 55.0, "Teclado mecánico RGB"),
        "monitor" to Producto("Monitor", "Electrónico", 300.0, "Monitor 24 pulgadas Full HD"),
        "auriculares" to Producto("Auriculares", "Electrónico", 80.0, "Auriculares con cancelación de ruido")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias a los elementos
        val etProducto = findViewById<EditText>(R.id.etProducto)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)

        // Acción del botón Buscar
        btnBuscar.setOnClickListener {
            val productoBuscado = etProducto.text.toString().trim().lowercase()

            // VALIDACIÓN 1: Campo vacío
            if (productoBuscado.isEmpty()) {
                mostrarAlertDialog(
                    getString(R.string.error_titulo),
                    getString(R.string.error_campo_vacio)
                )
                return@setOnClickListener
            }

            // Buscar el producto
            val productoEncontrado = productos[productoBuscado]

            // VALIDACIÓN 2: Mostrar producto si existe
            if (productoEncontrado != null) {
                mostrarProductoEncontrado(productoEncontrado)
            } else {
                mostrarAlertDialog(
                    "Producto no encontrado",
                    "No se encontró el producto '$productoBuscado'"
                )
            }
        }
    }

    // Función para mostrar AlertDialog simple
    private fun mostrarAlertDialog(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton(getString(R.string.boton_aceptar)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Función para mostrar el producto encontrado
    private fun mostrarProductoEncontrado(producto: Producto) {
        val mensaje = """
            Nombre: ${producto.nombre}
            Categoría: ${producto.categoria}
            Precio: $${String.format("%.2f", producto.precio)}
            Descripción: ${producto.descripcion}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.producto_encontrado_titulo))
            .setMessage(mensaje)
            .setPositiveButton(getString(R.string.boton_aceptar)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Clase de datos para los productos
    data class Producto(
        val nombre: String,
        val categoria: String,
        val precio: Double,
        val descripcion: String
    )
}