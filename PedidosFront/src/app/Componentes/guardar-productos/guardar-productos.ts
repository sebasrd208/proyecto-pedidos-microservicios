import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Inventario } from '../../Entidades/productos';
import { Router } from '@angular/router';
import { Servidor } from '../../Servidor/servidor';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-guardar-productos',
  imports: [FormsModule],
  templateUrl: './guardar-productos.html',
  styleUrl: './guardar-productos.css',
})
export class GuardarProductos {

  constructor(private router: Router, private servidor: Servidor) { }

  producto: Inventario = {
    idInventario: '',
    nombre: '',
    precio: '',
    stock: ''
  }

  registrar() {
    if (!this.producto.nombre || !this.producto.precio || !this.producto.stock) {
      Swal.fire('Error', 'Completa todos los campos', 'error');
      return;
    }

    this.servidor.guardarProducto(this.producto).subscribe({
      next: () => {
        Swal.fire('Éxito', 'Producto registrado correctamente', 'success');
        this.router.navigate(['listar-productos']);
      },
      error: (err) => {
        if (err.status === 400) {
          Swal.fire('ERROR AL REGISTRAR', JSON.stringify(err.error), 'error');
        } else if (err.status === 401) {
          Swal.fire('Error', 'No estas autenticado', 'error');
        } else if (err.status === 403) {
          Swal.fire('NO AUTORIZADO', 'No tienes permiso de ADMINISTRADOR', 'warning');
        } else {
          Swal.fire('Error', 'No se pudo registrar', 'error');
        }
        this.router.navigate(['listar-productos']);
      }
    });
  }

  cancelar() {
    Swal.fire({
      title: 'Cancelado!',
      text: 'Se ha cancelado la modificación...',
      showConfirmButton: false,
      icon: 'warning',
    });
    this.router.navigate(['listar-productos']);
  }
}
