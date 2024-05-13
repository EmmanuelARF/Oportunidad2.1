package rosales.emmanuel.oportunidad2

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    var subtotal = 0.0
    var iva = 0.0
    var total = 0.0

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnFacturar = findViewById<Button>(R.id.btnFacturar)
        val btnAgregar = findViewById<Button>(R.id.btnDarAlta)

        val etDescripcion = findViewById<EditText>(R.id.eTDescrip)
        val etCantidad = findViewById<EditText>(R.id.eTCantidad)
        val etPrecio = findViewById<EditText>(R.id.eTPrecio)

        btnAgregar.setOnClickListener {
            if (count < 3) {
                agregarFilaATabla(etDescripcion.text.toString(), etCantidad.text.toString().toInt(), etPrecio.text.toString().toDouble())
                count++  // Incrementar la cuenta
            } else {
                Toast.makeText(this, "Solo se pueden agregar 3 regalos", Toast.LENGTH_SHORT).show()
            }
        }
        btnFacturar.setOnClickListener {
            val bd = FirebaseFirestore.getInstance()
            val datos = hashMapOf(
                "descripcion" to etDescripcion.text.toString(),
                "cantidad" to etCantidad.text.toString().toInt(),
                "importe" to etPrecio.text.toString().toDouble(),
                "subtotal" to subtotal,
                "iva" to iva,
                "total" to total
            )

            bd.collection("factura")
                .add(datos)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    fun agregarFilaATabla(descripcion: String, cantidad: Int, importe: Double) {
        val table = findViewById<TableLayout>(R.id.tableLayout)

        val row = TableRow(this)
        val lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
        row.layoutParams = lp

        val tvDescripcion = TextView(this)
        tvDescripcion.text = descripcion
        row.addView(tvDescripcion)

        val tvCantidad = TextView(this)
        tvCantidad.text = cantidad.toString()
        row.addView(tvCantidad)

        val tvImporte = TextView(this)
        tvImporte.text = importe.toString()
        row.addView(tvImporte)

        table.addView(row)

        subtotal += cantidad * importe
        iva = subtotal * 0.16
        total = subtotal + iva

        val etSubtotal = findViewById<TextView>(R.id.tVSubtotal)
        val etIVA = findViewById<TextView>(R.id.tVIVA)
        val etTotal = findViewById<TextView>(R.id.tVTotal)

        etSubtotal.text = subtotal.toString()
        etIVA.text = iva.toString()
        etTotal.text = total.toString()
    }
}