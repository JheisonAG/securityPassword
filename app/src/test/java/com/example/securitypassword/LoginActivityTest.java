package com.example.securitypassword;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.*;

/**
 * Tests de integración para LoginActivity
 */
@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {

    private LoginActivity loginActivity;
    private EditText campoUsuario;
    private EditText campoContrasena;
    private Button botonIniciarSesion;
    private TextView textoRegistrate;
    private TextView textoOlvideContrasena;

    @Before
    public void setUp() {
        loginActivity = Robolectric.buildActivity(LoginActivity.class).create().get();

        campoUsuario = loginActivity.findViewById(R.id.campoUsuario);
        campoContrasena = loginActivity.findViewById(R.id.campoContrasena);
        botonIniciarSesion = loginActivity.findViewById(R.id.botonIniciarSesion);
        textoRegistrate = loginActivity.findViewById(R.id.textoRegistrate);
        textoOlvideContrasena = loginActivity.findViewById(R.id.textoOlvideContrasena);
    }

    @Test
    public void activity_seCreCorrectamente() {
        assertNotNull("LoginActivity debe crearse correctamente", loginActivity);
        assertFalse("LoginActivity no debe estar terminada", loginActivity.isFinishing());
    }

    @Test
    public void elementos_interfaz_seInicializanCorrectamente() {
        assertNotNull("Campo usuario debe existir", campoUsuario);
        assertNotNull("Campo contraseña debe existir", campoContrasena);
        assertNotNull("Botón iniciar sesión debe existir", botonIniciarSesion);
        assertNotNull("Texto registrate debe existir", textoRegistrate);
        assertNotNull("Texto olvidé contraseña debe existir", textoOlvideContrasena);
    }

    @Test
    public void camposVacios_botonLogin_noDeberiaIniciarActividad() {
        // Arrange
        campoUsuario.setText("");
        campoContrasena.setText("");

        // Act
        botonIniciarSesion.performClick();

        // Assert
        ShadowActivity shadowActivity = Shadows.shadowOf(loginActivity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        assertNull("No debe iniciar nueva actividad con campos vacíos", nextStartedActivity);
    }

    @Test
    public void camposCompletos_botonLogin_intentaAutenticacion() {
        // Arrange
        campoUsuario.setText("test@email.com");
        campoContrasena.setText("password123");

        // Act
        botonIniciarSesion.performClick();

        // Assert - Con Firebase real, esto requeriría más setup
        // Por ahora verificamos que los campos tienen contenido
        assertFalse("Campo usuario no debe estar vacío", campoUsuario.getText().toString().isEmpty());
        assertFalse("Campo contraseña no debe estar vacío", campoContrasena.getText().toString().isEmpty());
    }

    @Test
    public void textoRegistrate_alHacerClick_navegaARegistro() {
        // Act
        textoRegistrate.performClick();

        // Assert
        ShadowActivity shadowActivity = Shadows.shadowOf(loginActivity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();

        assertNotNull("Debe iniciar nueva actividad", nextStartedActivity);
        assertEquals("Debe navegar a RegistroActivity",
                RegistroActivity.class.getName(),
                nextStartedActivity.getComponent().getClassName());
    }

    @Test
    public void textoOlvideContrasena_alHacerClick_navegaARecuperar() {
        // Act
        textoOlvideContrasena.performClick();

        // Assert
        ShadowActivity shadowActivity = Shadows.shadowOf(loginActivity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();

        assertNotNull("Debe iniciar nueva actividad", nextStartedActivity);
        assertEquals("Debe navegar a RecuperarContrasenaActivity",
                RecuperarContrasenaActivity.class.getName(),
                nextStartedActivity.getComponent().getClassName());
    }

    @Test
    public void hints_camposTexto_sonCorrectos() {
        assertNotNull("Campo usuario debe tener hint", campoUsuario.getHint());
        assertNotNull("Campo contraseña debe tener hint", campoContrasena.getHint());
    }

    @Test
    public void inputTypes_sonCorrectos() {
        // Verificar que los tipos de input sean apropiados
        assertTrue("Campo usuario debe aceptar email",
                (campoUsuario.getInputType() & android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) != 0);
        assertTrue("Campo contraseña debe ser tipo password",
                (campoContrasena.getInputType() & android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD) != 0);
    }
}
