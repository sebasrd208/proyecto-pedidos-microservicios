import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Usuarios } from '../Entidades/usuarios';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(private http: HttpClient) {}

  private usernameSubject = new BehaviorSubject<string>(
    localStorage.getItem('username') || ''
  );

  username$ = this.usernameSubject.asObservable();
  url = 'http://localhost:8090';

  registro(user: Usuarios) {
    return this.http.post(this.url + '/usuarios/registro', user);
  }

  login(username: string, password: string) {
    const authHeader = 'Basic ' + btoa(username + ':' + password);
    localStorage.setItem('auth', authHeader);
    const headers = new HttpHeaders({
      Authorization: authHeader
    });

    return this.http.get(this.url + '/proveedores', { headers });
  }

  saveSession(username: string, password: string) {
    localStorage.setItem('auth', btoa(username + ':' + password));
    localStorage.setItem('username', username);
    localStorage.setItem('password', password);
    this.usernameSubject.next(username);
  }

  getAuthHeader() {
    const token = localStorage.getItem('auth');
    return {
      Authorization: 'Basic' + token
    }
  }

  getUsername(): string {
    return localStorage.getItem('username') || '';
  }

  logout() {
    localStorage.removeItem('auth');
    localStorage.removeItem('username');
    localStorage.removeItem('password');
    this.usernameSubject.next('');
  }

  isLogged() {
    return localStorage.getItem('auth') != null;
  }

  logeado(): boolean {    
    return !!localStorage.getItem('username');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('auth');
  }
  
}
