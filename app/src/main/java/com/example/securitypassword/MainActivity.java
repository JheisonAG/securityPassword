package com.example.securitypassword;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securitypassword.adapter.ContrasenaAdapter;
import com.example.securitypassword.firebase.FirebaseHelper;
import com.example.securitypassword.model.Contrasena;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContrasenaAdapter adapter;
    private FirebaseHelper firebaseHelper;
    private FirebaseAuth auth;
    private ListenerRegistration contrasenasListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase
        firebaseHelper = new FirebaseHelper();
        auth = FirebaseAuth.getInstance();

        // Verificar autenticación
        if (!firebaseHelper.isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Views principales
        View root = findViewById(R.id.main);
        BottomNavigationView bottom = findViewById(R.id.bottomNav);
        recyclerView = findViewById(R.id.listaContrasenas);

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, 0);
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(bottom, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bars.bottom);
            return insets;
        });

        // Configurar RecyclerView
        adapter = new ContrasenaAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Cambiar a FloatingActionButton para evitar ClassCastException
        FloatingActionButton fab = findViewById(R.id.fabAgregar);
        fab.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AgregarContrasenaActivity.class))
        );

        bottom.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                if (recyclerView != null) recyclerView.smoothScrollToPosition(0);
                return true;
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, getString(R.string.msg_settings_wip), Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        bottom.setSelectedItemId(R.id.nav_home);

        // Configurar Toolbar como ActionBar para mostrar el menú correctamente
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        // Mostrar ventana emergente al hacer click en el icono de lock
        toolbar.setNavigationOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Acerca de la app")
                .setMessage("Esta app ha sido desarrollada por Jheison Yaima y Jairo Pantoja, como un proyecto de la Universidad Santo Tomás Sede Iquique, bajo la guía del profesor Patricio Carrasco.")
                .setPositiveButton("Aceptar", null)
                .create();
            // Mejorar el estilo visual: centrar texto, aumentar tamaño fuente, icono personalizado
            dialog.setOnShowListener(d -> {
                // Centrar el título y el mensaje
                int titleId = this.getResources().getIdentifier("alertTitle", "id", "android");
                if (titleId > 0) {
                    android.widget.TextView title = dialog.findViewById(titleId);
                    if (title != null) {
                        title.setTextAlignment(android.view.View.TEXT_ALIGNMENT_CENTER);
                        title.setTextSize(20);
                        title.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
                android.widget.TextView message = dialog.findViewById(android.R.id.message);
                if (message != null) {
                    message.setTextAlignment(android.view.View.TEXT_ALIGNMENT_CENTER);
                    message.setTextSize(16);
                }
                // Cambiar color del botón
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            });
            dialog.show();
        });

        // Cargar contraseñas
        cargarContrasenas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Solo recargar contraseñas si el usuario sigue autenticado
        if (firebaseHelper.isUserLoggedIn()) {
            cargarContrasenas();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            cerrarSesion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargarContrasenas() {
        // Cancelar listener anterior si existe
        if (contrasenasListener != null) {
            contrasenasListener.remove();
            contrasenasListener = null;
        }
        // Solo escuchar si el usuario está autenticado
        if (!firebaseHelper.isUserLoggedIn()) {
            adapter.setContrasenas(new ArrayList<>());
            return;
        }
        contrasenasListener = firebaseHelper.getAllPasswords().addSnapshotListener((value, error) -> {
            if (error != null) {
                // Solo mostrar error si el usuario sigue autenticado
                if (firebaseHelper.isUserLoggedIn()) {
                    Toast.makeText(this, "Error al cargar contraseñas: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            List<Contrasena> contrasenas = new ArrayList<>();
            if (value != null) {
                for (DocumentSnapshot doc : value.getDocuments()) {
                    Contrasena contrasena = doc.toObject(Contrasena.class);
                    if (contrasena != null) {
                        contrasena.setId(doc.getId());
                        contrasena.prepareAfterFirebase();
                        contrasenas.add(contrasena);
                    }
                }
            }
            adapter.setContrasenas(contrasenas);
        });
    }

    private void cerrarSesion() {
        // Cancelar listener antes de cerrar sesión
        if (contrasenasListener != null) {
            contrasenasListener.remove();
            contrasenasListener = null;
        }
        auth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
