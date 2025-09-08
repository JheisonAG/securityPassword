package com.example.securitypassword;

import com.example.securitypassword.adapter.ContrasenaAdapterTest;
import com.example.securitypassword.firebase.FirebaseHelperTest;
import com.example.securitypassword.model.ContrasenaTest;
import com.example.securitypassword.utils.PasswordValidatorTest;
import com.example.securitypassword.utils.CryptoUtilsTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite de tests que ejecuta todos los tests unitarios y de integración
 * de la aplicación SecurityPassword
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    // Tests de modelos
    ContrasenaTest.class,

    // Tests de utilidades
    PasswordValidatorTest.class,
    CryptoUtilsTest.class,

    // Tests de Firebase
    FirebaseHelperTest.class,

    // Tests de actividades
    LoginActivityTest.class,
    AgregarContrasenaActivityTest.class,

    // Tests de adaptadores
    ContrasenaAdapterTest.class,

    // Test original
    ExampleUnitTest.class
})
public class SecurityPasswordTestSuite {
    // Esta clase permanece vacía. Se usa únicamente como titular para las anotaciones anteriores.
}
