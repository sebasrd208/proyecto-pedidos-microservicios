package com.mx.productos.service;

import com.mx.productos.dto.*;
import com.mx.productos.mappers.MapeoGeneral;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductosServiceTest {

    @Mock
    MapeoGeneral mapeo;

    @InjectMocks
    ProductosService service;

    @Test
    void listar_debeRetornarUnEstado200(){

        InventarioDTO producto1 = new InventarioDTO();
        producto1.setIdInventario("1");
        producto1.setNombre("Cheetos");
        producto1.setPrecio("14");
        producto1.setStock("10");

        InventarioDTO producto2 = new InventarioDTO();
        producto2.setIdInventario("2");
        producto2.setNombre("Fritos");
        producto2.setPrecio("18");
        producto2.setStock("10");

        List<InventarioDTO> lista = List.of(producto1, producto2);

        doAnswer(invocation -> {
            Map<String, Object> params = invocation.getArgument(0);
            params.put("rec_cursor", lista);
            return null;
        }).when(mapeo).SP_GETINVENTARIO(anyMap());

        List<InventarioDTO> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertEquals(lista, resultado);

        verify(mapeo).SP_GETINVENTARIO(anyMap());
    }

    @Test
    void insertarInventario_debeLlamarAlMapperConParametrosCorrectos() {

        InventarioDTO dto = new InventarioDTO();
        dto.setNombre("Cheetos");
        dto.setPrecio("14");
        dto.setStock("10");

        doNothing()
                .when(mapeo)
                .SP_SETINVENTARIO(anyMap());

        service.insertarInventario(dto);

        verify(mapeo).SP_SETINVENTARIO(
                argThat(params ->
                        "Cheetos".equals(params.get("PA_NOMBRE"))
                                && "14".equals(params.get("PA_PRECIO"))
                                && "10".equals(params.get("PA_STOCK"))
                )
        );
    }

    @Test
    void insertarInventario_debeLanzarRuntimeExceptionSiOracleFalla() {

        InventarioDTO dto = new InventarioDTO();
        dto.setNombre("Cheetos");
        dto.setPrecio("14");
        dto.setStock("10");

        RuntimeException causa =
                new RuntimeException("Error al insertar inventario");

        DataAccessException exception =
                new DataAccessResourceFailureException(
                        "ORA-20003: EL NOMBRE YA EXISTE",
                        causa
                );

        doThrow(exception)
                .when(mapeo)
                .SP_SETINVENTARIO(anyMap());

        RuntimeException resultado = assertThrows(
                RuntimeException.class,
                () -> service.insertarInventario(dto)
        );

        assertEquals(
                "Error al insertar inventario",
                resultado.getMessage()
        );
    }

    @Test
    void byIdInventario_debeRetornarUsuarioEncontrado() {

        InventarioDTO producto = new InventarioDTO();
        producto.setIdInventario("2");
        producto.setNombre("Fritos");
        producto.setPrecio("18");
        producto.setStock("10");

        doAnswer(invocation -> {
            Map<String, Object> params = invocation.getArgument(0);
            params.put("rec_cursor", List.of(producto));
            return null;
        }).when(mapeo).SP_GET_ID_INVENTARIO(anyMap());

        InventarioDTO resultado = service.byIdInventario("2");

        assertEquals(producto, resultado);

        verify(mapeo).SP_GET_ID_INVENTARIO(argThat(params ->
                "2".equals(params.get("PA_ID"))
        ));
    }

    @Test
    void byIdInventario_debeRetornarErrorSiFallaConsulta() {

        DataAccessException excepcion =
                new DataAccessResourceFailureException(
                        "ORA-20003: NO HAY DATOS DISPONIBLES SOBRE ESTE INVENTARIO",
                        new RuntimeException("Error al consultar inventario")
                );

        doThrow(excepcion)
                .when(mapeo)
                .SP_GET_ID_INVENTARIO(anyMap());

        RuntimeException resultado =
                assertThrows(
                        RuntimeException.class,
                        () -> service.byIdInventario("2")
                );

        assertEquals(
                "Error al consultar inventario",
                resultado.getMessage()
        );

        verify(mapeo).SP_GET_ID_INVENTARIO(argThat(params ->
                "2".equals(params.get("PA_ID"))
        ));
    }

    @Test
    void actualizarInventario_debeActualizarCorrectamente() {
        InventarioDTO producto = new InventarioDTO();
        producto.setNombre("Fritos");
        producto.setPrecio("18");
        producto.setStock("10");

        doNothing()
                .when(mapeo)
                .SP_UPDATE_INVENTARIO(anyMap());

        assertDoesNotThrow(() -> service.actualizarInventario(producto));

        verify(mapeo).SP_UPDATE_INVENTARIO(argThat(params ->
                "Fritos".equals(params.get("PA_NOMBRE"))
                        && "18".equals(params.get("PA_PRECIO"))
                        && "10".equals(params.get("PA_STOCK"))
        ));
    }

    @Test
    void actualizarInventario_debeRetornarErrorSiFallaActualizacion() {
        DataAccessException excepcion = new DataAccessResourceFailureException(
                "ORA-20003: EL PRODUCTO NO EXISTE",
                new RuntimeException("Error al actualizar inventario")
        );

        doThrow(excepcion)
                .when(mapeo)
                .SP_UPDATE_INVENTARIO(anyMap());

        RuntimeException resultado = assertThrows(
                RuntimeException.class,
                () -> service.actualizarInventario(new InventarioDTO())
        );

        assertEquals("Error al actualizar inventario", resultado.getMessage());

        verify(mapeo).SP_UPDATE_INVENTARIO(anyMap());
    }

    @Test
    void eliminar_debeEliminarInventarioCorrectamente() {
        doNothing()
                .when(mapeo)
                .SP_DELETE_INVENTARIO(anyMap());

        assertDoesNotThrow(() -> service.eliminar("2"));

        verify(mapeo).SP_DELETE_INVENTARIO(argThat(params ->
                "2".equals(params.get("PA_ID"))
        ));
    }

    @Test
    void eliminar_debeRetornarErrorSiFallaEliminacion() {
        DataAccessException excepcion = new DataAccessResourceFailureException(
                "ORA-20002: NO HAY DATOS DISPONIBLES SOBRE ESTE INVENTARIO",
                new RuntimeException("Error al eliminar inventario")
        );

        doThrow(excepcion)
                .when(mapeo)
                .SP_DELETE_INVENTARIO(anyMap());

        RuntimeException resultado = assertThrows(
                RuntimeException.class,
                () -> service.eliminar("2")
        );

        assertEquals("Error al eliminar inventario", resultado.getMessage());

        verify(mapeo).SP_DELETE_INVENTARIO(argThat(params ->
                "2".equals(params.get("PA_ID"))
        ));
    }

    @Test
    void generarTicket_debeGenerarTicketYCalcularTotal() {
        PedidosDTO pedido = new PedidosDTO();
        pedido.setProducto("Fritos");
        pedido.setCantidad("2");

        ProductoRequestDTO compras = new ProductoRequestDTO();
        compras.setPedidos(List.of(pedido));

        TicketDTO ticket = new TicketDTO();
        ticket.setTotal("36");

        doAnswer(invocation -> {
            Map<String, Object> params = invocation.getArgument(0);
            params.put("rec_cursor", List.of(ticket));
            return null;
        }).when(mapeo).SP_TICKET_INVENTARIO(anyMap());

        List<ProductosDTO> resultado = service.generarTicket(compras);

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getCompras().size());
        assertEquals(ticket, resultado.get(0).getCompras().get(0));
        assertEquals(36.0, resultado.get(0).getTotal(), 0.001);

        verify(mapeo).SP_TICKET_INVENTARIO(argThat(params ->
                "Fritos".equals(params.get("PA_PRODUCTO"))
                        && "2".equals(params.get("PA_CANTIDAD"))
        ));
    }

    @Test
    void generarTicket_debeRetornarErrorSiFallaConsulta() {
        PedidosDTO pedido = new PedidosDTO();
        pedido.setProducto("Fritos");
        pedido.setCantidad("2");

        ProductoRequestDTO compras = new ProductoRequestDTO();
        compras.setPedidos(List.of(pedido));

        DataAccessException excepcion = new DataAccessResourceFailureException(
                "ORA-20002: EL PRODUCTO NO EXISTE EN EL INVENTARIO",
                new RuntimeException("Error al generar ticket")
        );

        doThrow(excepcion)
                .when(mapeo)
                .SP_TICKET_INVENTARIO(anyMap());

        RuntimeException resultado = assertThrows(
                RuntimeException.class,
                () -> service.generarTicket(compras)
        );

        assertEquals("Error al generar ticket", resultado.getMessage());

        verify(mapeo).SP_TICKET_INVENTARIO(argThat(params ->
                "Fritos".equals(params.get("PA_PRODUCTO"))
                        && "2".equals(params.get("PA_CANTIDAD"))
        ));
    }
}
