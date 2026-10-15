package com.mx.proveedores.service;

import com.mx.proveedores.dto.ProveedorRequestDTO;
import com.mx.proveedores.dto.ProveedorResponseDTO;
import com.mx.proveedores.entity.Proveedor;
import com.mx.proveedores.excepciones.ProveedorNotFoundException;
import com.mx.proveedores.repository.iProveedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProveedorServiceTest {

    @Mock
    iProveedorRepository repository;

    @InjectMocks
    ProveedorService service;

    @Mock
    ModelMapper mapper;

    @Test
    void listar_debeRetornarUnaLista() {

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

        List<Proveedor> proveedores = List.of(proveedor1, proveedor2);

        ProveedorResponseDTO dto1 = new ProveedorResponseDTO();
        dto1.setNombre("Sebastian Ramiro Diaz");
        dto1.setRfc("RADS990124882");
        dto1.setEmail("sebaslascurain240@gmail.com");
        dto1.setTelefono("2331288809");
        dto1.setDireccion("Privada 12 de Octubre");

        ProveedorResponseDTO dto2 = new ProveedorResponseDTO();
        dto2.setNombre("Erick Ramiro Diaz");
        dto2.setRfc("RADE010624996");
        dto2.setEmail("erikramiri169@gmail.com");
        dto2.setTelefono("5539250195");
        dto2.setDireccion("Privada 12 de Octubre");

        when(repository.findAll(Sort.by(Sort.Direction.ASC, "idProveedor")))
                .thenReturn(proveedores);

        when(mapper.map(proveedor1, ProveedorResponseDTO.class))
                .thenReturn(dto1);

        when(mapper.map(proveedor2, ProveedorResponseDTO.class))
                .thenReturn(dto2);

        List<ProveedorResponseDTO> resultado = service.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        assertEquals("Sebastian Ramiro Diaz", resultado.get(0).getNombre());
        assertEquals("Erick Ramiro Diaz", resultado.get(1).getNombre());

        verify(repository).findAll(Sort.by(Sort.Direction.ASC, "idProveedor"));
        verify(mapper, times(2))
                .map(any(Proveedor.class), eq(ProveedorResponseDTO.class));
    }

    @Test
    void listar_debeRetornarUnaListaNula() {

        when(repository.findAll(Sort.by(Sort.Direction.ASC, "idProveedor")))
                .thenReturn(Collections.emptyList());

        List<ProveedorResponseDTO> resultado = service.listar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findAll(Sort.by(Sort.Direction.ASC, "idProveedor"));
        verifyNoInteractions(mapper);
    }

    @Test
    void listar_debeLanzarErrorSiFallaAccesoAlRepositorio() {

        when(repository.findAll(Sort.by(Sort.Direction.ASC, "idProveedor")))
                .thenThrow(new DataAccessResourceFailureException(
                        "Error de acceso a la base de datos"
                ));

        DataAccessResourceFailureException resultado = assertThrows(
                DataAccessResourceFailureException.class,
                () -> service.listar()
        );

        assertEquals(
                "Error de acceso a la base de datos",
                resultado.getMessage()
        );

        verify(repository).findAll(Sort.by(Sort.Direction.ASC, "idProveedor"));
        verifyNoInteractions(mapper);
    }

    @Test
    void guardar_debeGuardarProveedorCorrectamente() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setNombre("Sebastian Ramiro Diaz");
        dto.setRfc("RADS990124882");
        dto.setEmail("sebas@example.com");
        dto.setTelefono("2331288809");
        dto.setDireccion("Privada 12 de Octubre");

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(dto.getNombre());
        proveedor.setRfc(dto.getRfc());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());

        ProveedorResponseDTO respuesta = new ProveedorResponseDTO();
        respuesta.setNombre(dto.getNombre());
        respuesta.setEmail(dto.getEmail());

        when(repository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        when(mapper.map(dto, Proveedor.class))
                .thenReturn(proveedor);

        when(repository.save(proveedor))
                .thenReturn(proveedor);

        when(mapper.map(proveedor, ProveedorResponseDTO.class))
                .thenReturn(respuesta);

        ProveedorResponseDTO resultado = service.guardar(dto);

        assertNotNull(resultado);
        assertEquals("Sebastian Ramiro Diaz", resultado.getNombre());
        assertEquals("sebas@example.com", resultado.getEmail());

        verify(repository).findByEmail(dto.getEmail());
        verify(repository).save(proveedor);
        verify(mapper).map(dto, Proveedor.class);
        verify(mapper).map(proveedor, ProveedorResponseDTO.class);
    }

    @Test
    void guardar_debeLanzarErrorSiFallaAccesoAlRepositorio() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setEmail("sebas@example.com");

        Proveedor proveedor = new Proveedor();

        when(repository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        when(mapper.map(dto, Proveedor.class))
                .thenReturn(proveedor);

        when(repository.save(proveedor))
                .thenThrow(new DataAccessResourceFailureException(
                        "Error de acceso a la base de datos"
                ));

        assertThrows(
                DataAccessResourceFailureException.class,
                () -> service.guardar(dto)
        );

        verify(repository).findByEmail(dto.getEmail());
        verify(repository).save(proveedor);
    }

    @Test
    void guardar_debeLanzarErrorSiElEmailYaExiste() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setEmail("sebas@example.com");

        Proveedor existente = new Proveedor();

        when(repository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(existente));

        RuntimeException resultado = assertThrows(
                RuntimeException.class,
                () -> service.guardar(dto)
        );

        assertEquals(
                "El email ya existe, intenta con otro.",
                resultado.getMessage()
        );

        verify(repository).findByEmail(dto.getEmail());
        verify(repository, never()).save(any(Proveedor.class));
        verifyNoInteractions(mapper);
    }

    @Test
    void buscar_debeRetornarProveedorEncontrado() {

        int idProveedor = 1;

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Sebastian Ramiro Diaz");
        proveedor.setEmail("sebas@example.com");

        ProveedorResponseDTO respuesta = new ProveedorResponseDTO();
        respuesta.setNombre("Sebastian Ramiro Diaz");
        respuesta.setEmail("sebas@example.com");

        when(repository.findById(idProveedor))
                .thenReturn(Optional.of(proveedor));

        when(mapper.map(proveedor, ProveedorResponseDTO.class))
                .thenReturn(respuesta);

        ProveedorResponseDTO resultado = service.buscar(idProveedor);

        assertNotNull(resultado);
        assertEquals("Sebastian Ramiro Diaz", resultado.getNombre());
        assertEquals("sebas@example.com", resultado.getEmail());

        verify(repository).findById(idProveedor);
        verify(mapper).map(proveedor, ProveedorResponseDTO.class);
    }

    @Test
    void buscar_debeLanzarErrorSiFallaAccesoAlRepositorio() {

        int idProveedor = 1;

        when(repository.findById(idProveedor))
                .thenThrow(new DataAccessResourceFailureException(
                        "Error de acceso a la base de datos"
                ));

        assertThrows(
                DataAccessResourceFailureException.class,
                () -> service.buscar(idProveedor)
        );

        verify(repository).findById(idProveedor);
        verifyNoInteractions(mapper);
    }

    @Test
    void buscar_debeLanzarErrorSiProveedorNoExiste() {

        int idProveedor = 99;

        when(repository.findById(idProveedor))
                .thenReturn(Optional.empty());

        ProveedorNotFoundException resultado = assertThrows(
                ProveedorNotFoundException.class,
                () -> service.buscar(idProveedor)
        );

        assertEquals(
                "Proveedor no encontrado con el id 99",
                resultado.getMessage()
        );

        verify(repository).findById(idProveedor);
        verifyNoInteractions(mapper);
    }

    @Test
    void editar_debeActualizarProveedorCorrectamente() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setIdProveedor(1);
        dto.setTelefono("2331288809");
        dto.setEmail("nuevo@example.com");
        dto.setDireccion("Nueva dirección");

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Sebastian Ramiro Diaz");
        proveedor.setTelefono("1111111111");
        proveedor.setEmail("anterior@example.com");
        proveedor.setDireccion("Dirección anterior");

        ProveedorResponseDTO respuesta = new ProveedorResponseDTO();
        respuesta.setNombre("Sebastian Ramiro Diaz");
        respuesta.setTelefono("2331288809");
        respuesta.setEmail("nuevo@example.com");
        respuesta.setDireccion("Nueva dirección");

        when(repository.findById(1))
                .thenReturn(Optional.of(proveedor));

        when(repository.save(proveedor))
                .thenReturn(proveedor);

        when(mapper.map(proveedor, ProveedorResponseDTO.class))
                .thenReturn(respuesta);

        ProveedorResponseDTO resultado = service.editar(dto);

        assertNotNull(resultado);
        assertEquals("2331288809", proveedor.getTelefono());
        assertEquals("nuevo@example.com", proveedor.getEmail());
        assertEquals("Nueva dirección", proveedor.getDireccion());

        assertEquals("nuevo@example.com", resultado.getEmail());
        assertEquals("Nueva dirección", resultado.getDireccion());

        verify(repository).findById(1);
        verify(repository).save(proveedor);
        verify(mapper).map(proveedor, ProveedorResponseDTO.class);
    }

    @Test
    void editar_debeLanzarErrorSiFallaAccesoAlRepositorio() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setIdProveedor(1);
        dto.setTelefono("2331288809");
        dto.setEmail("nuevo@example.com");
        dto.setDireccion("Nueva dirección");

        Proveedor proveedor = new Proveedor();

        when(repository.findById(1))
                .thenReturn(Optional.of(proveedor));

        when(repository.save(proveedor))
                .thenThrow(new DataAccessResourceFailureException(
                        "Error al actualizar proveedor"
                ));

        assertThrows(
                DataAccessResourceFailureException.class,
                () -> service.editar(dto)
        );

        verify(repository).findById(1);
        verify(repository).save(proveedor);
        verifyNoInteractions(mapper);
    }

    @Test
    void editar_debeLanzarErrorSiProveedorNoExiste() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setIdProveedor(99);

        when(repository.findById(99))
                .thenReturn(Optional.empty());

        ProveedorNotFoundException resultado = assertThrows(
                ProveedorNotFoundException.class,
                () -> service.editar(dto)
        );

        assertEquals(
                "Proveedor no encontrado con el id 99",
                resultado.getMessage()
        );

        verify(repository).findById(99);
        verify(repository, never()).save(any(Proveedor.class));
        verifyNoInteractions(mapper);
    }

    @Test
    void eliminar_debeEliminarProveedorCorrectamente() {

        int idProveedor = 1;
        Proveedor proveedor = new Proveedor();

        when(repository.findById(idProveedor))
                .thenReturn(Optional.of(proveedor));

        assertDoesNotThrow(() -> service.eliminar(idProveedor));

        verify(repository).findById(idProveedor);
        verify(repository).delete(proveedor);
    }

    @Test
    void eliminar_debeLanzarErrorSiFallaAccesoAlRepositorio() {

        int idProveedor = 1;
        Proveedor proveedor = new Proveedor();

        when(repository.findById(idProveedor))
                .thenReturn(Optional.of(proveedor));

        doThrow(new DataAccessResourceFailureException(
                "Error al eliminar proveedor"
        )).when(repository).delete(proveedor);

        assertThrows(
                DataAccessResourceFailureException.class,
                () -> service.eliminar(idProveedor)
        );

        verify(repository).findById(idProveedor);
        verify(repository).delete(proveedor);
    }

    @Test
    void eliminar_debeLanzarErrorSiProveedorNoExiste() {

        int idProveedor = 99;

        when(repository.findById(idProveedor))
                .thenReturn(Optional.empty());

        ProveedorNotFoundException resultado = assertThrows(
                ProveedorNotFoundException.class,
                () -> service.eliminar(idProveedor)
        );

        assertEquals(
                "Proveedor no encontrado con el id 99",
                resultado.getMessage()
        );

        verify(repository).findById(idProveedor);
        verify(repository, never()).delete(any(Proveedor.class));
    }
}
