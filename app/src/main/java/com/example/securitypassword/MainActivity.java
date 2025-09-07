package com.example.securitypassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Views principales
        View root = findViewById(R.id.main);
        BottomNavigationView bottom = findViewById(R.id.bottomNav);

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

        ExtendedFloatingActionButton fab = findViewById(R.id.fabAgregar);
        fab.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AgregarContrasenaActivity.class))
        );

        bottom.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                RecyclerView rv = findViewById(R.id.listaContrasenas);
                if (rv != null) rv.smoothScrollToPosition(0);
                return true;
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, getString(R.string.msg_settings_wip), Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        bottom.setSelectedItemId(R.id.nav_home);
    }
}
