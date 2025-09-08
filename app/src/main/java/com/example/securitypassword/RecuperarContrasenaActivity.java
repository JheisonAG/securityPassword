package com.example.securitypassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    private EditText campoEmail;
    private Button botonRecuperar;
    private TextView textoVolverLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Enlazar vistas
        campoEmail = findViewById(R.id.campoEmail);
        botonRecuperar = findViewById(R.id.botonRecuperar);
        textoVolverLogin = findViewById(R.id.textoVolverLogin);

        // Enviar email de recuperación
        botonRecuperar.setOnClickListener(v -> {
            String email = campoEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enviar email de recuperación con Firebase
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Email de recuperación enviado. Revisa tu bandeja de entrada.", Toast.LENGTH_LONG).show();
                            volverALogin();
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        textoVolverLogin.setOnClickListener(v -> volverALogin());
    }

    private void volverALogin() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}
