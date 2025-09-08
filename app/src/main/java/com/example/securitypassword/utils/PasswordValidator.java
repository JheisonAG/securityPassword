package com.example.securitypassword.utils;

import java.util.regex.Pattern;

/**
 * Utilidades para validación de contraseñas y emails
 */
public class PasswordValidator {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    /**
     * Evalúa la fortaleza de una contraseña del 0 al 100
     */
    public static int evaluarFortaleza(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        int fortaleza = 0;

        // Longitud mínima de 8 caracteres
        if (password.length() >= 8) fortaleza += 20;

        // Contiene minúsculas
        if (password.matches(".*[a-z].*")) fortaleza += 20;

        // Contiene mayúsculas
        if (password.matches(".*[A-Z].*")) fortaleza += 20;

        // Contiene números
        if (password.matches(".*[0-9].*")) fortaleza += 20;

        // Contiene caracteres especiales
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) fortaleza += 20;

        return fortaleza;
    }

    /**
     * Obtiene la etiqueta de fortaleza basada en el puntaje
     */
    public static String obtenerEtiquetaFortaleza(int fortaleza) {
        if (fortaleza >= 80) return "Muy fuerte";
        else if (fortaleza >= 60) return "Fuerte";
        else if (fortaleza >= 40) return "Media";
        else if (fortaleza >= 20) return "Débil";
        else return "Muy débil";
    }

    /**
     * Valida si una contraseña es válida (mínimo 6 caracteres)
     */
    public static boolean esContrasenaValida(String password) {
        return password != null &&
               !password.trim().isEmpty() &&
               password.length() >= MIN_PASSWORD_LENGTH;
    }

    /**
     * Valida si un email tiene formato correcto
     */
    public static boolean esEmailValido(String email) {
        return email != null &&
               !email.trim().isEmpty() &&
               EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida si dos contraseñas coinciden
     */
    public static boolean contrasenasCoinciden(String password1, String password2) {
        return password1 != null &&
               password2 != null &&
               password1.equals(password2);
    }

    /**
     * Valida si un campo de texto no está vacío
     */
    public static boolean campoNoVacio(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }
}
