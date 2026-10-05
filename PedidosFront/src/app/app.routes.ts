import { Routes } from '@angular/router';
import { Registro } from './Componentes/registro/registro';
import { Login } from './Componentes/login/login';
import { ListarProveedores } from './Componentes/listar-proveedores/listar-proveedores';
import { guardsGuard } from './Guard/guards-guard';
import { ListarProductos } from './Componentes/listar-productos/listar-productos';
import { ListarPedidos } from './Componentes/listar-pedidos/listar-pedidos';
import { EditarProveedores } from './Componentes/editar-proveedores/editar-proveedores';
import { EditarProductos } from './Componentes/editar-productos/editar-productos';
import { GuardarProveedores } from './Componentes/guardar-proveedores/guardar-proveedores';
import { GuardarProductos } from './Componentes/guardar-productos/guardar-productos';
import { GuardarPedidos } from './Componentes/guardar-pedidos/guardar-pedidos';
import { RecuperarPassword } from './Componentes/recuperar-password/recuperar-password';
import { EditarUsuarios } from './Componentes/editar-usuarios/editar-usuarios';
import { CambiarPassword } from './Componentes/cambiar-password/cambiar-password';
import { ListarUsuarios } from './Componentes/listar-usuarios/listar-usuarios';

export const routes: Routes = [
    {
        path: 'recuperar-password',
        component: RecuperarPassword
    },
    {
        path: 'registros',
        component: Registro
    },
    {
        path: 'login',
        component: Login
    },
    {
        path: 'listar-proveedores',
        component: ListarProveedores,
        canActivate: [guardsGuard]
    },
    {
        path: 'listar-productos',
        component: ListarProductos,
        canActivate: [guardsGuard]
    },
    {
        path: 'listar-pedidos',
        component: ListarPedidos,
        canActivate: [guardsGuard]
    },
    {
        path: 'listar-usuarios',
        component: ListarUsuarios,
        canActivate: [guardsGuard]
    },
    {
        path: 'editar-proveedores',
        component: EditarProveedores,
        canActivate: [guardsGuard]
    },
    {
        path: 'editar-productos',
        component: EditarProductos,
        canActivate: [guardsGuard]
    },
    {
        path: 'editar-usuarios',
        component: EditarUsuarios,
        canActivate: [guardsGuard]
    },
    {
        path: 'guardar-proveedores',
        component: GuardarProveedores,
        canActivate: [guardsGuard]
    },
    {
        path: 'guardar-productos',
        component: GuardarProductos,
        canActivate: [guardsGuard]
    },
    {
        path: 'guardar-pedidos',
        component: GuardarPedidos,
        canActivate: [guardsGuard],
    },
    {
        path: 'cambiar-password',
        component: CambiarPassword,
        canActivate: [guardsGuard]
    },
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    {
        path: '**',
        redirectTo: 'login',
    }
];
