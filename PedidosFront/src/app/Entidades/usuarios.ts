export enum Rol {
    ADMIN = 'ADMIN',
    USER = 'USER',
}

export interface Usuarios {
    username: string;
    password: string;
    rol: Rol;
}