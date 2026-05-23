import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Servidor } from '../../Servidor/servidor';
import { Router } from '@angular/router';
import { ProveedorResponse } from '../../Entidades/proveedores';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-proveedores',
  imports: [FormsModule, CommonModule],
  templateUrl: './listar-proveedores.html',
  styleUrl: './listar-proveedores.css',
})
export class ListarProveedores implements OnInit{

  ngOnInit(): void {
      this.listar();
  }

  constructor(private router: Router, private servidor: Servidor){}

  proveedores: ProveedorResponse[] = [];
  sortColumn: keyof ProveedorResponse = 'idProveedor';
  sortDirection: 'asc' | 'desc' = 'asc';

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
    this.router.navigate(['guardar-proveedores']);
  }

  eliminar(idProveedor: number) {
    Swal.fire({
      title: '¿Eliminar proveedor?',
      text: 'Esta accion no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.servidor.eliminarProveedores(idProveedor).subscribe({
          next: () => {
            Swal.fire('Eliminado', 'Proveedor eliminado correctamente', 'success');
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

  editar(proveedor_x: ProveedorResponse) {
    localStorage.setItem('proveedor_key', proveedor_x.idProveedor.toString());
    this.router.navigate(['editar-proveedores']);
  }

  ordenarProveedores(columna: keyof ProveedorResponse) {
    if (this.sortColumn === columna) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = columna;
      this.sortDirection = 'asc';
    }

    this.proveedores.sort((a, b) => {
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
