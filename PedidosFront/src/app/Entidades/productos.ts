export interface Inventario {
  idInventario: string;
  nombre: string;
  precio: string;
  stock: string;
}

export interface Pedidos {
  producto: string;
  cantidad: string;
}

export interface ProductoRequest {
  pedidos: Pedidos[];
}

export interface Ticket {
  producto: string;
  precioUnitario: string;
  cantidad: string;
  total: string;
}

export interface Productos {
  compras: Ticket[];
  total: number;
}