import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Rol } from '../entities/login';
import { Sesion, SesionImpl } from '../entities/sesion';
import { ListadoSesionComponent } from '../listado-sesion/listado-sesion.component';
import { BackendFakeService } from '../services/backend.fake.service';
import { UsuariosService } from '../services/usuarios.service';

@Component({
  selector: 'app-formulario-sesion',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './formulario-sesion.component.html',
  styleUrl: './formulario-sesion.component.css',
})
export class FormularioSesionComponent {
  accion?: "Añadir" | "Editar";
  _sesion: Sesion = new SesionImpl();
  error: string = ''; 
  planes: number [] = [];
  userId?: number;

  constructor(public modal: NgbActiveModal, public backend: BackendFakeService, public usuariosService: UsuariosService) { 
    const u = this.usuariosService.getUsuarioSesion();
    this.userId = u?.id;
    this.obtenerPlanesId();
  }

  get esCliente(){
    return (this.usuariosService.rolCentro?.rol === Rol.CLIENTE);
  }

  get sesion () {
    return this._sesion;
  }

  set sesion(u: Sesion) {
    this._sesion = u;
  }

  /*
    Función para calcular el tiempo de ejercicio
  */
  calcularTiempoSesion(): void {
    const fecha1: Date = new Date(this._sesion.inicio);
    const fecha2: Date = new Date(this._sesion.fin);

    const difMilisec = Math.abs(fecha2.getTime() - fecha1.getTime());
    const difHoras = Math.floor(difMilisec / (1000 * 3600));
    const difMinutos = (difMilisec / (1000 * 60) % 60);

    if(difHoras === 1){
      this.sesion.datosSalud[3] = difHoras + ' hora y ' + difMinutos + ' minutos'; 
    } else{
      this.sesion.datosSalud[3] = difHoras + ' horas y ' + difMinutos + ' minutos'; 
    }
    this.obtenerPlanesId();
  }

  guardarSesion(): void {
    this.sesion.idPlan = Number(this.sesion.idPlan);    //Realizado para sacar un tipo number de lo cogido por el formulario
    this.sesion.idUsuario = this.userId;
    this.calcularTiempoSesion();    
    this.modal.close(this.sesion);
  }

  obtenerPlanesId(): void{
    this.planes = this.backend.obtenerPlanesPorId(this.esCliente,this.userId);
  }
}

