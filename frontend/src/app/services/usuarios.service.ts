import { Injectable } from "@angular/core";
import { Login, UsuarioSesion, Rol, RolCentro } from "../entities/login";
import { Observable, of, forkJoin, concatMap, lastValueFrom, BehaviorSubject } from "rxjs";
import {map} from 'rxjs/operators';
import * as jose from 'jose';

import { Usuario } from "../entities/usuario";
import { BackendFakeService } from "./backend.fake.service";
import { BackendService } from "./backend.service";
import { EventEmitter } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {
  _rolCentro?: RolCentro;
  id?: number;
  userIdChanged: EventEmitter<number> = new EventEmitter<number>();
  constructor(private backend: BackendService) {}

  doLogin(login: Login): Observable<UsuarioSesion> {
    let jwtObs = this.backend.login(login.email, login.password);
    let usuarioObs = jwtObs.pipe(concatMap(jwt=>this.backend.getUsuario(this.getUsuarioIdFromJwt(jwt))));
    let join = forkJoin({jwt: jwtObs, usuario: usuarioObs});
    let usuarioSesion = join.pipe(map(obj => {
      return {
        id: obj.usuario.id,
        nombre: obj.usuario.nombre,
        apellido1: obj.usuario.apellido1,
        apellido2: obj.usuario.apellido2,
        email: obj.usuario.email,
        roles: obj.usuario.administrador?[{rol: Rol.ADMINISTRADOR}]:[],
        jwt: obj.jwt
      };
    }));
    return usuarioSesion
    .pipe(concatMap(usuarioSesion=>this.completarConRoles(usuarioSesion)))
    .pipe(map(usuarioSesion=>{
      localStorage.setItem('usuario', JSON.stringify(usuarioSesion));
      if (usuarioSesion.roles.length > 0) {
        this.rolCentro = usuarioSesion.roles[0];
      } else {
        this.rolCentro = undefined;
      }
      // Desde que se produce el inicio de sesión buscamos tener actualizado IDUsuario
      this.id = usuarioSesion.id;
      this.userIdChanged.emit(this.id);
      return usuarioSesion;
    }));

  }

  /* 
    A pesar de contar con los 4 roles en todo momento, nos limitamos a crear
    las funcionalidades necesarias para los roles Cliente y Entrenador, que son
    los definidos en los requisitos
    En caso de necesitar implementar determinadas funcionalidades a los otros roles, bastaría
    con declarar las funciones necesarias para estos
  */
  private completarConRoles(usuarioSesion: UsuarioSesion): Observable<UsuarioSesion> {
    // TODO: acceder a lo sotros servicios (o simular) para completar con los roles necesarios
    usuarioSesion.roles = [{rol: Rol.ADMINISTRADOR}, {rol: Rol.CLIENTE}, {rol: Rol.ENTRENADOR}, {rol: Rol.GERENTE}];
    return of(usuarioSesion);
  }

  private getUsuarioIdFromJwt(jwt: string): number {
    let payload = jose.decodeJwt(jwt);
    console.log("Payload: "+JSON.stringify(payload));
    let id = payload.sub;
    if (id == undefined) {
      return 0;
    } else {
      return parseInt(id);
    }
  }

  get rolCentro(): RolCentro | undefined {
    return this._rolCentro;
  }

  set rolCentro(r: RolCentro | undefined) {
    this._rolCentro = r;
  }

  /* 
  Añadimos al método la condición de que si el usuario
  existe, se emita un evento que lleve consigo la ID del usuario,
  de modo que podamos utilizarla en el resto de componentes que utilicen
  este identificador y tengamos en todo momento el ID correcto  
  */
  getUsuarioSesion(): UsuarioSesion | undefined {
    const usuario = localStorage.getItem('usuario');
    if (usuario) {
      const u = JSON.parse(usuario); 
      this.userIdChanged.emit(u.id);
    }    
    return usuario ? JSON.parse(usuario) : undefined;
  }

  doLogout() {
    localStorage.removeItem('usuario');
  }

  doForgottenPassword(email: string): Observable<void> {
    return this.backend.forgottenPassword(email);
  }

  doCambiarContrasenia(password: string, token: string): Promise<void> {
    return lastValueFrom(this.backend.resetPassword(token, password),{defaultValue:undefined});
  }

  getUsuarios(): Observable<Usuario[]> {
    return this.backend.getUsuarios();
  }

  editarUsuario(usuario: Usuario): Observable<Usuario> {
    return this.backend.putUsuario(usuario);
  }

  eliminarUsuario(id: number): Observable<void> {
    return this.backend.deleteUsuario(id);
  }

  aniadirUsuario(usuario: Usuario): Observable<Usuario> {
    return this.backend.postUsuario(usuario);
  }
}
