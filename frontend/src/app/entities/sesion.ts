export interface Sesion {
    idPlan: number;
    inicio: Date;
    fin: Date;
    trabajoRealizado: string;
    multimedia: string [];
    descripcion: string;
    presencial: boolean;
    datosSalud: string [];
    id: number;
    idUsuario?: number;
}

export class SesionImpl {
    idPlan: number;
    inicio: Date;
    fin: Date;
    trabajoRealizado: string;
    multimedia: string [];
    descripcion: string;
    presencial: boolean;
    datosSalud: string [];
    id: number;
    idUsuario?: number;

    constructor(){
        this.idPlan = 0;
        this.inicio = new Date;
        this.fin = new Date;
        this.trabajoRealizado = '';
        this.multimedia = ['', ''];
        this.descripcion = '';
        this.presencial = true;
        this.datosSalud = ['', '', '', ''];
        this.id = 0;
        this.idUsuario  = 0;
    }
}