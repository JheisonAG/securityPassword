package com.example.securitypassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securitypassword.firebase.FirebaseHelper;
import com.example.securitypassword.model.Contrasena;
import com.example.securitypassword.utils.PasswordValidator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class AgregarContrasenaActivity extends AppCompatActivity {

    private EditText campoServicio, campoUrl, campoUsuarioEntrada, campoContrasenaNueva, campoNotas;
    private Button botonGuardar;
    private TextView textoCancelar, etiquetaFortaleza;
    private LinearProgressIndicator indicadorFortaleza;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contrasena);

        // Inicializar Firebase Helper
        firebaseHelper = new FirebaseHelper();

        // Enlazar vistas
        campoServicio = findViewById(R.id.campoServicio);
        campoUrl = findViewById(R.id.campoUrl);
        campoUsuarioEntrada = findViewById(R.id.campoUsuarioEntrada);
        campoContrasenaNueva = findViewById(R.id.campoContrasenaNueva);
        campoNotas = findViewById(R.id.campoNotas);
        botonGuardar = findViewById(R.id.botonGuardar);
        textoCancelar = findViewById(R.id.textoCancelar);
        etiquetaFortaleza = findViewById(R.id.etiquetaFortaleza);
        indicadorFortaleza = findViewById(R.id.indicadorFortaleza);

        // Verificar que el usuario esté autenticado
        if (!firebaseHelper.isUserLoggedIn()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Configurar indicador de fortaleza de contraseña
        campoContrasenaNueva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                evaluarFortalezaContrasena(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Guardar contraseña
        botonGuardar.setOnClickListener(v -> guardarContrasena());

        // "Cancelar y volver" -> regresar a Home
        textoCancelar.setOnClickListener(v -> volverAHome());
    }

    private void evaluarFortalezaContrasena(String password) {
        int fortaleza = PasswordValidator.evaluarFortaleza(password);
        String etiqueta = PasswordValidator.obtenerEtiquetaFortaleza(fortaleza);

        etiquetaFortaleza.setText("Fortaleza: " + etiqueta);
        indicadorFortaleza.setProgress(fortaleza);
    }

    private void guardarContrasena() {
        String servicio = campoServicio.getText().toString().trim();
        String url = campoUrl.getText().toString().trim();
        String usuario = campoUsuarioEntrada.getText().toString().trim();
        String contrasena = campoContrasenaNueva.getText().toString();
        String notas = campoNotas.getText().toString().trim();

        // Validaciones usando PasswordValidator
        if (!PasswordValidator.campoNoVacio(servicio)) {
            Toast.makeText(this, "El campo Servicio es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!PasswordValidator.campoNoVacio(usuario)) {
            Toast.makeText(this, "El campo Usuario es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!PasswordValidator.esContrasenaValida(contrasena)) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Contrasena
        Contrasena nuevaContrasena = new Contrasena(servicio, url, usuario, contrasena, notas,
            firebaseHelper.getCurrentUser().getUid());

        // Guardar en Firebase
        firebaseHelper.addPassword(nuevaContrasena, new FirebaseHelper.OnCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AgregarContrasenaActivity.this, "Contraseña guardada exitosamente", Toast.LENGTH_SHORT).show();
                volverAHome();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(AgregarContrasenaActivity.this, "Error al guardar: " + error, Toast.LENGTH_LONG).show();
            }
        });
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
