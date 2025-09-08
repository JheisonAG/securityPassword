package com.example.securitypassword.model;

import com.example.securitypassword.utils.CryptoUtils;

public class Contrasena {
    private String id;
    private String servicio;
    private String url;
    private String usuario;
    private String contrasena; // Este campo se guardará cifrado
    private String notas;
    private long fechaCreacion;
    private String userId;

    // Constructor vacío para Firebase
    public Contrasena() {}

    public Contrasena(String servicio, String url, String usuario, String contrasena, String notas, String userId) {
        this.servicio = servicio;
        this.url = url;
        this.usuario = usuario;
        this.contrasena = contrasena; // Se cifrará al guardar
        this.notas = notas;
        this.userId = userId;
        this.fechaCreacion = System.currentTimeMillis();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    // Getter para contraseña - descifra automáticamente
    public String getContrasena() {
        if (userId != null && contrasena != null && CryptoUtils.isEncrypted(contrasena)) {
            return CryptoUtils.safeDecrypt(contrasena, userId);
        }
        return contrasena;
    }

    // Setter para contraseña - NO cifra aquí, se hace en prepareForFirebase()
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Método para obtener contraseña cifrada (para Firebase)
    public String getContrasenaEncriptada() {
        return contrasena;
    }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public long getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(long fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    /**
     * Prepara el objeto para guardarlo en Firebase cifrando la contraseña
     */
    public void prepareForFirebase() {
        if (userId != null && contrasena != null && !CryptoUtils.isEncrypted(contrasena)) {
            this.contrasena = CryptoUtils.safeEncrypt(contrasena, userId);
        }
    }

    /**
     * Prepara el objeto después de recuperarlo de Firebase descifrando la contraseña
     */
    public void prepareAfterFirebase() {
        // El descifrado se hace automáticamente en getContrasena()
        // Este método existe para simetría y futuras expansiones
    }
}
