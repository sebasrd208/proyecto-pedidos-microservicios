import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Inventario } from '../../Entidades/productos';
import { Router } from '@angular/router';
import { Servidor } from '../../Servidor/servidor';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-editar-productos',
  imports: [FormsModule],
  templateUrl: './editar-productos.html',
  styleUrl: './editar-productos.css',
})
export class EditarProductos implements OnInit{

  ngOnInit(): void {
    this.buscar();
  }

  producto: Inventario = {
    idInventario: '',
    nombre: '',
    precio: '',
    stock: ''
  }

  constructor(private router: Router, private servidor: Servidor) { }

  buscar() {
    const id = String(localStorage.getItem('producto_key'));

    this.servidor.buscarProducto(id).subscribe({
      next: (dato) => {
        this.producto = dato;

        Swal.fire({
          title: 'CARGA EXITOSA',
          text: "Producto " + this.producto.idInventario + " cargado exitosamente",
          showConfirmButton: false,
          icon: 'success'
        });
      }, error: (error) => {
        console.log(JSON.stringify(error))
      }
    });
  }

  editar() {
    if (!this.producto.nombre || !this.producto.precio || !this.producto.stock) {
      Swal.fire('Error', 'Completa todos los campos', 'error');
      return;
    }

    this.servidor.editarProductos(this.producto).subscribe({
      next: () => {
        Swal.fire('Éxito', 'Producto actualizado correctamente', 'success');
        this.router.navigate(['listar-productos']);
      },
      error: (err) => {
        if (err.status === 400) {
          Swal.fire('ERROR AL ACTUALIZAR', JSON.stringify(err.error), 'error');
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
