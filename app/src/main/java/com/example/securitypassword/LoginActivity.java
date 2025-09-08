package com.example.securitypassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText campoUsuario, campoContrasena;
    private Button botonIniciarSesion;
    private TextView textoRegistrate, textoOlvideContrasena;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Enlazar vistas
        campoUsuario = findViewById(R.id.campoUsuario);
        campoContrasena = findViewById(R.id.campoContrasena);
        botonIniciarSesion = findViewById(R.id.botonIniciarSesion);
        textoRegistrate = findViewById(R.id.textoRegistrate);
        textoOlvideContrasena = findViewById(R.id.textoOlvideContrasena);

        // Iniciar sesión con Firebase
        botonIniciarSesion.setOnClickListener(v -> {
            String email = campoUsuario.getText().toString().trim();
            String password = campoContrasena.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Autenticar con Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login exitoso
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(this, "Bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // Error en login
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
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

    @Override
    protected void onStart() {
        super.onStart();
        // Verificar si el usuario ya está logueado
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
