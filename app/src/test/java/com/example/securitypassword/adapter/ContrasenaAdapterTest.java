package com.example.securitypassword.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import com.example.securitypassword.DetalleContrasenaActivity;
import com.example.securitypassword.R;
import com.example.securitypassword.model.Contrasena;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests unitarios para ContrasenaAdapter
 */
@RunWith(RobolectricTestRunner.class)
public class ContrasenaAdapterTest {

    private ContrasenaAdapter adapter;
    private Context context;
    private List<Contrasena> listaContrasenas;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        adapter = new ContrasenaAdapter(context);

        // Crear datos de prueba
        listaContrasenas = Arrays.asList(
            new Contrasena("Gmail", "https://gmail.com", "test1@gmail.com", "pass123", "Cuenta principal", "user1"),
            new Contrasena("Facebook", "https://facebook.com", "test2@facebook.com", "pass456", "Cuenta personal", "user1"),
            new Contrasena("GitHub", "", "developer@github.com", "securePass789", "", "user1")
        );

        // Asignar IDs a las contraseñas
        for (int i = 0; i < listaContrasenas.size(); i++) {
            listaContrasenas.get(i).setId("id" + (i + 1));
        }
    }

    @Test
    public void adapter_seCreCorrectamente() {
        assertNotNull("Adapter debe crearse correctamente", adapter);
        assertEquals("Lista inicial debe estar vacía", 0, adapter.getItemCount());
    }

    @Test
    public void setContrasenas_actualizaListaCorrectamente() {
        // Act
        adapter.setContrasenas(listaContrasenas);

        // Assert
        assertEquals("El adapter debe tener el número correcto de elementos",
                listaContrasenas.size(), adapter.getItemCount());
    }

    @Test
    public void setContrasenas_conListaVacia_funcionaCorrectamente() {
        // Arrange
        List<Contrasena> listaVacia = new ArrayList<>();

        // Act
        adapter.setContrasenas(listaVacia);

        // Assert
        assertEquals("Lista vacía debe resultar en 0 elementos", 0, adapter.getItemCount());
    }

    @Test
    public void setContrasenas_conListaNula_noRompeAdapter() {
        // Act & Assert - No debe lanzar excepción
        try {
            adapter.setContrasenas(null);
            // Si llega aquí, el adapter maneja nulos correctamente
            assertTrue("El adapter debe manejar listas nulas sin errores", true);
        } catch (Exception e) {
            fail("El adapter no debe fallar con lista nula: " + e.getMessage());
        }
    }

    @Test
    public void contrasenas_conDatosCompletos_seMuestranCorrectamente() {
        // Arrange
        adapter.setContrasenas(listaContrasenas);

        // Act & Assert - Verificar que los datos están disponibles
        assertEquals("Debe haber 3 contraseñas", 3, adapter.getItemCount());

        // Verificar que las contraseñas tienen los datos esperados
        Contrasena primera = listaContrasenas.get(0);
        assertEquals("Primera contraseña debe ser Gmail", "Gmail", primera.getServicio());
        assertEquals("Primera contraseña debe tener URL", "https://gmail.com", primera.getUrl());
        assertEquals("Primera contraseña debe tener usuario", "test1@gmail.com", primera.getUsuario());
    }

    @Test
    public void contrasenas_sinUrl_seManejanCorrectamente() {
        // Arrange - Contraseña sin URL
        Contrasena sinUrl = listaContrasenas.get(2); // GitHub tiene URL vacía

        // Assert
        assertTrue("Contraseña sin URL debe tener campo vacío",
                sinUrl.getUrl() == null || sinUrl.getUrl().isEmpty());
        assertEquals("Debe ser la contraseña de GitHub", "GitHub", sinUrl.getServicio());
    }

    @Test
    public void fechasCreacion_sonValidas() {
        // Act
        adapter.setContrasenas(listaContrasenas);

        // Assert
        for (Contrasena contrasena : listaContrasenas) {
            assertTrue("Fecha de creación debe ser válida (mayor que 0)",
                    contrasena.getFechaCreacion() > 0);
        }
    }

    @Test
    public void todosLosCampos_tienenContenidoValido() {
        // Act
        adapter.setContrasenas(listaContrasenas);

        // Assert
        for (Contrasena contrasena : listaContrasenas) {
            assertNotNull("Servicio no debe ser nulo", contrasena.getServicio());
            assertNotNull("Usuario no debe ser nulo", contrasena.getUsuario());
            assertNotNull("Contraseña no debe ser nula", contrasena.getContrasena());
            assertNotNull("User ID no debe ser nulo", contrasena.getUserId());

            assertFalse("Servicio no debe estar vacío", contrasena.getServicio().isEmpty());
            assertFalse("Usuario no debe estar vacío", contrasena.getUsuario().isEmpty());
            assertFalse("Contraseña no debe estar vacía", contrasena.getContrasena().isEmpty());
            assertFalse("User ID no debe estar vacío", contrasena.getUserId().isEmpty());
        }
    }
}
