import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Servidor } from '../../Servidor/servidor';
import { RespuestaRequest } from '../../Entidades/pedidos';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { ProveedorResponse } from '../../Entidades/proveedores';
import { Inventario } from '../../Entidades/productos';

@Component({
  selector: 'app-guardar-pedidos',
  imports: [FormsModule, CommonModule],
  templateUrl: './guardar-pedidos.html',
  styleUrl: './guardar-pedidos.css',
})
export class GuardarPedidos implements OnInit {

  ngOnInit(): void {
    this.listar();
    this.listarProductos();
  }
  constructor(private router: Router, private servidor: Servidor) { }

  proveedores: ProveedorResponse[] = [];
  productos: Inventario[] = [];
  filtroProductos: string = '';
  secciones: string[] = [];
  pedido: RespuestaRequest = {
    proveedor: 0,
    pedidos: [
      {
        producto: '',
        cantidad: ''
      }
    ]
  };

  listarProductos() {
    this.servidor.listarProducto().subscribe({
      next: (data) => {
        this.productos = data;
      },
      error: () => {
        Swal.fire('Error', 'No se pudieron cargar los productos', 'error');
      },
    });
  }


  listar() {
    this.servidor.listarProveedores().subscribe({
      next: (data) => {
        this.proveedores = data;
        console.log(JSON.stringify(data));
      },
      error: () => {
        Swal.fire('Error', 'No se pudieron cargar los proveedores', 'error');
      },
    });
  }

  guardar() {

    if (!this.pedido.proveedor || this.pedido.proveedor === 0) {
      Swal.fire(
        'Proveedor inválido',
        'Debes seleccionar un proveedor',
        'warning'
      );
      return;
    }

    const productoInvalido = this.pedido.pedidos.some(p =>
      !p.producto || p.producto.trim() === ''
    );

    if (productoInvalido) {
      Swal.fire(
        'Producto inválido',
        'Debes seleccionar un producto',
        'warning'
      );
      return;
    }

    if (this.pedido.pedidos.some(p => !p.cantidad)) {
      Swal.fire('Error', 'Completa todos los campos', 'error');
      return;
    }


    if (this.pedido.pedidos.some(p => !p.cantidad)) {
      Swal.fire('Error', 'Completa todos los campos', 'error');
      return;
    }

    const stockInsuficiente = this.pedido.pedidos.some(pedidoDetalle => {
      const producto = this.productos.find(prod =>
        prod.nombre.toLowerCase().trim() === pedidoDetalle.producto.toLowerCase().trim()
      );

      if (!producto) {
        return false;
      }

      return Number(pedidoDetalle.cantidad) > Number(producto.stock);
    });

    if (stockInsuficiente) {
      Swal.fire(
        'ADVERTENCIA',
        'Cantidad insuficiente',
        'warning'
      );
      return;
    }

    const cantidadInvalida = this.pedido.pedidos.some(p =>
      Number(p.cantidad) <= 0
    );

    if (cantidadInvalida) {
      Swal.fire(
        'ADVERTENCIA',
        'No se permiten cantidades negativas o iguales a 0',
        'warning'
      );
      return;
    }

    this.servidor.generarPDF(this.pedido).subscribe({
      next: (pdf: Blob) => {
        const blob = new Blob([pdf], {
          type: 'application/pdf'
        });

        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');
        this.router.navigate(['listar-pedidos']);
        Swal.fire({
          title: 'PDF GENERADO',
          text: 'El ticket fue generado correctamente',
          icon: 'success',
          showConfirmButton: false,
          timer: 1500
        });

      },
      error: (error) => {
        console.log(JSON.stringify(error));
        if (error.status === 401) {
          Swal.fire('Error', 'No estas autenticado', 'error');
        } else if (error.status === 403) {
          Swal.fire('NO AUTORIZADO', 'No tienes permiso de ADMINISTRADOR', 'warning');
        } else if (error.status === 409) {
          Swal.fire('ERROR AL REGISTRAR', JSON.stringify(error.error), 'error');
        } else {
          Swal.fire('Error', 'No se pudo generar el PDF', 'error');
        }
        this.router.navigate(['listar-pedidos']);
      }
    });
  }

  agregarPedido() {
    this.pedido.pedidos.push({
      producto: '',
      cantidad: ''
    });
  }

  getNombreConStock(nombre: string): string {
    const prod = this.productos.find(p => p.nombre === nombre);
    return prod ? `${prod.nombre}: ${prod.stock}` : '';
  }

  cancelar() {
    Swal.fire({
      title: 'Cancelado!',
      text: 'Se ha cancelado la modificación...',
      showConfirmButton: false,
      icon: 'warning',
    });
    this.router.navigate(['listar-pedidos']);
  }

  pedidosFiltrados(): Inventario[] {
    if (!this.filtroProductos) {
      return this.productos;
    }

    return this.productos.filter(pro => pro.stock === this.filtroProductos);
  }

  get totalFiltradosProductos(): number {
    return this.pedidosFiltrados().length;
  }

  get textoFiltroProductos(): string {
    if (!this.filtroProductos) {
      return 'Total: ' + this.totalFiltradosProductos;
    }

    return this.filtroProductos + ": " + this.totalFiltradosProductos;
  }

  retirarPedido() {
    if (this.pedido.pedidos.length > 1) {
      this.pedido.pedidos.pop();
    }
  }

  productosDisponibles(indexActual: number): Inventario[] {

    const productosSeleccionados = this.pedido.pedidos
      .filter((_, index) => index !== indexActual)
      .map(p => p.producto)
      .filter(p => p !== '');

    return this.productos.filter(prod =>
      !productosSeleccionados.includes(prod.nombre)
    );

  }
}
