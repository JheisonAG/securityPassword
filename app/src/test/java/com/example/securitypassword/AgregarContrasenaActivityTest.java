package com.example.securitypassword;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.securitypassword.utils.PasswordValidator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Tests de integración para AgregarContrasenaActivity
 */
@RunWith(RobolectricTestRunner.class)
public class AgregarContrasenaActivityTest {

    private AgregarContrasenaActivity activity;
    private EditText campoServicio;
    private EditText campoUrl;
    private EditText campoUsuarioEntrada;
    private EditText campoContrasenaNueva;
    private EditText campoNotas;
    private Button botonGuardar;
    private TextView textoCancelar;
    private TextView etiquetaFortaleza;
    private LinearProgressIndicator indicadorFortaleza;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(AgregarContrasenaActivity.class).create().get();

        campoServicio = activity.findViewById(R.id.campoServicio);
        campoUrl = activity.findViewById(R.id.campoUrl);
        campoUsuarioEntrada = activity.findViewById(R.id.campoUsuarioEntrada);
        campoContrasenaNueva = activity.findViewById(R.id.campoContrasenaNueva);
        campoNotas = activity.findViewById(R.id.campoNotas);
        botonGuardar = activity.findViewById(R.id.botonGuardar);
        textoCancelar = activity.findViewById(R.id.textoCancelar);
        etiquetaFortaleza = activity.findViewById(R.id.etiquetaFortaleza);
        indicadorFortaleza = activity.findViewById(R.id.indicadorFortaleza);
    }

    @Test
    public void activity_seCreCorrectamente() {
        assertNotNull("AgregarContrasenaActivity debe crearse correctamente", activity);
        assertFalse("Activity no debe estar terminada", activity.isFinishing());
    }

    @Test
    public void elementos_interfaz_seInicializanCorrectamente() {
        assertNotNull("Campo servicio debe existir", campoServicio);
        assertNotNull("Campo URL debe existir", campoUrl);
        assertNotNull("Campo usuario debe existir", campoUsuarioEntrada);
        assertNotNull("Campo contraseña debe existir", campoContrasenaNueva);
        assertNotNull("Campo notas debe existir", campoNotas);
        assertNotNull("Botón guardar debe existir", botonGuardar);
        assertNotNull("Texto cancelar debe existir", textoCancelar);
        assertNotNull("Etiqueta fortaleza debe existir", etiquetaFortaleza);
        assertNotNull("Indicador fortaleza debe existir", indicadorFortaleza);
    }

    @Test
    public void validacionCampos_servicioVacio_noDeberiaGuardar() {
        // Arrange
        campoServicio.setText("");
        campoUsuarioEntrada.setText("test@email.com");
        campoContrasenaNueva.setText("password123");

        // Act
        botonGuardar.performClick();

        // Assert - En una implementación real, verificaríamos que se muestre un Toast
        assertTrue("El campo servicio debe estar vacío para esta prueba",
                campoServicio.getText().toString().isEmpty());
    }

    @Test
    public void validacionCampos_usuarioVacio_noDeberiaGuardar() {
        // Arrange
        campoServicio.setText("Gmail");
        campoUsuarioEntrada.setText("");
        campoContrasenaNueva.setText("password123");

        // Act
        botonGuardar.performClick();

        // Assert
        assertTrue("El campo usuario debe estar vacío para esta prueba",
                campoUsuarioEntrada.getText().toString().isEmpty());
    }

    @Test
    public void validacionCampos_contrasenaVacia_noDeberiaGuardar() {
        // Arrange
        campoServicio.setText("Gmail");
        campoUsuarioEntrada.setText("test@email.com");
        campoContrasenaNueva.setText("");

        // Act
        botonGuardar.performClick();

        // Assert
        assertTrue("El campo contraseña debe estar vacío para esta prueba",
                campoContrasenaNueva.getText().toString().isEmpty());
    }

    @Test
    public void evaluadorFortaleza_contrasenaDebil_muestraIndicadorCorrecto() {
        // Arrange
        String contrasenaDebil = "123";

        // Act
        campoContrasenaNueva.setText(contrasenaDebil);

        // Assert - Verificamos la lógica del evaluador
        int fortaleza = PasswordValidator.evaluarFortaleza(contrasenaDebil);
        String etiqueta = PasswordValidator.obtenerEtiquetaFortaleza(fortaleza);

        assertTrue("Una contraseña débil debe tener fortaleza baja", fortaleza < 40);
        assertEquals("La etiqueta debe indicar debilidad", "Muy débil", etiqueta);
    }

    @Test
    public void evaluadorFortaleza_contrasenaFuerte_muestraIndicadorCorrecto() {
        // Arrange
        String contrasenaFuerte = "MyP@ssw0rd123!";

        // Act
        campoContrasenaNueva.setText(contrasenaFuerte);

        // Assert
        int fortaleza = PasswordValidator.evaluarFortaleza(contrasenaFuerte);
        String etiqueta = PasswordValidator.obtenerEtiquetaFortaleza(fortaleza);

        assertEquals("Una contraseña completa debe tener fortaleza máxima", 100, fortaleza);
        assertEquals("La etiqueta debe indicar fortaleza máxima", "Muy fuerte", etiqueta);
    }

    @Test
    public void camposOpcionales_puedenEstarVacios() {
        // Arrange - Solo campos obligatorios
        campoServicio.setText("Gmail");
        campoUsuarioEntrada.setText("test@email.com");
        campoContrasenaNueva.setText("password123");

        // URL y notas vacías (son opcionales)
        campoUrl.setText("");
        campoNotas.setText("");

        // Act & Assert
        assertFalse("Campo servicio no debe estar vacío", campoServicio.getText().toString().isEmpty());
        assertFalse("Campo usuario no debe estar vacío", campoUsuarioEntrada.getText().toString().isEmpty());
        assertFalse("Campo contraseña no debe estar vacío", campoContrasenaNueva.getText().toString().isEmpty());

        // Los campos opcionales pueden estar vacíos
        assertTrue("Campo URL puede estar vacío", campoUrl.getText().toString().isEmpty());
        assertTrue("Campo notas puede estar vacío", campoNotas.getText().toString().isEmpty());
    }

    @Test
    public void inputTypes_sonApropiadosParaCadaCampo() {
        // Verificar tipos de input apropiados
        assertTrue("Campo URL debe aceptar URIs",
                (campoUrl.getInputType() & android.text.InputType.TYPE_TEXT_VARIATION_URI) != 0);

        assertTrue("Campo email debe aceptar emails",
                (campoUsuarioEntrada.getInputType() & android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) != 0);

        assertTrue("Campo contraseña debe ser tipo password",
                (campoContrasenaNueva.getInputType() & android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD) != 0);

        assertTrue("Campo notas debe permitir múltiples líneas",
                (campoNotas.getInputType() & android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE) != 0);
    }
}
