import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Inventario } from '../Entidades/productos';
import { ProveedorRequest, ProveedorResponse } from '../Entidades/proveedores';
import { PedidosResponse, RespuestaRequest } from '../Entidades/pedidos';

@Injectable({
  providedIn: 'root',
})
export class Servidor {

  constructor(private http: HttpClient){}

  url = 'http://localhost:8090';

  //-------------------------PRODUCTOS------------------------------

  listarProducto() {
    return this.http.get<Inventario[]>(this.url + '/productos/mostrar');
  }

  buscarProducto(idInventario: string) {
    return this.http.get<Inventario>(this.url + '/productos/id?idInventario=' + idInventario);
  }

  guardarProducto(invetario: Inventario) {
    return this.http.post<Inventario>(this.url + '/productos/guardar', invetario);
  }

  editarProductos(invetario: Inventario) {
    return this.http.put<Inventario>(this.url + '/productos/editar', invetario);
  }

  eliminarProductos(idInventario: string) {
    return this.http.delete(this.url + '/productos/eliminar?idInventario=' + idInventario);
  }

  //-------------------------PROVEEDORES------------------------------

  listarProveedores() {
    return this.http.get<ProveedorResponse[]>(this.url + '/proveedores');
  }

  buscarProveedores(idProveedor: number) {
    return this.http.get<ProveedorResponse>(this.url + '/proveedores/' + idProveedor);
  }

  guardarProveedores(proveedor: ProveedorResponse) {
    return this.http.post<ProveedorRequest>(this.url + '/proveedores', proveedor);
  }

  editarProveedores(proveedor: ProveedorResponse) {
    return this.http.put<ProveedorRequest>(this.url + '/proveedores', proveedor);
  }

  eliminarProveedores(idProveedor: number) {
    return this.http.delete(this.url + '/proveedores/' + idProveedor);
  }

  //-------------------------PEDIDOS------------------------------

  listarPedidos() {
    return this.http.get<PedidosResponse[]>(this.url + '/pedidos');
  }

  buscarPedidos(idPedido: number) {
    return this.http.get<PedidosResponse>(this.url + '/pedidos/obtener-pdf?idPedido=' + idPedido);
  }

  generarPDF(request: RespuestaRequest) {

    return this.http.post(
      this.url + '/pedidos/pdf',
      request,
      {
        responseType: 'blob'
      }
    );

  }

  obtenerPDF(idPedido: number) {
    return this.http.post(
      this.url + '/pedidos/obtener-pdf',
      null,
      {
        params: {
          idPedido: idPedido
        },
        responseType: 'blob'
      }
    );
  }

  eliminarPedidos(idPedido: number) {
    return this.http.delete(this.url + '/pedidos/eliminar?idPedido=' + idPedido);
  }
}
