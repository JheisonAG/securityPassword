package com.example.securitypassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.securitypassword.utils.PasswordValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    private EditText campoNombre, campoEmail, campoContrasena, campoConfirmarContrasena;
    private Button botonRegistrarse;
    private TextView textoIniciaSesion;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Enlazar vistas
        campoNombre = findViewById(R.id.campoNombre);
        campoEmail = findViewById(R.id.campoEmail);
        campoContrasena = findViewById(R.id.campoContrasena);
        campoConfirmarContrasena = findViewById(R.id.campoConfirmarContrasena);
        botonRegistrarse = findViewById(R.id.botonRegistrarse);
        textoIniciaSesion = findViewById(R.id.textoIniciaSesion);

        // Registrar usuario con Firebase usando validaciones mejoradas
        botonRegistrarse.setOnClickListener(v -> {
            String nombre = campoNombre.getText().toString().trim();
            String email = campoEmail.getText().toString().trim();
            String password = campoContrasena.getText().toString();
            String confirmPassword = campoConfirmarContrasena.getText().toString();

            // Validaciones usando PasswordValidator
            if (!PasswordValidator.campoNoVacio(nombre)) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!PasswordValidator.esEmailValido(email)) {
                Toast.makeText(this, "Por favor ingresa un email válido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!PasswordValidator.esContrasenaValida(password)) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!PasswordValidator.contrasenasCoinciden(password, confirmPassword)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear usuario con Firebase
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(this, "Registro exitoso. Bienvenido!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // Error en registro
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        });

        textoIniciaSesion.setOnClickListener(v -> volverALogin());
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
