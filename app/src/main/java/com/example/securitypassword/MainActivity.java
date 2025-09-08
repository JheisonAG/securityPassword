package com.example.securitypassword;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContrasenaAdapter adapter;
    private FirebaseHelper firebaseHelper;
    private FirebaseAuth auth;

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

        ExtendedFloatingActionButton fab = findViewById(R.id.fabAgregar);
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

        // Cargar contraseñas
        cargarContrasenas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar contraseñas al volver a la actividad
        cargarContrasenas();
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
        firebaseHelper.getAllPasswords().addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(this, "Error al cargar contraseñas: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            List<Contrasena> contrasenas = new ArrayList<>();
            if (value != null) {
                for (DocumentSnapshot doc : value.getDocuments()) {
                    Contrasena contrasena = doc.toObject(Contrasena.class);
                    if (contrasena != null) {
                        contrasena.setId(doc.getId());
                        // IMPORTANTE: Las contraseñas se descifran automáticamente en getContrasena()
                        contrasena.prepareAfterFirebase();
                        contrasenas.add(contrasena);
                    }
                }
            }
            adapter.setContrasenas(contrasenas);
        });
    }

    private void cerrarSesion() {
        auth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
