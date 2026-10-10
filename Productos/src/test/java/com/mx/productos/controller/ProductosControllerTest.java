package com.mx.productos.controller;

import com.mx.productos.dto.InventarioDTO;
import com.mx.productos.dto.ProductoRequestDTO;
import com.mx.productos.dto.ProductosDTO;
import com.mx.productos.service.ProductosService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class ProductosControllerTest {

    @Mock
    ProductosService service;

    @InjectMocks
    ProductosController controller;

    @Test
    void mostrarProductos_debeRetornarUnaLista200(){
        List<InventarioDTO> lista = List.of(new InventarioDTO());
        when(service.listar()).thenReturn(lista);

        ResponseEntity<?> respuesta = controller.mostrarProductos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(lista, respuesta.getBody());

        verify(service).listar();
    }

    @Test
    void mostrarProductos_debeRetornarBadRequestSiOcurreError() {

        RuntimeException exception = new RuntimeException(
                new RuntimeException("ORA-20002: NO HAY INVENTARIO DISPONIBLE")
        );

        when(service.listar()).thenThrow(exception);

        ResponseEntity<?> respuesta = controller.mostrarProductos();

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("ORA-20002: NO HAY INVENTARIO DISPONIBLE", respuesta.getBody());

        verify(service).listar();
    }

    @Test
    void guardar_debeRetornarCreatedSiInsertaCorrectamente() {

        InventarioDTO dto = new InventarioDTO();

        doNothing().when(service).insertarInventario(dto);

        ResponseEntity<?> respuesta = controller.guardar(dto);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(
                "{\"Mensaje\":\"Registro exitoso\"}",
                respuesta.getBody()
        );

        verify(service).insertarInventario(dto);
    }

    @Test
    void guardar_debeRetornarBadRequestSiOcurreError() {

        InventarioDTO dto = new InventarioDTO();

        doThrow(new RuntimeException("ORA-20003: EL NOMBRE YA EXISTE"))
                .when(service)
                .insertarInventario(dto);

        ResponseEntity<?> respuesta = controller.guardar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("ORA-20003: EL NOMBRE YA EXISTE", respuesta.getBody());

        verify(service).insertarInventario(dto);
    }

    @Test
    void editar_debeRetornarCreatedSiInsertaCorrectamente() {

        InventarioDTO dto = new InventarioDTO();

        doNothing().when(service).actualizarInventario(dto);

        ResponseEntity<?> respuesta = controller.editar(dto);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(
                "{\"Mensaje\":\"Actualización exitoso\"}",
                respuesta.getBody()
        );

        verify(service).actualizarInventario(dto);
    }

    @Test
    void editar_debeRetornarBadRequestSiOcurreError() {

        InventarioDTO dto = new InventarioDTO();

        doThrow(new RuntimeException("ORA-20003: EL PRODUCTO NO EXISTE"))
                .when(service)
                .actualizarInventario(dto);

        ResponseEntity<?> respuesta = controller.editar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("ORA-20003: EL PRODUCTO NO EXISTE", respuesta.getBody());

        verify(service).actualizarInventario(dto);
    }

    @Test
    void eliminar_debeRetornarCreatedSiSeEliminoCorrectamente() {
        String username = "Rancheros";

        doNothing().when(service).eliminar(username);

        ResponseEntity<?> respuesta = controller.eliminar(username);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(
                "{\"Mensaje\":\"Producto eliminado exitosamente\"}",
                respuesta.getBody()
        );

        verify(service).eliminar(username);
    }

    @Test
    void eliminar_debeRetornarBadRequestSiOcurreError() {
        String username = "Rancheros";

        doThrow(new RuntimeException("ORA-20002: NO HAY DATOS DISPONIBLES SOBRE ESTE INVENTARIO"))
                .when(service)
                .eliminar(username);

        ResponseEntity<?> respuesta = controller.eliminar(username);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("ORA-20002: NO HAY DATOS DISPONIBLES SOBRE ESTE INVENTARIO", respuesta.getBody());

        verify(service).eliminar(username);
    }

    @Test
    void mostrarProducto_debeRetornarProductoConStatus200() {

        String idInventario = "1";
        InventarioDTO dto = new InventarioDTO();

        when(service.byIdInventario(idInventario))
                .thenReturn(dto);

        ResponseEntity<?> respuesta = controller.mostrarProducto(idInventario);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(dto, respuesta.getBody());

        verify(service).byIdInventario(idInventario);
    }

    @Test
    void mostrarProducto_debeRetornarBadRequestSiOcurreError() {

        String idInventario = "5";

        RuntimeException exception = new RuntimeException(
                new RuntimeException("ORA-20002: NO HAY DATOS DISPONIBLES SOBRE ESTE INVENTARIO")
        );

        when(service.byIdInventario(idInventario))
                .thenThrow(exception);

        ResponseEntity<?> respuesta = controller.mostrarProducto(idInventario);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("ORA-20002: NO HAY DATOS DISPONIBLES SOBRE ESTE INVENTARIO", respuesta.getBody());

        verify(service).byIdInventario(idInventario);
    }

    @Test
    void generarTicket_debeRetornarTicketGenerado() {

        ProductoRequestDTO request = new ProductoRequestDTO();

        ProductosDTO producto = new ProductosDTO();
        producto.setTotal(36.0);

        List<ProductosDTO> ticket = List.of(producto);

        when(service.generarTicket(request)).thenReturn(ticket);

        ResponseEntity<?> respuesta = controller.generarTicket(request);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(ticket, respuesta.getBody());

        verify(service).generarTicket(request);
    }

    @Test
    void generarTicket_debeRetornarBadRequestSiOcurreError() {

        ProductoRequestDTO request = new ProductoRequestDTO();

        RuntimeException error = new RuntimeException(
                "Error al generar ticket",
                new RuntimeException("ORA-20002: EL PRODUCTO NO EXISTE EN EL INVENTARIO")
        );

        when(service.generarTicket(request)).thenThrow(error);

        ResponseEntity<?> respuesta = controller.generarTicket(request);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("ORA-20002: EL PRODUCTO NO EXISTE EN EL INVENTARIO", respuesta.getBody());

        verify(service).generarTicket(request);
    }
}
