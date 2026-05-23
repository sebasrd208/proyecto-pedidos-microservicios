import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Servidor } from '../../Servidor/servidor';
import { Router } from '@angular/router';
import { ProveedorResponse } from '../../Entidades/proveedores';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-guardar-proveedores',
  imports: [FormsModule],
  templateUrl: './guardar-proveedores.html',
  styleUrl: './guardar-proveedores.css',
})
export class GuardarProveedores {

  constructor(private router: Router, private servidor: Servidor) { }

  proveedor: ProveedorResponse = {
    idProveedor: 0,
    nombre: '',
    rfc: '',
    email: '',
    telefono: '',
    direccion: ''
  }

  registrar() {
    if (!this.proveedor.nombre || !this.proveedor.rfc || !this.proveedor.email
      || !this.proveedor.telefono || !this.proveedor.direccion) {
      Swal.fire('Error', 'Completa todos los campos', 'error');
      return;
    }

    this.servidor.guardarProveedores(this.proveedor).subscribe({
      next: () => {
        Swal.fire('Éxito', 'Proveedor registrado correctamente', 'success');
        this.router.navigate(['listar-proveedores']);
      },
      error: (err) => {
        if (err.status === 409) {
          Swal.fire('ERROR AL REGISTRAR', JSON.stringify(err.error), 'error');
        } else if (err.status === 401) {
          Swal.fire('Error', 'No estas autenticado', 'error');
        } else if (err.status === 403) {
          Swal.fire('NO AUTORIZADO', 'No tienes permiso de ADMINISTRADOR', 'warning');
        } else {
          Swal.fire('Error', 'No se pudo registrar', 'error');
        }
        this.router.navigate(['listar-proveedores']);
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
    this.router.navigate(['listar-proveedores']);
  }
}
