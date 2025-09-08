package com.example.securitypassword.utils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitarios para CryptoUtils - Verificación de cifrado seguro
 */
public class CryptoUtilsTest {

    private final String TEST_USER_ID = "testUser123";
    private final String TEST_PASSWORD = "MySecretPassword123!";
    private final String EMPTY_TEXT = "";
    private final String NULL_TEXT = null;

    @Test
    public void encrypt_contrasenaValida_retornaTextoCifrado() throws Exception {
        // Act
        String encrypted = CryptoUtils.encrypt(TEST_PASSWORD, TEST_USER_ID);

        // Assert
        assertNotNull("El texto cifrado no debe ser nulo", encrypted);
        assertNotEquals("El texto cifrado debe ser diferente al original", TEST_PASSWORD, encrypted);
        assertTrue("El texto cifrado debe ser más largo", encrypted.length() > TEST_PASSWORD.length());
    }

    @Test
    public void decrypt_textoCifrado_retornaTextoOriginal() throws Exception {
        // Arrange
        String encrypted = CryptoUtils.encrypt(TEST_PASSWORD, TEST_USER_ID);

        // Act
        String decrypted = CryptoUtils.decrypt(encrypted, TEST_USER_ID);

        // Assert
        assertEquals("El texto descifrado debe ser igual al original", TEST_PASSWORD, decrypted);
    }

    @Test
    public void encryptDecrypt_cicloCompleto_mantieneDatos() throws Exception {
        // Arrange
        String[] testPasswords = {
            "simplepass",
            "Más Compleja 123!",
            "特殊字符测试",
            "🔐🛡️ emoji test",
            "Very long password with many characters to test encryption and decryption"
        };

        // Act & Assert
        for (String password : testPasswords) {
            String encrypted = CryptoUtils.encrypt(password, TEST_USER_ID);
            String decrypted = CryptoUtils.decrypt(encrypted, TEST_USER_ID);

            assertEquals("Ciclo completo debe mantener la contraseña: " + password,
                        password, decrypted);
        }
    }

    @Test
    public void encrypt_textosVacios_sonManejadasCorrectamente() throws Exception {
        // Test texto vacío
        String encryptedEmpty = CryptoUtils.encrypt(EMPTY_TEXT, TEST_USER_ID);
        assertEquals("Texto vacío debe permanecer vacío", EMPTY_TEXT, encryptedEmpty);

        // Test texto nulo
        String encryptedNull = CryptoUtils.encrypt(NULL_TEXT, TEST_USER_ID);
        assertEquals("Texto nulo debe permanecer nulo", NULL_TEXT, encryptedNull);
    }

    @Test
    public void isEncrypted_textoPlano_retornaFalso() {
        assertFalse("Texto plano no debe detectarse como cifrado",
                   CryptoUtils.isEncrypted(TEST_PASSWORD));
        assertFalse("Texto vacío no debe detectarse como cifrado",
                   CryptoUtils.isEncrypted(""));
        assertFalse("Texto nulo no debe detectarse como cifrado",
                   CryptoUtils.isEncrypted(null));
    }

    @Test
    public void isEncrypted_textoCifrado_retornaVerdadero() throws Exception {
        // Arrange
        String encrypted = CryptoUtils.encrypt(TEST_PASSWORD, TEST_USER_ID);

        // Act & Assert
        assertTrue("Texto cifrado debe detectarse correctamente",
                  CryptoUtils.isEncrypted(encrypted));
    }

    @Test
    public void safeEncrypt_conError_retornaTextoOriginal() {
        // Arrange - user ID inválido que puede causar error
        String invalidUserId = "";

        // Act
        String result = CryptoUtils.safeEncrypt(TEST_PASSWORD, invalidUserId);

        // Assert - Debe devolver el texto original si falla
        assertNotNull("Safe encrypt no debe devolver null", result);
    }

    @Test
    public void safeDecrypt_conError_retornaTextoOriginal() {
        // Arrange - texto que no está cifrado
        String plainText = "not encrypted text";

        // Act
        String result = CryptoUtils.safeDecrypt(plainText, TEST_USER_ID);

        // Assert
        assertEquals("Safe decrypt debe devolver texto original si no puede descifrar",
                    plainText, result);
    }

    @Test
    public void encrypt_diferentesUsuarios_generanCifradosDiferentes() throws Exception {
        // Arrange
        String userId1 = "user1";
        String userId2 = "user2";

        // Act
        String encrypted1 = CryptoUtils.encrypt(TEST_PASSWORD, userId1);
        String encrypted2 = CryptoUtils.encrypt(TEST_PASSWORD, userId2);

        // Assert
        assertNotEquals("Diferentes usuarios deben generar cifrados diferentes",
                       encrypted1, encrypted2);
    }

    @Test
    public void encrypt_mismaContrasena_generaCifradosDiferentes() throws Exception {
        // Arrange & Act - Cifrar la misma contraseña múltiples veces
        String encrypted1 = CryptoUtils.encrypt(TEST_PASSWORD, TEST_USER_ID);
        String encrypted2 = CryptoUtils.encrypt(TEST_PASSWORD, TEST_USER_ID);

        // Assert - Deben ser diferentes debido al IV aleatorio
        assertNotEquals("Múltiples cifrados de la misma contraseña deben ser diferentes",
                       encrypted1, encrypted2);

        // Pero ambos deben descifrar al mismo texto
        String decrypted1 = CryptoUtils.decrypt(encrypted1, TEST_USER_ID);
        String decrypted2 = CryptoUtils.decrypt(encrypted2, TEST_USER_ID);

        assertEquals("Ambos cifrados deben descifrar al texto original", TEST_PASSWORD, decrypted1);
        assertEquals("Ambos cifrados deben descifrar al texto original", TEST_PASSWORD, decrypted2);
    }

    @Test
    public void generateUserKey_mismoUsuario_generaMismaClave() throws Exception {
        // Act
        String key1 = CryptoUtils.generateUserKey(TEST_USER_ID).toString();
        String key2 = CryptoUtils.generateUserKey(TEST_USER_ID).toString();

        // Assert
        assertEquals("La misma clave debe generarse para el mismo usuario", key1, key2);
    }
}
