package com.example.securitypassword;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.securitypassword.firebase.FirebaseHelper;
import com.example.securitypassword.model.Contrasena;
import com.example.securitypassword.utils.CryptoUtils;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class DetalleContrasenaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contrasena);

        EditText campoServicio = findViewById(R.id.campoServicioDetalle);
        EditText campoUrl = findViewById(R.id.campoUrlDetalle);
        EditText campoUsuario = findViewById(R.id.campoUsuarioDetalle);
        EditText campoContrasena = findViewById(R.id.campoContrasenaDetalle);
        EditText campoNotas = findViewById(R.id.campoNotasDetalle);
        MaterialButton botonEditar = findViewById(R.id.botonEditar);
        MaterialButton botonEliminar = findViewById(R.id.botonEliminar);

        // Obtener datos del Intent
        String contrasenaId = getIntent().getStringExtra("contrasena_id");
        String servicio = getIntent().getStringExtra("servicio");
        String url = getIntent().getStringExtra("url");
        String usuario = getIntent().getStringExtra("usuario");
        String contrasena = getIntent().getStringExtra("contrasena");
        String notas = getIntent().getStringExtra("notas");

        // Obtener el UID del usuario autenticado
        String userUid = null;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        // Desencriptar la contraseña si es posible
        String contrasenaDesencriptada = contrasena;
        if (userUid != null && contrasena != null && !contrasena.isEmpty()) {
            contrasenaDesencriptada = CryptoUtils.safeDecrypt(contrasena, userUid);
        }

        // Mostrar datos en los campos
        campoServicio.setText(servicio);
        campoUrl.setText(url);
        campoUsuario.setText(usuario);
        campoContrasena.setText(contrasenaDesencriptada);
        campoNotas.setText(notas);

        // Estado de edición
        final boolean[] enEdicion = {false};

        botonEditar.setOnClickListener(v -> {
            if (!enEdicion[0]) {
                // Cambiar a modo edición
                campoServicio.setEnabled(true);
                campoUrl.setEnabled(true);
                campoUsuario.setEnabled(true);
                campoContrasena.setEnabled(true);
                campoNotas.setEnabled(true);
                botonEditar.setText("Guardar");
                enEdicion[0] = true;
            } else {
                // Guardar cambios
                String nuevoServicio = campoServicio.getText().toString().trim();
                String nuevaUrl = campoUrl.getText().toString().trim();
                String nuevoUsuario = campoUsuario.getText().toString().trim();
                String nuevaContrasena = campoContrasena.getText().toString();
                String nuevasNotas = campoNotas.getText().toString();

                if (nuevoServicio.isEmpty() || nuevoUsuario.isEmpty() || nuevaContrasena.isEmpty()) {
                    Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                Contrasena contrasenaObj = new Contrasena();
                contrasenaObj.setId(contrasenaId);
                contrasenaObj.setServicio(nuevoServicio);
                contrasenaObj.setUrl(nuevaUrl);
                contrasenaObj.setUsuario(nuevoUsuario);
                contrasenaObj.setContrasena(nuevaContrasena);
                contrasenaObj.setNotas(nuevasNotas);
                contrasenaObj.setFechaCreacion(System.currentTimeMillis());

                new FirebaseHelper().updatePassword(contrasenaObj, new FirebaseHelper.OnCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DetalleContrasenaActivity.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                        campoServicio.setEnabled(false);
                        campoUrl.setEnabled(false);
                        campoUsuario.setEnabled(false);
                        campoContrasena.setEnabled(false);
                        campoNotas.setEnabled(false);
                        botonEditar.setText("Editar");
                        enEdicion[0] = false;
                    }
                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(DetalleContrasenaActivity.this, "Error al actualizar: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        botonEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Eliminar contraseña")
                .setMessage("¿Estás seguro de que deseas eliminar esta contraseña?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    new FirebaseHelper().deletePassword(contrasenaId, new FirebaseHelper.OnCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(DetalleContrasenaActivity.this, "Contraseña eliminada", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(DetalleContrasenaActivity.this, "Error al eliminar: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
        });
    }
}
