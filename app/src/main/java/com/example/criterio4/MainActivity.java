package com.example.criterio4;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView actvCamarero;
    private EditText etTotalCuenta;
    private CheckBox cbPropina;
    private SeekBar seekBarPropina;
    private TextView tvPorcentajePropina, tvResultadoFinal;
    private RadioGroup rgMetodoPago;
    private RadioButton rbEfectivo, rbTarjeta;
    private RatingBar ratingBarServicio;
    private Button btnCalcular;

    private int porcentajeActual = 10; // Valor inicial del SeekBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referenciar vistas
        actvCamarero = findViewById(R.id.actvCamarero);
        etTotalCuenta = findViewById(R.id.etTotalCuenta);
        cbPropina = findViewById(R.id.cbPropina);
        seekBarPropina = findViewById(R.id.seekBarPropina);
        tvPorcentajePropina = findViewById(R.id.tvPorcentajePropina);
        rgMetodoPago = findViewById(R.id.rgMetodoPago);
        rbEfectivo = findViewById(R.id.rbEfectivo);
        rbTarjeta = findViewById(R.id.rbTarjeta);
        ratingBarServicio = findViewById(R.id.ratingBarServicio);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultadoFinal = findViewById(R.id.tvResultadoFinal);

        // Mejora 1: Lista preconfigurada de camareros
        String[] camareros = {"Adrián", "Jennifer", "Karen", "Fanny", "Escarlin", "Carlos"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, camareros);
        actvCamarero.setAdapter(adapter);

        // Escuchar cambios en el SeekBar
        seekBarPropina.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                porcentajeActual = progress;
                // Extraemos la base del texto desde strings.xml para mantener el multi-idioma
                String textoBase = getString(R.string.text_propina).split(":")[0];
                tvPorcentajePropina.setText(textoBase + ": " + progress + "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Botón Calcular
        btnCalcular.setOnClickListener(v -> calcularCuenta());
    }

    private void calcularCuenta() {
        String textoIngresado = etTotalCuenta.getText().toString().trim();

        // Criterio 3: Controlar valor nulo/vacío
        if (textoIngresado.isEmpty()) {
            mostrarError(getString(R.string.error_vacio));
            return;
        }

        try {
            double totalCuenta = Double.parseDouble(textoIngresado);

            // Criterio 3: Controlar valor 0 o negativo
            if (totalCuenta <= 0) {
                mostrarError(getString(R.string.error_negativo));
                return;
            }

            // Si pasa validaciones, calculamos
            double propina = 0.0;
            if (cbPropina.isChecked()) {
                propina = totalCuenta * (porcentajeActual / 100.0);
            }

            double totalFinal = totalCuenta + propina;

            // Obtener método de pago
            String metodoPago = rbEfectivo.isChecked() ? rbEfectivo.getText().toString() : rbTarjeta.getText().toString();

            // Obtener camarero y estrellas
            String camarero = actvCamarero.getText().toString();
            if(camarero.isEmpty()) camarero = "No especificado";
            float estrellas = ratingBarServicio.getRating();

            // Restaurar color a negro (Mejora 2)
            tvResultadoFinal.setTextColor(Color.BLACK);

            // Crear resultado final
            String mensaje = String.format("Total: %.2f€\nPropina: %.2f€\nMétodo: %s\nCamarero: %s\nCalificación: %.1f ⭐",
                    totalFinal, propina, metodoPago, camarero, estrellas);

            tvResultadoFinal.setText(mensaje);

        } catch (NumberFormatException e) {
            // Criterio 3: Controlar letras o símbolos inválidos
            mostrarError(getString(R.string.error_formato));
        }
    }

    // Mejora 2: Mostrar errores de color rojo
    private void mostrarError(String mensajeError) {
        tvResultadoFinal.setTextColor(Color.RED);
        tvResultadoFinal.setText(mensajeError);
    }
}