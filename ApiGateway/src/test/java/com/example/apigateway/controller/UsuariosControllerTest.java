package com.example.apigateway.controller;

import com.example.apigateway.entity.Rol;
import com.example.apigateway.entity.Usuario;
import com.example.apigateway.exception.UsuarioExistenteException;
import com.example.apigateway.exception.UsuarioNoEncontradoException;
import com.example.apigateway.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class UsuariosControllerTest {

    @Mock
    UsuarioService service;

    @InjectMocks
    UsuariosController controller;

    @Test
    void mostrarUsuarios_debeRetornarListaConStatus200() {

        List<Usuario> lista = List.of(new Usuario());
        when(service.mostrar()).thenReturn(lista);

        ResponseEntity<?> respuesta = controller.mostrar();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(lista, respuesta.getBody());

        verify(service).mostrar();
    }

    @Test
    void mostrar_debeRetornarListaVaciaConStatus200() {

        List<Usuario> lista = List.of();

        when(service.mostrar()).thenReturn(lista);

        ResponseEntity<?> respuesta = controller.mostrar();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(lista, respuesta.getBody());

        assertNotNull(respuesta.getBody());

        assertTrue(
                ((List<?>) respuesta.getBody()).isEmpty(),
                "La lista está vacía"
        );

        verify(service).mostrar();
    }

    @Test
    void mostrar_debeRetornarBadRequestSiOcurreError() {

        RuntimeException exception = new RuntimeException(
                new RuntimeException("Error al consultar usuarios")
        );

        when(service.mostrar()).thenThrow(exception);

        ResponseEntity<?> respuesta = controller.mostrar();

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Error al consultar usuarios", respuesta.getBody());

        verify(service).mostrar();
    }

    @Test
    void buscarUsuario_debeRetornarUsuarioConStatus200() {

        Usuario dto=new Usuario();
        String usuario = "Erick";

        when(service.mostrarUsuario(usuario))
                .thenReturn(dto);

        ResponseEntity<?> respuesta = controller.buscarUsuario(usuario);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(dto, respuesta.getBody());

        verify(service).mostrarUsuario(usuario);
    }

    @Test
    void buscarUsuario_debeRetornarBadRequestSiOcurreError() {

        String usuario = "Sebastián";

        UsuarioNoEncontradoException exception =
                new UsuarioNoEncontradoException("Usuario no encontrado");

        exception.initCause(new RuntimeException("Usuario no encontrado"));

        when(service.mostrarUsuario(usuario))
                .thenThrow(exception);

        ResponseEntity<?> respuesta = controller.buscarUsuario(usuario);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Usuario no encontrado", respuesta.getBody());

        verify(service).mostrarUsuario(usuario);
    }

    @Test
    void buscarUsuario_debeRetornarUsuarioNoEncontrado() {

        List<Usuario> lista = List.of();

        when(service.mostrar()).thenReturn(lista);

        ResponseEntity<?> respuesta = controller.mostrar();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(lista, respuesta.getBody());

        assertNotNull(respuesta.getBody());

        assertTrue(
                ((List<?>) respuesta.getBody()).isEmpty(),
                "La lista está vacía"
        );

        verify(service).mostrar();
    }

    @Test
    void registrar_debeRetornarCreatedSiInsertaCorrectamente() {

        Usuario dto = new Usuario();
        dto.setUsername("Sebastián");
        dto.setNombreCompleto("Sebastián Díaz");
        dto.setRol(Rol.USER);

        when(service.registrar(dto))
                .thenReturn(dto);

        ResponseEntity<?> respuesta = controller.registrar(dto);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(
                "{\"Mensaje\":\"Registro exitoso\"}",
                respuesta.getBody()
        );

        verify(service).registrar(dto);
    }

    @Test
    void registrar_debeRetornarBadRequestSiOcurreError() {

        Usuario dto = new Usuario();

        doThrow(new RuntimeException("Error al guardar usuario"))
                .when(service)
                .registrar(dto);

        ResponseEntity<?> respuesta = controller.registrar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Error al guardar usuario", respuesta.getBody());

        verify(service).registrar(dto);
    }

    @Test
    void registrar_debeRetornarBadRequestSiUsuarioYaExiste() {

        Usuario dto = new Usuario();
        dto.setUsername("Sebastián");

        when(service.registrar(dto))
                .thenThrow(new UsuarioExistenteException(
                        "El usuario ya está registrado"
                ));

        ResponseEntity<?> respuesta = controller.registrar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(
                "El usuario ya está registrado",
                respuesta.getBody()
        );

        verify(service).registrar(dto);
    }

    @Test
    void actualizarPassword_debeRetornarCreatedSiActualizoPasswordCorrectamente() {

        String username = "Sebastián";
        String password = "249901";

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);

        when(service.modificarPassword(username, password))
                .thenReturn(usuario);

        ResponseEntity<?> respuesta =
                controller.actualizarPassword(username, password);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());

        assertEquals(
                "{\"Mensaje\":\"Se actualizó la contraseña del usuario " + username + "\"}",
                respuesta.getBody()
        );

        verify(service).modificarPassword(username, password);
    }

    @Test
    void actualizarPassword_debeRetornarBadRequestSiOcurreError() {

        String username="Sebastián", password="249901";

        doThrow(new RuntimeException("Error al actualizar usuario"))
                .when(service)
                .modificarPassword(username, password);

        ResponseEntity<?> respuesta = controller.actualizarPassword(username, password);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Error al actualizar usuario", respuesta.getBody());

        verify(service).modificarPassword(username, password);
    }

    @Test
    void actualizarPassword_debeRetornarBadRequestSiUsuarioNoExiste() {

        String username = "Sebastián";
        String password = "249901";

        when(service.modificarPassword(username, password))
                .thenThrow(new UsuarioNoEncontradoException(
                        "Usuario no encontrado"
                ));

        ResponseEntity<?> respuesta =
                controller.actualizarPassword(username, password);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(
                "Usuario no encontrado",
                respuesta.getBody()
        );

        verify(service).modificarPassword(username, password);
    }

    @Test
    void actualizar_debeRetornarOkSiActualizaCorrectamente() {

        Usuario usuario = new Usuario();
        usuario.setUsername("Erika");

        when(service.editar(usuario))
                .thenReturn(usuario);

        ResponseEntity<?> respuesta = controller.actualizar(usuario);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(
                "{\"Mensaje\":\"Se actualizó la contraseña del usuario Erika\"}",
                respuesta.getBody()
        );

        verify(service).editar(usuario);
    }

    @Test
    void actualizar_debeRetornarBadRequestSiUsuarioYaExiste() {

        Usuario usuario = new Usuario();
        usuario.setUsername("Erika");

        when(service.editar(usuario))
                .thenThrow(new UsuarioExistenteException(
                        "El usuario ya está registrado"
                ));

        ResponseEntity<?> respuesta = controller.actualizar(usuario);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(
                "El usuario ya está registrado",
                respuesta.getBody()
        );

        verify(service).editar(usuario);
    }

    @Test
    void actualizar_debeRetornarBadRequestSiUsuarioNoExiste() {

        Usuario usuario = new Usuario();
        usuario.setUsername("Sachi");

        when(service.editar(usuario))
                .thenThrow(new UsuarioNoEncontradoException(
                        "Usuario no encontrado"
                ));

        ResponseEntity<?> respuesta = controller.actualizar(usuario);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(
                "Usuario no encontrado",
                respuesta.getBody()
        );

        verify(service).editar(usuario);
    }

    @Test
    void login_debeRetornarOkSiCredencialesCorrectas() {

        Usuario usuario = new Usuario();
        usuario.setUsername("Mikari");
        usuario.setPassword("HASH123");
        usuario.setRol(Rol.ADMIN);

        when(service.login("Mikari", "123456"))
                .thenReturn(usuario);

        ResponseEntity<?> respuesta =
                controller.login("Mikari", "123456");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(usuario, respuesta.getBody());

        verify(service).login("Mikari", "123456");
    }

    @Test
    void login_debeRetornarUnauthorizedSiCredencialesIncorrectas() {

        when(service.login("Mikari", "incorrecto"))
                .thenReturn(null);

        ResponseEntity<?> respuesta =
                controller.login("Mikari", "incorrecto");

        assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
        assertEquals(
                "Usuario o contraseña incorrectos",
                respuesta.getBody()
        );

        verify(service).login("Mikari", "incorrecto");
    }

    @Test
    void eliminar_debeRetornarCreatedSiEliminaCorrectamente() {

        String username = "Erika";

        doNothing()
                .when(service)
                .borrarUsuario(username);

        ResponseEntity<?> respuesta =
                controller.eliminar(username);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(
                "{\"Mensaje\":\"Usuario eliminado de manera exitosa\"}",
                respuesta.getBody()
        );

        verify(service).borrarUsuario(username);
    }

    @Test
    void eliminar_debeRetornarBadRequestSiUsuarioNoExiste() {

        String username = "Sachi";

        doThrow(new UsuarioNoEncontradoException(
                "Usuario no encontrado"
        ))
                .when(service)
                .borrarUsuario(username);

        ResponseEntity<?> respuesta =
                controller.eliminar(username);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(
                "Usuario no encontrado",
                respuesta.getBody()
        );

        verify(service).borrarUsuario(username);
    }

    @Test
    void eliminar_debeRetornarBadRequestSiOcurreError() {

        String username = "Erika";

        doThrow(new RuntimeException(
                "Error al eliminar usuario"
        ))
                .when(service)
                .borrarUsuario(username);

        ResponseEntity<?> respuesta =
                controller.eliminar(username);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(
                "Error al eliminar usuario",
                respuesta.getBody()
        );

        verify(service).borrarUsuario(username);
    }
}
