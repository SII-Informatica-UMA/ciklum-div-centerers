export interface AsignacionEntrenamiento {
    id: number;
    idEntrenador: number;
    idCliente: number;
    especialidad: string;
    plan: number;
}

export class AsignacionEntrenamientoImpl {
    id: number;
    idEntrenador: number;
    idCliente: number;
    especialidad: string;
    plan: number;

    constructor(){
        this.id = 0;
        this.idEntrenador = 0;
        this.idCliente = 0;
        this.especialidad = '';
        this.plan = 0;
    }
}