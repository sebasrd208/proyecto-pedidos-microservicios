import { Component } from '@angular/core';
import Swal from 'sweetalert2';
import { Rol, Usuarios } from '../../Entidades/usuarios';
import { Router } from '@angular/router';
import { AuthService } from '../../Servidor/auth-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-registro',
  imports: [FormsModule],
  templateUrl: './registro.html',
  styleUrl: './registro.css',
})

export class Registro {
  
  confirm = '';
  showPassword: boolean = false;

  constructor(private router: Router, private auth: AuthService){}

  usuario: Usuarios = {
    id: 0,
    nombreCompleto: '',
    username: '',
    password: '',
    rol: Rol.USER
  }

  registrar() {
    if (!this.usuario.username || !this.usuario.nombreCompleto || !this.usuario.password || !this.confirm) {
      Swal.fire('ADVERTENCIA', 'Completa todos los campos', 'warning');
      return;
    }

    if (this.usuario.password !== this.confirm) {
      Swal.fire('ADVERTENCIA', 'Las contraseñas no coinciden', 'warning');
      return;
    }

    console.log(this.usuario);

    this.auth.registrar(this.usuario).subscribe({
      next: () => {
        Swal.fire('Éxito', 'Usuario registrado correctamente', 'success');        
        this.router.navigate(['login']);
        
      },
      error: (error) => {
        console.log(JSON.stringify(error));
        Swal.fire('Error', 'No se pudo registrar', 'error');
      }
    });
  }

  login() {
    this.router.navigate(['login']);
  }  
}
