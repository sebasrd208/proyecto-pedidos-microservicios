import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Servidor } from '../../Servidor/servidor';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { PedidosResponse } from '../../Entidades/pedidos';

@Component({
  selector: 'app-listar-pedidos',
  imports: [FormsModule, CommonModule],
  templateUrl: './listar-pedidos.html',
  styleUrl: './listar-pedidos.css',
})
export class ListarPedidos implements OnInit{

  ngOnInit(): void {
    this.listar();
  }

  constructor(private router: Router, private servidor: Servidor) { }

  pedidos: PedidosResponse[] = [];
  sortColumn: keyof PedidosResponse = 'idPedido';
  sortDirection: 'asc' | 'desc' = 'asc';


  listar() {
    this.servidor.listarPedidos().subscribe({
      next: (data) => {
        this.pedidos = data;
        console.log(JSON.stringify(data));
      },
      error: () => {
        Swal.fire('Error', 'No se pudieron cargar los pedidos', 'error');
      },
    });
  }

  guardar() {
    this.router.navigate(['guardar-pedidos']);
  }

  obtenerPDF(idPedido: number) {
    this.servidor.obtenerPDF(idPedido).subscribe({
      next: (pdf: Blob) => {

        const blob = new Blob([pdf], {
          type: 'application/pdf'
        });

        const url = window.URL.createObjectURL(blob);

        window.open(url, '_blank');
      },
      error: (error) => {
        if (error.status === 409) {
          Swal.fire('ERROR AL REGISTRAR', JSON.stringify(error.error), 'error');
        } else if (error.status === 401) {
          Swal.fire('Error', 'No estas autenticado', 'error');
        } else if (error.status === 403) {
          Swal.fire({
            title: 'NO AUTORIZADO',
            text: 'Necesitas permisos de ADMINISTRADOR para ver este documento',
            icon: 'warning'
          });
        } else {
          Swal.fire('Error', 'No se pudo registrar', 'error');
        }
      }
    });
  }

  eliminar(idPedido: number) {
    Swal.fire({
      title: '¿Eliminar pedido?',
      text: 'Esta accion no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.servidor.eliminarPedidos(idPedido).subscribe({
          next: () => {
            Swal.fire('Eliminado', 'Pedido eliminado correctamente', 'success');
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

  ordenarPedidos(columna: keyof PedidosResponse) {
    if (this.sortColumn === columna) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = columna;
      this.sortDirection = 'asc';
    }

    this.pedidos.sort((a, b) => {
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
