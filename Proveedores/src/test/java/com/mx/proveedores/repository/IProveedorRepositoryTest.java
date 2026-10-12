package com.mx.proveedores.repository;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import com.mx.proveedores.entity.Proveedor;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IProveedorRepositoryTest {

    @Autowired
    iProveedorRepository repository;

    @Test
    void save_debeGuardarUnProveedor(){
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Sebastian Ramiro Diaz");
        proveedor.setRfc("RADS990124882");
        proveedor.setEmail("sebaslascurain240@gmail.com");
        proveedor.setTelefono("2331288809");
        proveedor.setDireccion("Privada 12 de Octubre");

        Proveedor resultado = repository.save(proveedor);

        assertTrue(resultado.getIdProveedor() > 0);
        assertEquals("Sebastian Ramiro Diaz", resultado.getNombre());
        assertEquals("RADS990124882", resultado.getRfc());
        assertEquals("sebaslascurain240@gmail.com", resultado.getEmail());
        assertEquals("2331288809", resultado.getTelefono());
        assertEquals("Privada 12 de Octubre", resultado.getDireccion());
    }

    @Test
    void findByUsername_debeRetornarNuloSiNoExiste() {

        Optional<Proveedor> resultado =
                repository.findByEmail("sebaslascurain240@gmail.com");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findByUsername_debeEncontrarCorreo() {

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Sebastian Ramiro Diaz");
        proveedor.setRfc("RADS990124882");
        proveedor.setEmail("sebaslascurain240@gmail.com");
        proveedor.setTelefono("2331288809");
        proveedor.setDireccion("Privada 12 de Octubre");

        Proveedor encontrado = repository.save(proveedor);

        Optional<Proveedor> resultado =
                repository.findByEmail("sebaslascurain240@gmail.com");

        assertTrue(resultado.isPresent());
        assertEquals(
                encontrado.getIdProveedor(),
                resultado.get().getIdProveedor()
        );

        assertEquals(
                "Sebastian Ramiro Diaz",
                resultado.get().getNombre()
        );

        assertEquals(
                "RADS990124882",
                resultado.get().getRfc()
        );

        assertEquals(
                "sebaslascurain240@gmail.com",
                resultado.get().getEmail()
        );

        assertEquals(
                "2331288809",
                resultado.get().getTelefono()
        );

        assertEquals(
                "Privada 12 de Octubre",
                resultado.get().getDireccion()
        );
    }

    @Test
    void findById_debeRetornarNuloSiNoExiste() {

        Optional<Proveedor> resultado =
                repository.findById(2);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findById_debeEncontrarProveedor() {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Sebastian Ramiro Diaz");
        proveedor.setRfc("RADS990124882");
        proveedor.setEmail("sebaslascurain240@gmail.com");
        proveedor.setTelefono("2331288809");
        proveedor.setDireccion("Privada 12 de Octubre");

        Proveedor guardado = repository.save(proveedor);

        Optional<Proveedor> resultado =
                repository.findById(guardado.getIdProveedor());

        assertTrue(resultado.isPresent());
        assertEquals(
                guardado.getIdProveedor(),
                resultado.get().getIdProveedor()
        );

        assertEquals(
                "Sebastian Ramiro Diaz",
                resultado.get().getNombre()
        );

        assertEquals(
                "RADS990124882",
                resultado.get().getRfc()
        );

        assertEquals(
                "sebaslascurain240@gmail.com",
                resultado.get().getEmail()
        );

        assertEquals(
                "2331288809",
                resultado.get().getTelefono()
        );

        assertEquals(
                "Privada 12 de Octubre",
                resultado.get().getDireccion()
        );
    }

    @Test
    void findAll_debeRetornarTodosLosProveedores(){
        Proveedor proveedor1 = new Proveedor();
        proveedor1.setNombre("Sebastian Ramiro Diaz");
        proveedor1.setRfc("RADS990124882");
        proveedor1.setEmail("sebaslascurain240@gmail.com");
        proveedor1.setTelefono("2331288809");
        proveedor1.setDireccion("Privada 12 de Octubre");

        Proveedor proveedor2 = new Proveedor();
        proveedor2.setNombre("Erick Ramiro Diaz");
        proveedor2.setRfc("RADE010624996");
        proveedor2.setEmail("erikramiri169@gmail.com");
        proveedor2.setTelefono("5539250195");
        proveedor2.setDireccion("Privada 12 de Octubre");

        repository.save(proveedor1);
        repository.save(proveedor2);

        List<Proveedor> proveedores = repository.findAll();

        assertTrue(proveedores.stream()
                .anyMatch(u -> u.getRfc().equals("RADS990124882")));

        assertTrue(proveedores.stream()
                .anyMatch(u -> u.getRfc().equals("RADE010624996")));
    }

    @Test
    void findAll_debeRetornarUnaListaNula(){
        repository.deleteAll();

        List<Proveedor> proveedores = repository.findAll();

        assertNotNull(proveedores);
        assertTrue(proveedores.isEmpty());
    }

    @Test
    void deleteById_debeEliminarProveedor() {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Sebastian Ramiro Diaz");
        proveedor.setRfc("RADS990124882");
        proveedor.setEmail("sebaslascurain240@gmail.com");
        proveedor.setTelefono("2331288809");
        proveedor.setDireccion("Privada 12 de Octubre");

        Proveedor guardado = repository.save(proveedor);

        repository.deleteById(guardado.getIdProveedor());

        Optional<Proveedor> resultado =
                repository.findById(guardado.getIdProveedor());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deleteById_debeConfirmarQueElProveedorYaFueEliminado() {

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Sebastian Ramiro Diaz");
        proveedor.setRfc("RADS990124882");
        proveedor.setEmail("sebaslascurain240@gmail.com");
        proveedor.setTelefono("2331288809");
        proveedor.setDireccion("Privada 12 de Octubre");

        Proveedor guardado = repository.save(proveedor);
        int idProveedor = guardado.getIdProveedor();

        repository.deleteById(idProveedor);

        assertTrue(repository.findById(idProveedor).isEmpty());

        assertDoesNotThrow(() -> repository.deleteById(idProveedor));

        assertTrue(repository.findById(idProveedor).isEmpty());
    }
}
