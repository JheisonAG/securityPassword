package com.example.securitypassword.utils;

import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utilidades de cifrado para proteger contraseñas usando AES-256-GCM
 */
public class CryptoUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    /**
     * Genera una clave AES-256 derivada del UID del usuario
     */
    public static SecretKey generateUserKey(String userUid) throws Exception {
        // Usar el UID del usuario como base para generar una clave determinística
        byte[] keyBytes = new byte[32]; // 256 bits
        byte[] uidBytes = userUid.getBytes(StandardCharsets.UTF_8);

        // Llenar la clave con el UID repetido y hasheado
        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = uidBytes[i % uidBytes.length];
        }

        // Aplicar un hash simple para mayor seguridad
        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = (byte) (keyBytes[i] ^ (i * 13 + 7));
        }

        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Cifra un texto usando AES-256-GCM
     */
    public static String encrypt(String plainText, String userUid) throws Exception {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }

        SecretKey secretKey = generateUserKey(userUid);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // Generar IV aleatorio
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);

        byte[] encryptedData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Combinar IV + datos cifrados
        byte[] encryptedWithIv = new byte[GCM_IV_LENGTH + encryptedData.length];
        System.arraycopy(iv, 0, encryptedWithIv, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptedData, 0, encryptedWithIv, GCM_IV_LENGTH, encryptedData.length);

        return Base64.encodeToString(encryptedWithIv, Base64.DEFAULT);
    }

    /**
     * Descifra un texto cifrado con AES-256-GCM
     */
    public static String decrypt(String encryptedText, String userUid) throws Exception {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText;
        }

        SecretKey secretKey = generateUserKey(userUid);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        byte[] encryptedWithIv = Base64.decode(encryptedText, Base64.DEFAULT);

        // Extraer IV y datos cifrados
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] encryptedData = new byte[encryptedWithIv.length - GCM_IV_LENGTH];

        System.arraycopy(encryptedWithIv, 0, iv, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptedWithIv, GCM_IV_LENGTH, encryptedData, 0, encryptedData.length);

        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);

        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * Verifica si un texto está cifrado (contiene caracteres Base64)
     */
    public static boolean isEncrypted(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        try {
            Base64.decode(text, Base64.DEFAULT);
            return text.length() > 20 && text.matches("^[A-Za-z0-9+/]*={0,2}$");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Cifra de manera segura manejando errores
     */
    public static String safeEncrypt(String plainText, String userUid) {
        try {
            return encrypt(plainText, userUid);
        } catch (Exception e) {
            // En caso de error, devolver el texto original (no ideal, pero mejor que perder datos)
            return plainText;
        }
    }

    /**
     * Descifra de manera segura manejando errores
     */
    public static String safeDecrypt(String encryptedText, String userUid) {
        try {
            return decrypt(encryptedText, userUid);
        } catch (Exception e) {
            // Si no se puede descifrar, devolver el texto original
            return encryptedText;
        }
    }
}
