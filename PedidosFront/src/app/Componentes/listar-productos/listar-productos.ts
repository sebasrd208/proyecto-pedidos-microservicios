import { Component, OnInit } from '@angular/core';
import { Servidor } from '../../Servidor/servidor';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Inventario } from '../../Entidades/productos';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-productos',
  imports: [FormsModule, CommonModule],
  templateUrl: './listar-productos.html',
  styleUrl: './listar-productos.css',
})
export class ListarProductos implements OnInit {

  ngOnInit(): void {
    this.listar();
  }

  constructor(private router: Router, private servidor: Servidor) { }
  productos: Inventario[] = [];
  sortColumn: keyof Inventario = 'idInventario';
  sortDirection: 'asc' | 'desc' = 'asc';

  listar() {
    this.servidor.listarProducto().subscribe({
      next: (data) => {
        this.productos = data;
      },
      error: () => {
        Swal.fire('Error', 'No se pudieron cargar los productos', 'error');
      },
    });
  }

  guardar() {
    this.router.navigate(['guardar-productos']);
  }

  eliminar(idInventario: string) {
    Swal.fire({
      title: '¿Eliminar producto?',
      text: 'Esta accion no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.servidor.eliminarProductos(idInventario).subscribe({
          next: () => {
            Swal.fire('Eliminado', 'Producto eliminado correctamente', 'success');
            this.listar();
          },
          error: (error) => {
            if (error.status === 403) {
              Swal.fire('NO AUTORIZADO', 'No tienes permiso de ADMINISTRADOR', 'warning');
              return;
            }
            Swal.fire('Error', 'No se pudo eliminar', 'error');
          },
        });
      }
    })
  }

  editar(producto_x: Inventario) {
    localStorage.setItem('producto_key', producto_x.idInventario);
    this.router.navigate(['editar-productos']);
  }

  ordenarProductos(columna: keyof Inventario) {
    if (this.sortColumn === columna) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = columna;
      this.sortDirection = 'asc';
    }

    this.productos.sort((a, b) => {
      const valA = a[columna];
      const valB = b[columna];

      if (typeof valA === 'string' && typeof valB === 'string') {
        return this.sortDirection === 'asc'
          ? valA.localeCompare(valB)
          : valB.localeCompare(valA);
      } else {
        return this.sortDirection === 'asc'
          ? (valA as any) - (valB as any)
          : (valB as any) - (valA as any);
      }
    });
  }

}
