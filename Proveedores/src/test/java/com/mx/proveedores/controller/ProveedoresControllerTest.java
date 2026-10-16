package com.mx.proveedores.controller;

import com.mx.proveedores.dto.ProveedorRequestDTO;
import com.mx.proveedores.dto.ProveedorResponseDTO;
import com.mx.proveedores.excepciones.ProveedorNotFoundException;
import com.mx.proveedores.service.ProveedorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProveedoresControllerTest {

    @InjectMocks
    ProveedoresController controller;

    @Mock
    ProveedorService service;

    @Test
    void listar_debeRetornarLista200(){
        List<ProveedorResponseDTO> lista = List.of(new ProveedorResponseDTO());
        when(service.listar()).thenReturn(lista);

        ResponseEntity<?> respuesta = controller.listar();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(lista, respuesta.getBody());

        verify(service).listar();
    }

    @Test
    void listar_debeRetornarListaNula200(){

        List<ProveedorResponseDTO> lista = List.of();
        when(service.listar()).thenReturn(lista);

        ResponseEntity<?> respuesta = controller.listar();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(lista, respuesta.getBody());

        assertNotNull(respuesta.getBody());

        assertTrue(
                ((List<?>) respuesta.getBody()).isEmpty(),
                "La lista está vacía"
        );

        verify(service).listar();
    }

    @Test
    void listar_debeRetornarErrorAlConsultar(){

        ProveedorNotFoundException exception = new ProveedorNotFoundException(
                "Error al consultar proveedores"
        );

        exception.initCause(
                new RuntimeException("Error al consultar proveedores")
        );

        when(service.listar()).thenThrow(exception);

        ResponseEntity<?> respuesta = controller.listar();

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Error al consultar proveedores", respuesta.getBody());

        verify(service).listar();
    }

    @Test
    void guardar_debeRetornarCreatedSiInsertaCorrectamente() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setNombre("Sebastian Ramiro Diaz");
        dto.setRfc("RADS990124882");
        dto.setEmail("sebas@example.com");
        dto.setTelefono("2331288809");
        dto.setDireccion("Privada 12 de Octubre");

        ProveedorResponseDTO proveedorRespuesta = new ProveedorResponseDTO();
        proveedorRespuesta.setNombre(dto.getNombre());
        proveedorRespuesta.setEmail(dto.getEmail());

        when(service.guardar(dto))
                .thenReturn(proveedorRespuesta);

        ResponseEntity<?> respuesta = controller.guardar(dto);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertSame(proveedorRespuesta, respuesta.getBody());

        verify(service).guardar(dto);
    }

    @Test
    void guardar_debeRetornarBadRequestSiOcurreErrorDeAcceso() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setNombre("Sebastian Ramiro Diaz");
        dto.setRfc("RADS990124882");
        dto.setEmail("sebas@example.com");
        dto.setTelefono("2331288809");
        dto.setDireccion("Privada 12 de Octubre");

        when(service.guardar(dto))
                .thenThrow(new org.springframework.dao.DataAccessResourceFailureException(
                        "Error de acceso a la base de datos"
                ));

        ResponseEntity<?> respuesta = controller.guardar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Error de acceso a la base de datos", respuesta.getBody());

        verify(service).guardar(dto);
    }

    @Test
    void guardar_debeRetornarBadRequestSiElEmailYaExiste() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setNombre("Sebastian Ramiro Diaz");
        dto.setRfc("RADS990124882");
        dto.setEmail("sebas@example.com");
        dto.setTelefono("2331288809");
        dto.setDireccion("Privada 12 de Octubre");

        when(service.guardar(dto))
                .thenThrow(new RuntimeException(
                        "El email ya existe, intenta con otro."
                ));

        ResponseEntity<?> respuesta = controller.guardar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(
                "El email ya existe, intenta con otro.",
                respuesta.getBody()
        );

        verify(service).guardar(dto);
    }

    @Test
    void buscar_debeRetornarOkSiEncuentraProveedor() {

        int idProveedor = 1;
        ProveedorResponseDTO proveedor = new ProveedorResponseDTO();
        proveedor.setNombre("Sebastian Ramiro Diaz");
        proveedor.setEmail("sebas@example.com");

        when(service.buscar(idProveedor)).thenReturn(proveedor);

        ResponseEntity<?> respuesta = controller.buscar(idProveedor);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertSame(proveedor, respuesta.getBody());

        verify(service).buscar(idProveedor);
    }

    @Test
    void buscar_debePropagarErrorDeAccesoABaseDeDatos() {

        int idProveedor = 1;

        when(service.buscar(idProveedor))
                .thenThrow(new DataAccessResourceFailureException(
                        "Error de acceso a la base de datos"
                ));

        assertThrows(
                DataAccessResourceFailureException.class,
                () -> controller.buscar(idProveedor)
        );

        verify(service).buscar(idProveedor);
    }

    @Test
    void buscar_debeRetornarBadRequestSiNoEncuentraProveedor() {

        int idProveedor = 1;

        ProveedorNotFoundException exception =
                new ProveedorNotFoundException("Proveedor no encontrado");

        exception.initCause(
                new RuntimeException("Proveedor no encontrado")
        );

        when(service.buscar(idProveedor)).thenThrow(exception);

        ResponseEntity<?> respuesta = controller.buscar(idProveedor);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Proveedor no encontrado", respuesta.getBody());

        verify(service).buscar(idProveedor);
    }

    @Test
    void editar_debeRetornarOkSiEditaCorrectamente() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setIdProveedor(1);
        dto.setNombre("Sebastian Ramiro Diaz");
        dto.setRfc("RADS990124882");
        dto.setEmail("sebas@example.com");
        dto.setTelefono("2331288809");
        dto.setDireccion("Privada 12 de Octubre");

        ProveedorResponseDTO proveedorRespuesta = new ProveedorResponseDTO();
        proveedorRespuesta.setNombre(dto.getNombre());
        proveedorRespuesta.setEmail(dto.getEmail());

        when(service.editar(dto)).thenReturn(proveedorRespuesta);

        ResponseEntity<?> respuesta = controller.editar(dto);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertSame(proveedorRespuesta, respuesta.getBody());

        verify(service).editar(dto);
    }

    @Test
    void editar_debeRetornarBadRequestSiOcurreErrorDeAcceso() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setIdProveedor(1);
        dto.setEmail("sebas@example.com");

        when(service.editar(dto))
                .thenThrow(new DataAccessResourceFailureException(
                        "Error de acceso a la base de datos"
                ));

        ResponseEntity<?> respuesta = controller.editar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Error de acceso a la base de datos", respuesta.getBody());

        verify(service).editar(dto);
    }

    @Test
    void editar_debeRetornarBadRequestSiNoEncuentraProveedor() {

        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setIdProveedor(1);
        dto.setEmail("sebas@example.com");

        when(service.editar(dto))
                .thenThrow(new ProveedorNotFoundException(
                        "Proveedor no encontrado con el id 1"
                ));

        ResponseEntity<?> respuesta = controller.editar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(
                "Proveedor no encontrado con el id 1",
                respuesta.getBody()
        );

        verify(service).editar(dto);
    }

    @Test
    void eliminar_debeRetornarNoContentSiEliminaCorrectamente() {

        int idProveedor = 1;

        doNothing().when(service).eliminar(idProveedor);

        ResponseEntity<?> respuesta = controller.eliminar(idProveedor);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());

        verify(service).eliminar(idProveedor);
    }

    @Test
    void eliminar_debePropagarErrorDeAccesoABaseDeDatos() {

        int idProveedor = 1;

        doThrow(new DataAccessResourceFailureException(
                "Error de acceso a la base de datos"
        )).when(service).eliminar(idProveedor);

        assertThrows(
                DataAccessResourceFailureException.class,
                () -> controller.eliminar(idProveedor)
        );

        verify(service).eliminar(idProveedor);
    }

    @Test
    void eliminar_debeRetornarBadRequestSiNoEncuentraProveedor() {

        int idProveedor = 1;

        ProveedorNotFoundException exception =
                new ProveedorNotFoundException("Proveedor no encontrado");

        exception.initCause(
                new RuntimeException("Proveedor no encontrado")
        );

        doThrow(exception).when(service).eliminar(idProveedor);

        ResponseEntity<?> respuesta = controller.eliminar(idProveedor);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Proveedor no encontrado", respuesta.getBody());

        verify(service).eliminar(idProveedor);
    }

}
