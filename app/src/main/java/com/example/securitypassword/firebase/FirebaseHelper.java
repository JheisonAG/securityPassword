package com.example.securitypassword.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.example.securitypassword.model.Contrasena;

public class FirebaseHelper {
    private static final String COLLECTION_PASSWORDS = "passwords";

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public FirebaseHelper() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    // Obtener usuario actual
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    // Verificar si el usuario está logueado
    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    // Obtener referencia a la colección de contraseñas del usuario actual
    public CollectionReference getPasswordsCollection() {
        String userId = getCurrentUser().getUid();
        return db.collection("users").document(userId).collection(COLLECTION_PASSWORDS);
    }

    // Obtener todas las contraseñas del usuario ordenadas por fecha
    public Query getAllPasswords() {
        return getPasswordsCollection().orderBy("fechaCreacion", Query.Direction.DESCENDING);
    }

    // Agregar nueva contraseña - AHORA CON CIFRADO AUTOMÁTICO
    public void addPassword(Contrasena contrasena, OnCompleteListener listener) {
        if (!isUserLoggedIn()) {
            listener.onFailure("Usuario no autenticado");
            return;
        }

        contrasena.setUserId(getCurrentUser().getUid());

        // CIFRAR LA CONTRASEÑA ANTES DE GUARDAR
        contrasena.prepareForFirebase();

        getPasswordsCollection()
            .add(contrasena)
            .addOnSuccessListener(documentReference -> {
                contrasena.setId(documentReference.getId());
                listener.onSuccess();
            })
            .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    // Actualizar contraseña existente - CON CIFRADO
    public void updatePassword(Contrasena contrasena, OnCompleteListener listener) {
        if (!isUserLoggedIn() || contrasena.getId() == null) {
            listener.onFailure("Usuario no autenticado o ID inválido");
            return;
        }

        // CIFRAR LA CONTRASEÑA ANTES DE ACTUALIZAR
        contrasena.prepareForFirebase();

        getPasswordsCollection()
            .document(contrasena.getId())
            .set(contrasena)
            .addOnSuccessListener(aVoid -> listener.onSuccess())
            .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    // Eliminar contraseña
    public void deletePassword(String passwordId, OnCompleteListener listener) {
        if (!isUserLoggedIn()) {
            listener.onFailure("Usuario no autenticado");
            return;
        }

        getPasswordsCollection()
            .document(passwordId)
            .delete()
            .addOnSuccessListener(aVoid -> listener.onSuccess())
            .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    // Interface para callbacks
    public interface OnCompleteListener {
        void onSuccess();
        void onFailure(String error);
    }
}
