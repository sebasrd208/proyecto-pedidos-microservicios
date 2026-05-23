import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProveedorResponse } from '../../Entidades/proveedores';
import { Servidor } from '../../Servidor/servidor';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-editar-proveedores',
  imports: [FormsModule],
  templateUrl: './editar-proveedores.html',
  styleUrl: './editar-proveedores.css',
})
export class EditarProveedores implements OnInit{

  ngOnInit(): void {
    this.buscar();
  }

  constructor(private router: Router, private servidor: Servidor) { }

  proveedor: ProveedorResponse = {
    idProveedor: 0,
    nombre: '',
    rfc: '',
    email: '',
    telefono: '',
    direccion: ''
  }

  buscar() {
    const id = Number(localStorage.getItem('proveedor_key'));

    this.servidor.buscarProveedores(id).subscribe({
      next: (dato) => {
        this.proveedor = dato;

        Swal.fire({
          title: 'CARGA EXITOSA',
          text: "Proveedor " + this.proveedor.idProveedor + " cargado exitosamente",
          showConfirmButton: false,
          icon: 'success'
        });
      }, error: (error) => {
        console.log(JSON.stringify(error))
      }
    });
  }

  editar() {
    if (!this.proveedor.nombre || !this.proveedor.rfc || !this.proveedor.email
      || !this.proveedor.telefono || !this.proveedor.direccion) {
      Swal.fire('Error', 'Completa todos los campos', 'error');
      return;
    }

    this.servidor.editarProveedores(this.proveedor).subscribe({
      next: () => {
        Swal.fire('Éxito', 'Proveedor actualizado correctamente', 'success');
        this.router.navigate(['listar-proveedores']);
      },
      error: (err) => {
        if (err.status === 409) {
          Swal.fire('ERROR AL ACTUALIZAR', JSON.stringify(err.error), 'error');
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
