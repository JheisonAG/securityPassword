package com.example.securitypassword.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitarios para la clase Contrasena
 */
public class ContrasenaTest {

    private Contrasena contrasena;
    private final String SERVICIO_TEST = "Gmail";
    private final String URL_TEST = "https://gmail.com";
    private final String USUARIO_TEST = "test@gmail.com";
    private final String PASSWORD_TEST = "password123";
    private final String NOTAS_TEST = "Cuenta principal";
    private final String USER_ID_TEST = "user123";

    @Before
    public void setUp() {
        contrasena = new Contrasena(SERVICIO_TEST, URL_TEST, USUARIO_TEST,
                                  PASSWORD_TEST, NOTAS_TEST, USER_ID_TEST);
    }

    @Test
    public void constructor_vacio_creaObjetoValido() {
        Contrasena contrasenaVacia = new Contrasena();
        assertNotNull("El constructor vacío debe crear un objeto válido", contrasenaVacia);
    }

    @Test
    public void constructor_conParametros_asignaValoresCorrectamente() {
        assertEquals("El servicio debe asignarse correctamente", SERVICIO_TEST, contrasena.getServicio());
        assertEquals("La URL debe asignarse correctamente", URL_TEST, contrasena.getUrl());
        assertEquals("El usuario debe asignarse correctamente", USUARIO_TEST, contrasena.getUsuario());
        assertEquals("La contraseña debe asignarse correctamente", PASSWORD_TEST, contrasena.getContrasena());
        assertEquals("Las notas deben asignarse correctamente", NOTAS_TEST, contrasena.getNotas());
        assertEquals("El user ID debe asignarse correctamente", USER_ID_TEST, contrasena.getUserId());
    }

    @Test
    public void constructor_conParametros_asignaFechaCreacion() {
        long tiempoAntes = System.currentTimeMillis() - 1000; // 1 segundo antes
        long tiempoDespues = System.currentTimeMillis() + 1000; // 1 segundo después

        assertTrue("La fecha de creación debe estar entre el tiempo antes y después de la creación",
                contrasena.getFechaCreacion() >= tiempoAntes && contrasena.getFechaCreacion() <= tiempoDespues);
    }

    @Test
    public void setters_y_getters_funcionanCorrectamente() {
        String nuevoId = "id123";
        String nuevoServicio = "Facebook";
        String nuevaUrl = "https://facebook.com";
        String nuevoUsuario = "nuevo@email.com";
        String nuevaContrasena = "nuevaPassword456";
        String nuevasNotas = "Cuenta secundaria";
        long nuevaFecha = System.currentTimeMillis();
        String nuevoUserId = "newUser456";

        contrasena.setId(nuevoId);
        contrasena.setServicio(nuevoServicio);
        contrasena.setUrl(nuevaUrl);
        contrasena.setUsuario(nuevoUsuario);
        contrasena.setContrasena(nuevaContrasena);
        contrasena.setNotas(nuevasNotas);
        contrasena.setFechaCreacion(nuevaFecha);
        contrasena.setUserId(nuevoUserId);

        assertEquals("El ID debe actualizarse correctamente", nuevoId, contrasena.getId());
        assertEquals("El servicio debe actualizarse correctamente", nuevoServicio, contrasena.getServicio());
        assertEquals("La URL debe actualizarse correctamente", nuevaUrl, contrasena.getUrl());
        assertEquals("El usuario debe actualizarse correctamente", nuevoUsuario, contrasena.getUsuario());
        assertEquals("La contraseña debe actualizarse correctamente", nuevaContrasena, contrasena.getContrasena());
        assertEquals("Las notas deben actualizarse correctamente", nuevasNotas, contrasena.getNotas());
        assertEquals("La fecha debe actualizarse correctamente", nuevaFecha, contrasena.getFechaCreacion());
        assertEquals("El user ID debe actualizarse correctamente", nuevoUserId, contrasena.getUserId());
    }

    @Test
    public void valores_nulos_sonManejadasCorrectamente() {
        Contrasena contrasenaNula = new Contrasena(null, null, null, null, null, null);

        assertNull("El servicio nulo debe ser manejado", contrasenaNula.getServicio());
        assertNull("La URL nula debe ser manejada", contrasenaNula.getUrl());
        assertNull("El usuario nulo debe ser manejado", contrasenaNula.getUsuario());
        assertNull("La contraseña nula debe ser manejada", contrasenaNula.getContrasena());
        assertNull("Las notas nulas deben ser manejadas", contrasenaNula.getNotas());
        assertNull("El user ID nulo debe ser manejado", contrasenaNula.getUserId());
    }

    @Test
    public void valores_vacios_sonManejadasCorrectamente() {
        Contrasena contrasenaVacia = new Contrasena("", "", "", "", "", "");

        assertEquals("El servicio vacío debe ser manejado", "", contrasenaVacia.getServicio());
        assertEquals("La URL vacía debe ser manejada", "", contrasenaVacia.getUrl());
        assertEquals("El usuario vacío debe ser manejado", "", contrasenaVacia.getUsuario());
        assertEquals("La contraseña vacía debe ser manejada", "", contrasenaVacia.getContrasena());
        assertEquals("Las notas vacías deben ser manejadas", "", contrasenaVacia.getNotas());
        assertEquals("El user ID vacío debe ser manejado", "", contrasenaVacia.getUserId());
    }
}
