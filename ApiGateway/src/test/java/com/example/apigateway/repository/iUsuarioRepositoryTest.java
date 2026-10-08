package com.example.apigateway.repository;

import com.example.apigateway.entity.Rol;
import com.example.apigateway.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class iUsuarioRepositoryTest {

    @Autowired
    private iUsuarioRepository repository;

    @Test
    void save_debeGuardarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("sebas");
        usuario.setPassword("HASH");
        usuario.setRol(Rol.USER);

        Usuario resultado = repository.save(usuario);

        assertNotNull(resultado.getId());
        assertEquals("sebas", resultado.getUsername());
        assertEquals("HASH", resultado.getPassword());
        assertEquals(Rol.USER, resultado.getRol());
    }

    @Test
    void findByUsername_debeEncontrarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("sebas");
        usuario.setPassword("HASH");
        usuario.setRol(Rol.USER);

        repository.save(usuario);

        Optional<Usuario> resultado =
                repository.findByUsername("sebas");

        assertTrue(resultado.isPresent());
        assertEquals("sebas", resultado.get().getUsername());
        assertEquals("HASH", resultado.get().getPassword());
    }

    @Test
    void findByUsername_debeRetornarVacioSiNoExiste() {

        Optional<Usuario> resultado =
                repository.findByUsername("inexistente");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findById_debeEncontrarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("sebas");
        usuario.setPassword("HASH");
        usuario.setRol(Rol.USER);

        Usuario guardado = repository.save(usuario);

        Optional<Usuario> resultado =
                repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals(
                guardado.getId(),
                resultado.get().getId()
        );
        assertEquals(
                "sebas",
                resultado.get().getUsername()
        );
    }

    @Test
    void findAll_debeRetornarTodosLosUsuarios() {
        Usuario usuario1 = new Usuario();
        usuario1.setUsername("test_sebas");
        usuario1.setPassword("HASH1");
        usuario1.setRol(Rol.USER);

        Usuario usuario2 = new Usuario();
        usuario2.setUsername("test_admin");
        usuario2.setPassword("HASH2");
        usuario2.setRol(Rol.ADMIN);

        repository.save(usuario1);
        repository.save(usuario2);

        List<Usuario> usuarios = repository.findAll();

        assertTrue(usuarios.stream()
                .anyMatch(u -> u.getUsername().equals("test_sebas")));

        assertTrue(usuarios.stream()
                .anyMatch(u -> u.getUsername().equals("test_admin")));
    }

    @Test
    void deleteById_debeEliminarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("sebas");
        usuario.setPassword("HASH");
        usuario.setRol(Rol.USER);

        Usuario guardado = repository.save(usuario);

        repository.deleteById(guardado.getId());

        Optional<Usuario> resultado =
                repository.findById(guardado.getId());

        assertTrue(resultado.isEmpty());
    }
}
