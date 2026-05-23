import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../Servidor/auth-service';
import Swal from 'sweetalert2';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-navbar',
  imports: [FormsModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar implements OnInit{

  username: string = '';

  ngOnInit() {
    this.authService.username$.subscribe(username => {
      this.username = username;
    });
  }

  constructor(
    private router: Router,
    private authService: AuthService,
  ) { }

  isLoggedIn() {
    return this.authService.logeado();
  }

  logout() {
    Swal.fire({
      title: 'CERRAR SESIÓN',
      text: '¿Deseas cerrar sesión?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Cerrar sesión',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: 'SESION FINALIZADA',
          text: "Cerraste sesión exitosamente",
          showConfirmButton: false,
          icon: 'success'
        });
        this.authService.logout();
        this.router.navigate(['login']);
      }
    })
  }

  getUsername() {
    return localStorage.getItem('username') || '';
  }

  registro() {
    this.router.navigate(['registros']);
  }

  guardar() {
    this.router.navigate(['guardar-producto']);
  }
}
