package com.example.apigateway.service;

import com.example.apigateway.entity.Rol;
import com.example.apigateway.entity.Usuario;
import com.example.apigateway.exception.UsuarioExistenteException;
import com.example.apigateway.exception.UsuarioNoEncontradoException;
import com.example.apigateway.repository.iUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    iUsuarioRepository repository;

    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    UsuarioService service;

    @Test
    void mostrar_debeRetornarLista(){

        Usuario usuario1 = new Usuario();
        usuario1.setNombreCompleto("Mikari Tachibana");
        usuario1.setUsername("Mikari");
        usuario1.setPassword("HASH1");
        usuario1.setRol(Rol.ADMIN);

        Usuario usuario2 = new Usuario();
        usuario2.setNombreCompleto("Erika Amano");
        usuario2.setUsername("Erika");
        usuario2.setPassword("HASH2");
        usuario2.setRol(Rol.USER);

        Usuario usuario3 = new Usuario();
        usuario2.setNombreCompleto("Awayuki Kokorone");
        usuario2.setUsername("Shuwa");
        usuario2.setPassword("HASH3");
        usuario2.setRol(Rol.USER);

        List<Usuario> usuarios = List.of(usuario1, usuario2, usuario3);

        when(repository.findAll(Sort.by(Sort.Direction.ASC, "id")))
                .thenReturn(usuarios);

        List<Usuario> resultado = service.mostrar();

        assertNotNull(resultado);

        assertEquals(3, resultado.size());

    }

    @Test
    void mostrar_debeRetornarListaVaciaSiNoHayUsuarios() {

        when(repository.findAll(Sort.by(Sort.Direction.ASC, "id")))
                .thenReturn(List.of());

        List<Usuario> resultado = service.mostrar();

        assertNotNull(resultado, "La lista de usuarios no debe ser null");

        assertTrue(
                resultado.isEmpty(),
                "No hay usuarios registrados"
        );

        verify(repository)
                .findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Test
    void mostrarUsuarios_debeRetornarUsuarioEncontrado(){

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Erika Amano");
        usuario.setUsername("Erika");
        usuario.setPassword("HASH2");
        usuario.setRol(Rol.USER);

        when(repository.findByUsername("Erika"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = service.mostrarUsuario("Erika");

        assertEquals(usuario, resultado);

        verify(repository).findByUsername("Erika");
    }

    @Test
    void mostrarUsuarios_debeLanzarExcepcionSiUsuarioNoExiste() {

        when(repository.findByUsername("Sachi"))
                .thenReturn(Optional.empty());

        UsuarioNoEncontradoException excepcion =
                assertThrows(
                        UsuarioNoEncontradoException.class,
                        () -> service.mostrarUsuario("Sachi")
                );

        assertEquals(
                "Usuario no encontrado",
                excepcion.getMessage()
        );

        verify(repository)
                .findByUsername("Sachi");
    }

    @Test
    void modificarPassword_debeActualizarPassword() {

        Usuario usuario = new Usuario();
        usuario.setUsername("Erika");
        usuario.setPassword("HASH_VIEJO");

        when(repository.findByUsername("Erika"))
                .thenReturn(Optional.of(usuario));

        doReturn("HASH_NUEVO")
                .when(encoder)
                .encode("NuevaPassword123");

        when(repository.save(usuario))
                .thenReturn(usuario);

        Usuario resultado =
                service.modificarPassword("Erika", "NuevaPassword123");

        assertNotNull(resultado);
        assertEquals("HASH_NUEVO", resultado.getPassword());

        verify(repository).findByUsername("Erika");
        verify(encoder).encode("NuevaPassword123");
        verify(repository).save(usuario);
    }

    @Test
    void modificarPassword_debeRetornarUsuarioNoEncontrado() {

        when(repository.findByUsername("Sachi"))
                .thenReturn(Optional.empty());

        UsuarioNoEncontradoException excepcion =
                assertThrows(
                        UsuarioNoEncontradoException.class,
                        () -> service.modificarPassword(
                                "Sachi",
                                "NuevaPassword123"
                        )
                );

        assertEquals(
                "Usuario no encontrado",
                excepcion.getMessage()
        );

        verify(repository).findByUsername("Sachi");

        verify(encoder, never())
                .encode(anyString());

        verify(repository, never())
                .save(any(Usuario.class));
    }

    @Test
    void borrarUsuario_debeRetornarUsuarioEncontrado(){

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Erika Amano");
        usuario.setUsername("Erika");
        usuario.setPassword("HASH2");
        usuario.setRol(Rol.USER);

        when(repository.findByUsername("Erika"))
                .thenReturn(Optional.of(usuario));

        service.borrarUsuario("Erika");

        verify(repository).findByUsername("Erika");
        verify(repository).delete(usuario);
    }

    @Test
    void borrarUsuario_debeRetornarUsuarioNoEncontrado() {

        when(repository.findByUsername("Sachi"))
                .thenReturn(Optional.empty());

        UsuarioNoEncontradoException excepcion =
                assertThrows(
                        UsuarioNoEncontradoException.class,
                        () -> service.borrarUsuario("Sachi")
                );

        assertEquals(
                "Usuario no encontrado",
                excepcion.getMessage()
        );

        verify(repository).findByUsername("Sachi");
        verify(repository, never())
                .delete(any(Usuario.class));
    }

    @Test
    void editar_debeRetornarActualizacionExitoso(){

        Usuario usuario = new Usuario();
        usuario.setId(27);
        usuario.setNombreCompleto("Erika Amano");
        usuario.setUsername("Erika");
        usuario.setRol(Rol.USER);

        when(repository.findById(27))
                .thenReturn(Optional.of(usuario));

        when(repository.save(usuario))
                .thenReturn(usuario);

        Usuario resultado =
                service.editar(usuario);

        assertNotNull(resultado);

        verify(repository).findById(27);
        verify(repository).save(usuario);
    }

    @Test
    void editar_debeRetornarUsuarioExistente() {

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombreCompleto("Mikari Tachibana");
        usuario.setUsername("Mikari");
        usuario.setRol(Rol.USER);

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(2);
        usuarioExistente.setUsername("Mikari");

        when(repository.findById(1))
                .thenReturn(Optional.of(usuario));

        when(repository.findByUsername("Mikari"))
                .thenReturn(Optional.of(usuarioExistente));

        UsuarioExistenteException excepcion =
                assertThrows(
                        UsuarioExistenteException.class,
                        () -> service.editar(usuario)
                );

        assertEquals(
                "El usuario ya está registrado",
                excepcion.getMessage()
        );

        verify(repository).findById(1);
        verify(repository).findByUsername("Mikari");
        verify(repository, never()).save(any(Usuario.class));
    }

    @Test
    void editar_debeRetornarErrorAlGuardar() {

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombreCompleto("Erika Amano");
        usuario.setUsername("Erika");
        usuario.setRol(Rol.USER);

        when(repository.findById(1))
                .thenReturn(Optional.of(usuario));

        when(repository.findByUsername("Erika"))
                .thenReturn(Optional.of(usuario));

        when(repository.save(usuario))
                .thenThrow(new RuntimeException("Error al editar usuario"));

        RuntimeException excepcion =
                assertThrows(
                        RuntimeException.class,
                        () -> service.editar(usuario)
                );

        assertEquals(
                "Error al editar usuario",
                excepcion.getMessage()
        );

        verify(repository).findById(1);
        verify(repository).findByUsername("Erika");
        verify(repository).save(usuario);
    }

    @Test
    void registrar_debeRetornarRegistroExitoso(){

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Naofumi Iwatani");
        usuario.setUsername("Naofumi");
        usuario.setPassword("Password123");

        doReturn("HASH_GENERADO")
                .when(encoder)
                .encode("Password123");

        when(repository.save(usuario))
                .thenReturn(usuario);

        Usuario resultado = service.registrar(usuario);

        assertNotNull(resultado);
        assertEquals("Naofumi", resultado.getUsername());
        assertEquals("HASH_GENERADO", resultado.getPassword());
        assertEquals(Rol.USER, resultado.getRol());

        verify(encoder).encode("Password123");
        verify(repository).save(usuario);
    }

    @Test
    void registrar_debeRetornarUsuarioYaExiste(){

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Naofumi Iwatani");
        usuario.setUsername("Naofumi");
        usuario.setPassword("Password123");
        usuario.setRol(Rol.USER);

        when(repository.findByUsername("Naofumi"))
                .thenReturn(Optional.of(usuario));

        UsuarioExistenteException excepcion =
                assertThrows(
                        UsuarioExistenteException.class,
                        () -> service.registrar(usuario)
                );

        assertEquals(
                "El usuario ya está registrado",
                excepcion.getMessage()
        );

        verify(repository).findByUsername("Naofumi");

        verify(encoder, never())
                .encode(anyString());

        verify(repository, never())
                .save(any(Usuario.class));
    }

    @Test
    void registrar_debeRetornarErrorAlGuardar(){

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Naofumi Iwatani");
        usuario.setUsername("Naofumi");
        usuario.setPassword("Password123");

        when(repository.findByUsername("Naofumi"))
                .thenReturn(Optional.empty());

        doReturn("HASH_GENERADO")
                .when(encoder)
                .encode("Password123");

        when(repository.save(usuario))
                .thenThrow(new RuntimeException("Error al guardar usuario"));

        RuntimeException excepcion =
                assertThrows(
                        RuntimeException.class,
                        () -> service.registrar(usuario)
                );

        assertEquals(
                "Error al guardar usuario",
                excepcion.getMessage()
        );

        verify(repository).findByUsername("Naofumi");
        verify(encoder).encode("Password123");
        verify(repository).save(usuario);
    }

    @Test
    void login_debeRetornarUsuarioSiPasswordEsCorrecto() {

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Mikari Tachibana");
        usuario.setUsername("Mikari");
        usuario.setPassword("HASH123");
        usuario.setRol(Rol.ADMIN);

        when(repository.findByUsername("Mikari"))
                .thenReturn(Optional.of(usuario));

        when(encoder.matches("249901", "HASH123"))
                .thenReturn(true);

        Usuario resultado = service.login("Mikari", "249901");

        assertEquals(usuario, resultado);

        verify(repository).findByUsername("Mikari");
        verify(encoder).matches("249901", "HASH123");

    }

    @Test
    void login_debeRetornarNullSiPasswordEsIncorrecto() {

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Mikari Tachibana");
        usuario.setUsername("Mikari");
        usuario.setPassword("HASH123");
        usuario.setRol(Rol.ADMIN);

        when(repository.findByUsername("Mikari"))
                .thenReturn(Optional.of(usuario));

        when(encoder.matches("incorrecto", "HASH123"))
                .thenReturn(false);

        Usuario resultado = service.login("Mikari", "incorrecto");

        assertNull(resultado);

        verify(repository).findByUsername("Mikari");
        verify(encoder).matches("incorrecto", "HASH123");

    }

    @Test
    void login_debeRetornarUsuarioNoEncontrado() {

        when(repository.findByUsername("Sachi"))
                .thenReturn(Optional.empty());

        UsuarioNoEncontradoException excepcion =
                assertThrows(
                        UsuarioNoEncontradoException.class,
                        () -> service.login("Sachi", "123456")
                );

        assertEquals(
                "Usuario no encontrado",
                excepcion.getMessage()
        );

        verify(repository).findByUsername("Sachi");

        verify(encoder, never())
                .matches(anyString(), anyString());
    }

    @Test
    void loadUserByUsername_debeRetornarUserDetails() {

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Mikari Tachibana");
        usuario.setUsername("Mikari");
        usuario.setPassword("HASH123");
        usuario.setRol(Rol.ADMIN);

        when(repository.findByUsername("Mikari"))
                .thenReturn(Optional.of(usuario));

        UserDetails resultado =
                service.loadUserByUsername("Mikari");

        assertNotNull(resultado);
        assertEquals("Mikari", resultado.getUsername());
        assertEquals("HASH123", resultado.getPassword());
        assertTrue(resultado.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority())));

        verify(repository).findByUsername("Mikari");
    }

    @Test
    void loadUserByUsername_debeLanzarExcepcionSiUsuarioNoExiste() {

        when(repository.findByUsername("Sachi"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException excepcion =
                assertThrows(
                        UsernameNotFoundException.class,
                        () -> service.loadUserByUsername("Sachi")
                );

        assertEquals(
                "Usuario no encontrado",
                excepcion.getMessage()
        );

        verify(repository).findByUsername("Sachi");
    }

}
