package com.mx.productos.values;

public class GeneralValue {

    public static final String SP_SETINVENTARIO
            = "{ call PA_INVENTARIO.SP_SETINVENTARIO(" +
            "#{PA_NOMBRE, mode=IN, jdbcType=VARCHAR}," +
            "#{PA_PRECIO, mode=IN, jdbcType=VARCHAR}," +
            "#{PA_STOCK,  mode=IN, jdbcType=VARCHAR}" +
            ")}";

    public static final String SP_GETINVENTARIO
            = "{ call PA_INVENTARIO.SP_GETINVENTARIO(" +
            " #{rec_cursor, mode=OUT, jdbcType=CURSOR, javaType=ResultSet, resultMap=r_SP_GETINVENTARIO}" +
            ")}";

    public static final String SP_GET_ID_INVENTARIO
            = "{ call PA_INVENTARIO.SP_GET_ID_INVENTARIO(" +
            " #{rec_cursor, mode=OUT, jdbcType=CURSOR, javaType=ResultSet, resultMap=r_SP_GET_ID_INVENTARIO}," +
            " #{PA_ID, mode=IN, jdbcType=VARCHAR}" +
            ")}";

    public static final String SP_UPDATE_INVENTARIO
            ="{ call PA_INVENTARIO.SP_UPDATE_INVENTARIO(" +
            "#{PA_NOMBRE, mode=IN, jdbcType=VARCHAR}," +
            "#{PA_PRECIO, mode=IN, jdbcType=VARCHAR}," +
            "#{PA_STOCK,  mode=IN, jdbcType=VARCHAR}" +
            ")}";

    public static final String SP_DELETE_INVENTARIO
            =" { call PA_INVENTARIO.SP_DELETE_INVENTARIO(" +
            " #{PA_ID, mode=IN, jdbcType=VARCHAR}" +
            ")}";

    public static final String SP_TICKET_INVENTARIO
            ="{ call PA_INVENTARIO.SP_TICKET_INVENTARIO(" +
            " #{rec_cursor, mode=OUT, jdbcType=CURSOR, javaType=ResultSet, resultMap=r_SP_TICKET_INVENTARIO}," +
            "#{PA_PRODUCTO, mode=IN, jdbcType=VARCHAR}," +
            "#{PA_CANTIDAD, mode=IN, jdbcType=VARCHAR}" +
            ")}";

}