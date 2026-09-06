package com.example.gestindenotas

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etPp1: EditText
    private lateinit var etPp2: EditText
    private lateinit var etPp3: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnOrdenarAlfabetico: Button
    private lateinit var btnOrdenarNumerico: Button
    private lateinit var btnLimpiar: Button
    private lateinit var tvInfo: TextView
    private lateinit var tvMensajeLista: TextView
    private lateinit var llListaEstudiantes: LinearLayout

    private val listaEstudiantes = mutableListOf<Estudiante>()
    private val df = DecimalFormat("#.##")

    data class Estudiante(
        val nombre: String,
        val pp1: Double,
        val pp2: Double,
        val pp3: Double
    ) {
        fun getPromedio(): Double = (pp1 + pp2 + pp3) / 3

        fun getEstado(): String {
            val promedio = getPromedio()
            return when {
                promedio >= 13 -> "APROBADO"
                promedio in 10.0..12.9 -> "SUSTITUTORIO"
                else -> "DESAPROBADO"
            }
        }

        fun getColorEstado(): Int {
            val promedio = getPromedio()
            return when {
                promedio >= 13 -> R.color.greenDark
                promedio in 10.0..12.9 -> R.color.orangeDark
                else -> R.color.redDark
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setupListeners()
    }

    private fun initViews() {
        etNombre = findViewById(R.id.etNombre)
        etPp1 = findViewById(R.id.etPp1)
        etPp2 = findViewById(R.id.etPp2)
        etPp3 = findViewById(R.id.etPp3)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnOrdenarAlfabetico = findViewById(R.id.btnOrdenarAlfabetico)
        btnOrdenarNumerico = findViewById(R.id.btnOrdenarNumerico)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        tvInfo = findViewById(R.id.tvInfo)
        tvMensajeLista = findViewById(R.id.tvMensajeLista)
        llListaEstudiantes = findViewById(R.id.llListaEstudiantes)
    }

    private fun setupListeners() {
        btnGuardar.setOnClickListener { guardarNota() }
        btnOrdenarAlfabetico.setOnClickListener { ordenarAlfabeticamente() }
        btnOrdenarNumerico.setOnClickListener { ordenarNumericamente() }
        btnLimpiar.setOnClickListener { limpiarTodo() }
    }

    private fun guardarNota() {
        val nombre = etNombre.text.toString().trim()
        val pp1Str = etPp1.text.toString().trim()
        val pp2Str = etPp2.text.toString().trim()
        val pp3Str = etPp3.text.toString().trim()

        if (nombre.isEmpty() || pp1Str.isEmpty() || pp2Str.isEmpty() || pp3Str.isEmpty()) {
            mostrarAlertDialog(
                getString(R.string.alert_titulo_error),
                getString(R.string.alert_mensaje_vacio)
            )
            return
        }

        try {
            val pp1 = pp1Str.toDouble()
            val pp2 = pp2Str.toDouble()
            val pp3 = pp3Str.toDouble()

            if (pp1 !in 0.0..20.0 || pp2 !in 0.0..20.0 || pp3 !in 0.0..20.0) {
                mostrarAlertDialog(
                    getString(R.string.alert_titulo_error),
                    getString(R.string.alert_mensaje_nota_invalida)
                )
                return
            }

            val estudiante = Estudiante(nombre, pp1, pp2, pp3)
            listaEstudiantes.add(estudiante)
            mostrarInfoEstudiante(estudiante)
            actualizarListaVisual()
            limpiarCampos()

        } catch (e: NumberFormatException) {
            mostrarAlertDialog(
                getString(R.string.alert_titulo_error),
                getString(R.string.alert_mensaje_numero_invalido)
            )
        }
    }

    private fun mostrarInfoEstudiante(estudiante: Estudiante) {
        tvInfo.text = """
            Nombre: ${estudiante.nombre}
            Notas: PP1=${estudiante.pp1} | PP2=${estudiante.pp2} | PP3=${estudiante.pp3}
            Promedio: ${df.format(estudiante.getPromedio())}
            Estado: ${estudiante.getEstado()}
        """.trimIndent()
    }

    private fun actualizarListaVisual() {
        llListaEstudiantes.removeAllViews()

        if (listaEstudiantes.isEmpty()) {
            tvMensajeLista.visibility = View.VISIBLE
            llListaEstudiantes.addView(tvMensajeLista)
            return
        }

        tvMensajeLista.visibility = View.GONE

        for (estudiante in listaEstudiantes) {
            val itemView = crearVistaEstudiante(estudiante)
            llListaEstudiantes.addView(itemView)
        }
    }

    private fun crearVistaEstudiante(estudiante: Estudiante): View {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 12, 16, 12)
            setBackgroundColor(getColor(R.color.cardBackground))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 4)
            }
        }

        val columnaIzquierda = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        val tvNombre = TextView(this).apply {
            text = estudiante.nombre
            textSize = 16f
            setTextColor(getColor(R.color.textPrimary))
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        val tvNotas = TextView(this).apply {
            text = "PP1: ${estudiante.pp1}  PP2: ${estudiante.pp2}  PP3: ${estudiante.pp3}"
            textSize = 13f
            setTextColor(getColor(R.color.textSecondary))
        }

        columnaIzquierda.addView(tvNombre)
        columnaIzquierda.addView(tvNotas)

        val columnaDerecha = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.END
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val tvPromedio = TextView(this).apply {
            text = df.format(estudiante.getPromedio())
            textSize = 18f
            setTextColor(getColor(R.color.colorPrimary))
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        val tvEstado = TextView(this).apply {
            text = estudiante.getEstado()
            textSize = 12f
            setTextColor(getColor(estudiante.getColorEstado()))
        }

        columnaDerecha.addView(tvPromedio)
        columnaDerecha.addView(tvEstado)

        layout.addView(columnaIzquierda)
        layout.addView(columnaDerecha)

        return layout
    }

    private fun ordenarAlfabeticamente() {
        if (listaEstudiantes.isEmpty()) {
            mostrarAlertDialog(
                getString(R.string.alert_titulo_info),
                getString(R.string.alert_mensaje_sin_datos)
            )
            return
        }
        listaEstudiantes.sortBy { it.nombre }
        actualizarListaVisual()
        tvInfo.text = getString(R.string.msg_ordenado_alfabetico)
    }

    private fun ordenarNumericamente() {
        if (listaEstudiantes.isEmpty()) {
            mostrarAlertDialog(
                getString(R.string.alert_titulo_info),
                getString(R.string.alert_mensaje_sin_datos)
            )
            return
        }
        listaEstudiantes.sortByDescending { it.getPromedio() }
        actualizarListaVisual()
        tvInfo.text = getString(R.string.msg_ordenado_numerico)
    }

    private fun limpiarCampos() {
        etNombre.text.clear()
        etPp1.text.clear()
        etPp2.text.clear()
        etPp3.text.clear()
        etNombre.requestFocus()
    }

    private fun limpiarTodo() {
        listaEstudiantes.clear()
        actualizarListaVisual()
        limpiarCampos()
        tvInfo.text = getString(R.string.msg_esperando)
    }

    private fun mostrarAlertDialog(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton(getString(R.string.alert_boton_aceptar)) { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}