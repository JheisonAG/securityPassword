package com.example.securitypassword.utils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitarios para utilidades de validación de contraseñas
 */
public class PasswordValidatorTest {

    @Test
    public void evaluarFortaleza_contrasenaVacia_retornaCero() {
        int fortaleza = PasswordValidator.evaluarFortaleza("");
        assertEquals("Una contraseña vacía debe tener fortaleza 0", 0, fortaleza);
    }

    @Test
    public void evaluarFortaleza_contrasenaMuyCorta_retornaFortalezaBaja() {
        int fortaleza = PasswordValidator.evaluarFortaleza("123");
        assertTrue("Una contraseña muy corta debe tener fortaleza baja", fortaleza < 40);
    }

    @Test
    public void evaluarFortaleza_contrasenaLarga_sumaPuntos() {
        int fortaleza = PasswordValidator.evaluarFortaleza("12345678");
        assertTrue("Una contraseña de 8+ caracteres debe sumar puntos", fortaleza >= 20);
    }

    @Test
    public void evaluarFortaleza_contieneMayusculas_sumaPuntos() {
        int fortaleza = PasswordValidator.evaluarFortaleza("Password");
        assertTrue("Una contraseña con mayúsculas debe sumar puntos", fortaleza >= 20);
    }

    @Test
    public void evaluarFortaleza_contieneMinusculas_sumaPuntos() {
        int fortaleza = PasswordValidator.evaluarFortaleza("password");
        assertTrue("Una contraseña con minúsculas debe sumar puntos", fortaleza >= 20);
    }

    @Test
    public void evaluarFortaleza_contieneNumeros_sumaPuntos() {
        int fortaleza = PasswordValidator.evaluarFortaleza("password123");
        assertTrue("Una contraseña con números debe sumar puntos", fortaleza >= 40);
    }

    @Test
    public void evaluarFortaleza_contieneCaracteresEspeciales_sumaPuntos() {
        int fortaleza = PasswordValidator.evaluarFortaleza("password@!");
        assertTrue("Una contraseña con caracteres especiales debe sumar puntos", fortaleza >= 40);
    }

    @Test
    public void evaluarFortaleza_contrasenaCompleta_esMuyFuerte() {
        int fortaleza = PasswordValidator.evaluarFortaleza("MyP@ssw0rd123!");
        assertEquals("Una contraseña completa debe tener fortaleza 100", 100, fortaleza);
    }

    @Test
    public void obtenerEtiquetaFortaleza_valores_correctos() {
        assertEquals("Fortaleza 0-19 debe ser 'Muy débil'", "Muy débil",
                PasswordValidator.obtenerEtiquetaFortaleza(0));
        assertEquals("Fortaleza 20-39 debe ser 'Débil'", "Débil",
                PasswordValidator.obtenerEtiquetaFortaleza(30));
        assertEquals("Fortaleza 40-59 debe ser 'Media'", "Media",
                PasswordValidator.obtenerEtiquetaFortaleza(50));
        assertEquals("Fortaleza 60-79 debe ser 'Fuerte'", "Fuerte",
                PasswordValidator.obtenerEtiquetaFortaleza(70));
        assertEquals("Fortaleza 80+ debe ser 'Muy fuerte'", "Muy fuerte",
                PasswordValidator.obtenerEtiquetaFortaleza(90));
    }

    @Test
    public void esContrasenaValida_contrasenasInvalidas_retornaFalso() {
        assertFalse("Contraseña nula debe ser inválida", PasswordValidator.esContrasenaValida(null));
        assertFalse("Contraseña vacía debe ser inválida", PasswordValidator.esContrasenaValida(""));
        assertFalse("Contraseña muy corta debe ser inválida", PasswordValidator.esContrasenaValida("123"));
        assertFalse("Contraseña de solo espacios debe ser inválida", PasswordValidator.esContrasenaValida("     "));
    }

    @Test
    public void esContrasenaValida_contrasenasValidas_retornaVerdadero() {
        assertTrue("Contraseña de 6+ caracteres debe ser válida",
                PasswordValidator.esContrasenaValida("123456"));
        assertTrue("Contraseña compleja debe ser válida",
                PasswordValidator.esContrasenaValida("MyP@ssw0rd"));
    }

    @Test
    public void esEmailValido_emailsInvalidos_retornaFalso() {
        assertFalse("Email nulo debe ser inválido", PasswordValidator.esEmailValido(null));
        assertFalse("Email vacío debe ser inválido", PasswordValidator.esEmailValido(""));
        assertFalse("Email sin @ debe ser inválido", PasswordValidator.esEmailValido("email.com"));
        assertFalse("Email sin dominio debe ser inválido", PasswordValidator.esEmailValido("email@"));
        assertFalse("Email sin usuario debe ser inválido", PasswordValidator.esEmailValido("@domain.com"));
    }

    @Test
    public void esEmailValido_emailsValidos_retornaVerdadero() {
        assertTrue("Email simple debe ser válido", PasswordValidator.esEmailValido("test@email.com"));
        assertTrue("Email con subdominios debe ser válido", PasswordValidator.esEmailValido("user@mail.google.com"));
        assertTrue("Email con números debe ser válido", PasswordValidator.esEmailValido("user123@domain.org"));
    }
}
