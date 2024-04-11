import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Rol } from '../entities/login';
import { Sesion, SesionImpl } from '../entities/sesion';
import { SesionesService } from '../services/sesiones.service';
import { FormularioSesionComponent } from '../formulario-sesion/formulario-sesion.component';
import { UsuariosService } from '../services/usuarios.service';
import { AsignacionEntrenamiento } from '../entities/asignacionentrenamientos';
import { BackendFakeService } from '../services/backend.fake.service';

@Component({
  selector: 'app-listado-sesion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './listado-sesion.component.html',
  styleUrl: './listado-sesion.component.css'
})
export class ListadoSesionComponent {
  sesiones: Sesion [] = [];
  sesionesfiltrado: Sesion[] = [];
  userId?: number;
  asignaciones: AsignacionEntrenamiento [] = [];

  constructor(private sesionesService: SesionesService, private usuariosService: UsuariosService, private backend: BackendFakeService,private modalService: NgbModal) {
    this.usuariosService.userIdChanged.subscribe(userId => {
      this.userId = userId;
    });
    
    this.actualizarSesiones();
   }
  private get rol() {
    return this.usuariosService.rolCentro;
  }
  private get id() { 
    return this.userId;
  }

  isCliente(): boolean {
    return (this.rol?.rol == Rol.CLIENTE);
  }

  isClienteOrEntrenador(): boolean {
    if(this.rol?.rol == Rol.CLIENTE){
      //console.log("ES CLIENTE");
      this.sesionesfiltrado = this.backend.filtrarSesionesPorIdCliente(this.userId);
    } else if (this.rol?.rol == Rol.ENTRENADOR) {
      //console.log("ES ENTRENADOR");
      this.sesionesfiltrado = this.backend.filtrarSesionesPorIdEntrenador(this.userId);
    }
    return (this.rol?.rol == Rol.CLIENTE || this.rol?.rol == Rol.ENTRENADOR);
  }

  ngOnInit(): void {
    this.actualizarSesiones();
  }

  get listaPlanes(){
    if(this.rol?.rol == Rol.CLIENTE){
      return this.backend.obtenerPlanesPorId(true, this.userId);
    } else if (this.rol?.rol == Rol.ENTRENADOR) {
      return this.backend.obtenerPlanesPorId(false, this.userId);
    } else {
      return [];
    }
  }

  actualizarSesiones() {
    this.sesionesService.getSesiones().subscribe(sesiones => {
      this.sesiones = sesiones;
    });

    if(this.rol?.rol == Rol.CLIENTE){
      //console.log("ES CLIENTE");
      this.sesionesfiltrado = this.backend.filtrarSesionesPorIdCliente(this.userId);
    } else if (this.rol?.rol == Rol.ENTRENADOR) {
      //console.log("ES ENTRENADOR");
      this.sesionesfiltrado = this.backend.filtrarSesionesPorIdEntrenador(this.userId);
    } else {
      //console.log("ES OTRA COSA");
    }
    
  }

  aniadirSesion(): void {
    let ref = this.modalService.open(FormularioSesionComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.sesion = new SesionImpl();
    ref.result.then((sesion: Sesion) => {
      this.sesionesService.aniadirSesion(sesion).subscribe(sesion => {
        this.actualizarSesiones();
      });
    }, (reason) => {});
  }

  private sesionEditado(sesion: Sesion): void {
    this.sesionesService.editarSesion(sesion).subscribe(() => {
      this.actualizarSesiones();
    });
  }

  eliminarSesion(id: number): void {
    this.sesionesService.eliminarSesion(id).subscribe(() => {
      this.actualizarSesiones();
    });
  }

  editarSesion(sesion: Sesion): void {
    let ref = this.modalService.open(FormularioSesionComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.sesion = {...sesion};
    ref.result.then((sesion: Sesion) => {
      this.sesionEditado(sesion);
    }, (reason) => {});
  }
}
