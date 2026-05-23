package com.mx.productos.mappers;

import java.util.*;
import com.mx.productos.dto.*;
import com.mx.productos.values.*;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MapeoGeneral {

    @Select(GeneralValue.SP_SETINVENTARIO)
    @Options(statementType = StatementType.CALLABLE)
    public void SP_SETINVENTARIO(Map<String, Object> params);

    @Results(
            id = "r_SP_GETINVENTARIO",
            value = {
                @Result(property = "idInventario", column = "ID_INVENTARIO", id = true),
                @Result(property = "nombre", column = "NOMBRE"),
                @Result(property = "precio", column = "PRECIO"),
                @Result(property = "stock", column = "STOCK")
            }
    )
    @Select(GeneralValue.SP_GETINVENTARIO)
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(InventarioDTO.class)
    public void SP_GETINVENTARIO(Map<String, Object> params);

    @Results(
            id = "r_SP_GET_ID_INVENTARIO",
            value = {
                    @Result(property = "idInventario", column = "ID_INVENTARIO", id = true),
                    @Result(property = "nombre", column = "NOMBRE"),
                    @Result(property = "precio", column = "PRECIO"),
                    @Result(property = "stock", column = "STOCK")
            }
    )
    @Select(GeneralValue.SP_GET_ID_INVENTARIO)
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(InventarioDTO.class)
    public void SP_GET_ID_INVENTARIO(Map<String, Object> params);

    @Select(GeneralValue.SP_UPDATE_INVENTARIO)
    @Options(statementType = StatementType.CALLABLE)
    public void SP_UPDATE_INVENTARIO(Map<String, Object> params);

    @Select(GeneralValue.SP_DELETE_INVENTARIO)
    @Options(statementType = StatementType.CALLABLE)
    public void SP_DELETE_INVENTARIO(Map<String, Object> params);

    @Results(
            id = "r_SP_TICKET_INVENTARIO",
            value = {
                    @Result(property = "producto", column = "PRODUCTO", id = true),
                    @Result(property = "precioUnitario", column = "PRECIO_UNITARIO"),
                    @Result(property = "cantidad", column = "CANTIDAD"),
                    @Result(property = "total", column = "TOTAL")
            }
    )
    @Select(GeneralValue.SP_TICKET_INVENTARIO)
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(TicketDTO.class)
    public void SP_TICKET_INVENTARIO(Map<String, Object> params);
}
