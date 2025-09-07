package com.example.securitypassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText campoUsuario, campoContrasena;
    private Button botonIniciarSesion;
    private TextView textoRegistrate, textoOlvideContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Enlazar vistas
        campoUsuario = findViewById(R.id.campoUsuario);
        campoContrasena = findViewById(R.id.campoContrasena);
        botonIniciarSesion = findViewById(R.id.botonIniciarSesion);
        textoRegistrate = findViewById(R.id.textoRegistrate);
        textoOlvideContrasena = findViewById(R.id.textoOlvideContrasena);

        // Iniciar sesión
        botonIniciarSesion.setOnClickListener(v -> {
            String usuario = campoUsuario.getText().toString().trim();
            String contrasena = campoContrasena.getText().toString();
            if (usuario.isEmpty() || contrasena.isEmpty()) {
                return;
            }
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });

        // Ir a Registro
        textoRegistrate.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class))
        );

        // Ir a Recuperar contraseña
        textoOlvideContrasena.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RecuperarContrasenaActivity.class))
        );
    }
}
