export enum Estado {
  PENDIENTE = "PENDIENTE",
  PROCESADO = "PROCESADO",
  ENTREGADO = "ENTREGADO",
  CANCELADO = "CANCELADO",
}

export interface Pedidos {
  producto: string;
  cantidad: string;
}

export interface Ticket {
  producto: string;
  precioUnitario: string;
  cantidad: string;
  total: string;
}

export interface ProveedorResponse {
  idProveedor: number;
  nombre: string;
  rfc: string;
  email: string;
  telefono: string;
  direccion: string;
}

export interface Inventario {
  idInventario: string;
  nombre: string;
  precio: string;
  stock: string;
}

export interface PedidosRequest {
  idPedido: number;
  proveedorId: number;
  documento: string;
  total: number;
  estado: Estado;
}

export interface PedidosResponse {
  idPedido: number;
  folio: string;
  proveedorId: number;
  documento: string;
  fechaPedido: string;
  total: number;
  estado: string;
}

export interface ProductoRequest {
  pedidos: Pedidos[];
}

export interface RespuestaRequest {
  proveedor: number;
  pedidos: Pedidos[];
}

export interface Productos {
  compras: Ticket[];
  total: number;
}

export interface TicketProveedorResponse {
  pedido: PedidosResponse;
  proveedor: ProveedorResponse;
  ticket: Productos[];
}