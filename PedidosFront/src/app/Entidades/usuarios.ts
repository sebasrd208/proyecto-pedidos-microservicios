export enum Rol {
    ADMIN = 'ADMIN',
    USER = 'USER',
}

export interface Usuarios {
    id: Number;
    nombreCompleto: string;
    username: string;
    password: string;
    rol: Rol;
}