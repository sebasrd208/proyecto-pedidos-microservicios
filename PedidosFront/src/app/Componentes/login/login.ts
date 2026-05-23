import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Servidor } from '../../Servidor/servidor';
import { AuthService } from '../../Servidor/auth-service';
import Swal from 'sweetalert2';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  constructor(
    private router: Router,
    private service: Servidor,
    private authService: AuthService,
  ) { }

  username: string = '';
  password: string = '';
  showPassword: boolean = false;

  login() {
    if (!this.username || !this.password) {
      Swal.fire('Error', 'Completa todos los campos', 'error');
      return;
    }
    this.authService.saveSession(this.username, this.password);

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.service.listarProveedores().subscribe({

          next: () => {
            Swal.fire('ACCESO CONCEDIDO', 'Bienvenido ' + this.username, 'success');
            this.router.navigate(['listar-proveedores']);
          },
          error: (err) => {
            this.authService.logout();
            Swal.fire('Error', 'Error cargando proveedores', 'error');
          }
        });
      },
      error: (err) => {
        this.authService.logout();
        if (err.status === 401) {          
            Swal.fire('Error', 'Credenciales incorrectas', 'error');          
        } else {
            Swal.fire('Error', 'Error del servidor', 'error');          
        }        
      }
    });
  }

  isLoggedIn() {
    return this.authService.isLogged();
  }

  registro() {
    this.router.navigate(['registros']);
  }
}
