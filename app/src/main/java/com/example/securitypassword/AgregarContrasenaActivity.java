package com.example.securitypassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AgregarContrasenaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contrasena);

        // "Cancelar y volver" -> regresar a Home
        TextView cancelar = findViewById(R.id.textoCancelar);
        cancelar.setOnClickListener(v -> volverAHome());
    }

    private void volverAHome() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}
