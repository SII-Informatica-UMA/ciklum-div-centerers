import { Injectable } from "@angular/core";
import { concat, filter, Observable, of } from "rxjs";
import { Usuario } from "../entities/usuario";
import { SECRET_JWT } from "../config/config";
import { from } from "rxjs";
import * as jose from 'jose';
import { FRONTEND_URI } from "../config/config";
import { Sesion } from "../entities/sesion";
import { AsignacionEntrenamiento } from "../entities/asignacionentrenamientos";

// Este servicio imita al backend pero utiliza localStorage para almacenar los datos

const usuariosC: Usuario [] = [
  {
    id: 1,
    nombre: 'Admin',
    apellido1: 'Admin',
    apellido2: 'Admin',
    email: 'admin@uma.es',
    administrador: true,
    password: 'admin'
  },
  {
    id: 2,
    nombre: 'Antonio',
    apellido1: 'García',
    apellido2: 'Ramos',
    email: 'antonio@uma.es',
    administrador: false,
    password: '5678'
  },
  {
    id: 3,
    nombre: 'Rubén',
    apellido1: 'Díaz',
    apellido2: 'Ruiz',
    email: 'ruben@uma.es',
    administrador: false,
    password: 'ruben'
  }, 
  {
    id: 4,
    nombre: 'Ana',
    apellido1: 'López',
    apellido2: 'López',
    email: 'ana@uma.es',
    administrador: false,
    password: 'ana'
  },
  {
    id: 5,
    nombre: 'Carlos',
    apellido1: 'Martín',
    apellido2: 'Serrano',
    email: 'carlos@uma.es',
    administrador: false,
    password: 'carlos'
  },
];

const sesionesC: Sesion [] = [

  {
    idPlan: 1,
    inicio: new Date("2020-11-12"),
    fin: new Date("2020-11-13"),
    trabajoRealizado: "Pierna",
    multimedia: [],
    descripcion: "Primera sesión",
    presencial: true,
    datosSalud: [],
    id: 1,
    idUsuario:3
  },
  {
  idPlan: 2,
  inicio: new Date("2020-11-12"),
  fin: new Date("2020-11-13"),
  trabajoRealizado: "Pierna",
  multimedia: [],
  descripcion: "Primera sesión",
  presencial: true,
  datosSalud: [],
  id: 2,
  idUsuario:3
},

  {
    idPlan: 2,
    inicio: new Date("2020-12-05"),
    fin: new Date("2020-12-05"),
    trabajoRealizado: "Espalda",
    multimedia: [],
    descripcion: "Segunda sesión",
    presencial: false,
    datosSalud: [],
    id: 2,
    idUsuario:4
  },

  {
    idPlan: 2,
    inicio: new Date("2020-12-12"),
    fin: new Date("2020-12-13"),
    trabajoRealizado: "Pecho",
    multimedia: [],
    descripcion: "Tercera sesión",
    presencial: true,
    datosSalud: [],
    id: 3,
    idUsuario:5
  },

];

const asignacionesC: AsignacionEntrenamiento [] = [
  {
    id: 1,
    idEntrenador: 1,
    idCliente: 3,
    especialidad: '',
    plan: 1
  },
  {
    id: 2,
    idEntrenador: 1,
    idCliente: 5,
    especialidad: '',
    plan: 2
  },
  {
    id: 3,
    idEntrenador: 1,
    idCliente: 5,
    especialidad: '',
    plan: 1
  },
  {
    id: 4,
    idEntrenador: 1,
    idCliente: 4,
    especialidad: '',
    plan: 1
  },
];


@Injectable({
  providedIn: 'root'
})
export class BackendFakeService {
  private usuarios: Usuario [];
  private sesiones: Sesion [];
  private asignaciones: AsignacionEntrenamiento[];
  private forgottenPasswordTokens;

  // Obtenemos del localStorage los usuarios, sesiones y asignaciones que tengamos
  constructor() {
    let _usuarios = localStorage.getItem('usuarios');
    let _sesiones = localStorage.getItem('sesiones');
    let _asignaciones = localStorage.getItem('asignaciones');
    if (_usuarios) {
      this.usuarios = JSON.parse(_usuarios);
    } else {
      this.usuarios = [...usuariosC];
    }

    if (_sesiones) {
      this.sesiones = JSON.parse(_sesiones);
    } else {
      this.sesiones = [...sesionesC];
    }

    if (_asignaciones) {
      this.asignaciones = JSON.parse(_asignaciones);
    } else {
      this.asignaciones = [...asignacionesC];
    }

    let _forgottenPasswordTokens = localStorage.getItem('forgottenPasswordTokens');
    if (_forgottenPasswordTokens) {
      this.forgottenPasswordTokens = new Map(JSON.parse(_forgottenPasswordTokens));
    } else {
      this.forgottenPasswordTokens = new Map();
    }
  }

  getUsuarios(): Observable<Usuario[]> {
    return of(this.usuarios);
  }

  // Obtiene la lista de sesiones filtrada en función de las asignaciones existentes (explicado en filtrarSesiones)
  getSesiones(): Observable<Sesion[]> {
    return of(this.filtrarSesiones(this.sesiones, this.asignaciones));
  }

  /*
    Tomamos inicialmente todas las sesiones del backend, y en base
    a los planes de entrenamiento definidos en alguna asignación, generamos
    una nueva lista de sesiones en la que solo se encuentren las sesiones con
    un plan guardado en asignaciones.

    Por ejemplo, si tenemos una sesión con idPlan=5, pero ninguna asignación apunta
    a este idPlan, esta sesión no se mostrará al usuario.
  */
  filtrarSesiones(ses: Sesion[], asig: AsignacionEntrenamiento[]): Sesion[] {
    const idValido = asig.map(asignacion => asignacion.plan);
    //console.log("Sesiones inicialmente:");
    //console.log(ses);
    const filtradoPorPlan  = ses.filter(sesion => idValido.includes(sesion.idPlan));
    //console.log("Sesiones tras filtro por plan:");
    //console.log(filtradoPorPlan);

    const clienteValido = asig.map(asignacion => asignacion.idCliente);
    const entrenadorValido = asig.map(asignacion => asignacion.idEntrenador);

    //const usuarioValido = clienteValido.concat(entrenadorValido);
    //console.log("IDs que pueden ver alguna sesión de las existentes:");
    //console.log(usuarioValido);
    
    return filtradoPorPlan.filter(sesion => idValido.includes(sesion.idPlan));
  }

  /* 
    Este método toma la lista filtrada y vuelve a filtrarla, para que solo aparezcan
    las sesiones que hayan sido creadas por el cliente (sin esta función, si varios clientes
    estaban relacionados con un mismo plan, podían ver las sesiones de otras personas)
  */
  filtrarSesionesPorIdCliente(id?: number): Sesion[] {
    //console.log('PLANES ASIGNADOS A ESTE ID' + this.obtenerPlanesPorId(true, id));
    const ses = this.sesiones;
    const asig = this.asignaciones;
    const listaFiltrada = this.filtrarSesiones (ses, asig);
    const idsPermitidos = asig.filter(asignacion => asignacion.idCliente === id).map(asignacion => asignacion.plan);
    return listaFiltrada.filter(sesion => idsPermitidos.includes(sesion.idPlan)).filter(sesion => id === sesion.idUsuario);   //Filtra por plan, después por usuario (así solo aparecen sesiones propias)
  }

  /*
    Método similar al anterior, pero sin el detalle de filtrar por idCliente, ya que como
    entrenador sí nos interesa poder ver todas las sesiones creadas por los clientes a los 
    que estamos entrenando.
  */
  filtrarSesionesPorIdEntrenador(id?: number): Sesion[] {
    //console.log('PLANES ASIGNADOS A ESTE ID' + this.obtenerPlanesPorId(false, id));
    const ses = this.sesiones;
    const asig = this.asignaciones;
    const listaFiltrada = this.filtrarSesiones (ses, asig);
    const idsPermitidos = asig.filter(asignacion => asignacion.idEntrenador === id).map(asignacion => asignacion.plan);
    return listaFiltrada.filter(sesion => idsPermitidos.includes(sesion.idPlan));//.filter()
  }

  getAsignaciones(): Observable<AsignacionEntrenamiento[]> {
    return of(this.asignaciones);
  }

  postUsuario(usuario: Usuario): Observable<Usuario> {
    let u = this.usuarios.find(u => u.email == usuario.email);
    if (!usuario.email) {
      return new Observable<Usuario>(observer => {
        observer.error('El email es obligatorio');
      });
    }
    if (u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario ya existe');
      });
    }
    // Si no trae contraseña generamos una aleatoria
    if (usuario.password.length == 0) {
      usuario.password = this.generarCadena();
    }

    usuario.id = this.usuarios.map(u => u.id).reduce((a, b) => Math.max(a, b)) + 1;
    this.usuarios.push(usuario);
    this.guardarUsuariosEnLocalStorage();
    return of(usuario);
  }

  private guardarUsuariosEnLocalStorage() {
    localStorage.setItem('usuarios', JSON.stringify(this.usuarios));
  }

  private guardarForgottenPasswordTokensEnLocalStorage() {
    localStorage.setItem('forgottenPasswordTokens', JSON.stringify(Array.from(this.forgottenPasswordTokens.entries())));
  }

  putUsuario(usuario: Usuario): Observable<Usuario> {
    let u = this.usuarios.find(u => u.id == usuario.id);
    if (!u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario no existe');
      });
    }
    // Si la contraseña está en blanco mantenemos la que ya tiene
    if (usuario.password.length == 0) {
      usuario.password = u.password;
    }

    Object.assign(u, usuario);
    this.guardarUsuariosEnLocalStorage();
    return of(u);
  }

  deleteUsuario(id: number): Observable<void> {
    let i = this.usuarios.findIndex(u => u.id == id);
    if (i < 0) {
      return new Observable<void>(observer => {
        observer.error('El usuario no existe');
      });
    }
    this.usuarios.splice(i, 1);
    this.guardarUsuariosEnLocalStorage();
    return of();
  }

  getUsuario(id: number): Observable<Usuario> {
    let u = this.usuarios.find(u => u.id == id);
    if (!u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario no existe');
      });
    }
    return of(u);
  }

  login(email: string, password: string): Observable<string> {
    let u = this.usuarios.find(u => u.email == email && u.password == password);
    if (!u) {
      return new Observable<string>(observer => {
        observer.error({status: 401, statusText: 'Usuario o contraseña incorrectos'});
      });
    }
    return from(this.generateJwt(u));
  }

  forgottenPassword(email: string): Observable<void> {
    const token = this.generarCadena()
    console.log('Para resetar la contraseña acceda a: '+FRONTEND_URI+'/reset-password?token='+token);
    this.forgottenPasswordTokens.set(token, email);
    this.guardarForgottenPasswordTokensEnLocalStorage();
    return of();
  }

  resetPassword(token: string, password: string): Observable<void> {
    if (!this.forgottenPasswordTokens.get(token)) {
      return new Observable<void>(observer => {
        observer.error('Token incorrecto');
      });
    }
    let email = this.forgottenPasswordTokens.get(token);
    console.log("Email for token: ", email)
    let u = this.usuarios.find(u => u.email == email);
    if (!u) {
      return new Observable<void>(observer => {
        observer.error('Usuario no existe');
      });
    }
    u.password = password;
    this.forgottenPasswordTokens.delete(token);

    this.guardarUsuariosEnLocalStorage();
    this.guardarForgottenPasswordTokensEnLocalStorage();
    return of();
  }

  private generateJwt(usuario: Usuario): Promise<string> {
    const secret = new TextEncoder().encode(SECRET_JWT);
    return new jose.SignJWT({ sub: ""+usuario.id, email: usuario.email })
      .setProtectedHeader({ alg: 'HS256' })
      .sign(secret);
  }

  private generarCadena(): string {
    return Math.random().toString(36).substring(2);
  }

  postSesion(sesion: Sesion): Observable<Sesion> {
    sesion.id = this.sesiones.map(u => u.id).reduce((a, b) => Math.max(a, b)) + 1;
    let u = this.sesiones.find(u => u.id == sesion.id);
    if (!sesion.id) {
      return new Observable<Sesion>(observer => {
        observer.error('Error. No hay ID sesión');
      });
    }
    if (u) {
      return new Observable<Sesion>(observer => {
        observer.error('La sesión ya existe');
      });
    }     
    this.sesiones.push(sesion);
    this.guardarSesionesEnLocalStorage();
    return of(sesion);
  }

  private guardarSesionesEnLocalStorage() {
    localStorage.setItem('sesiones', JSON.stringify(this.sesiones));
  }

  putSesion(sesion: Sesion): Observable<Sesion> {
    let u = this.sesiones.find(u => u.id == sesion.id);
    if (!u) {
      return new Observable<Sesion>(observer => {
        observer.error('La sesión no existe');
      });
    }
    
    Object.assign(u, sesion);
    this.guardarSesionesEnLocalStorage();
    return of(u);
  }

  deleteSesion(id: number): Observable<void> {
    let i = this.sesiones.findIndex(u => u.id == id);
    if (i < 0) {
      return new Observable<void>(observer => {
        observer.error('La sesion no existe');
      });
    }
    this.sesiones.splice(i, 1);
    this.guardarSesionesEnLocalStorage();
    return of();
  }

  postAsignacion(asignacion: AsignacionEntrenamiento): Observable<AsignacionEntrenamiento> {
    asignacion.id = this.asignaciones.map(u => u.id).reduce((a, b) => Math.max(a, b)) + 1;
    let u = this.asignaciones.find(u => u.id == asignacion.id);
    if (!asignacion.id) {
      return new Observable<AsignacionEntrenamiento>(observer => {
        observer.error('Error ID asignación');
      });
    }
    if (u) {
      return new Observable<AsignacionEntrenamiento>(observer => {
        observer.error('La asignación ya existe');
      });
    }
    this.asignaciones.push(asignacion);
    this.guardarAsignacionesEnLocalStorage();
    return of(asignacion);
  }

  private guardarAsignacionesEnLocalStorage() {
    localStorage.setItem('asignaciones', JSON.stringify(this.asignaciones));
  }

  putAsignacion(asignacion: AsignacionEntrenamiento): Observable<AsignacionEntrenamiento> {
    let u = this.asignaciones.find(u => u.id == asignacion.id);
    if (!u) {
      return new Observable<AsignacionEntrenamiento>(observer => {
        observer.error('La asignación no existe');
      });
    }
    
    Object.assign(u, asignacion);
    this.guardarAsignacionesEnLocalStorage();
    return of(u);
  }

  deleteAsignacion(id: number): Observable<void> {
    let i = this.asignaciones.findIndex(u => u.id == id);
    if (i < 0) {
      return new Observable<void>(observer => {
        observer.error('La asignación no existe');
      });
    }
    this.asignaciones.splice(i, 1);
    this.guardarAsignacionesEnLocalStorage();
    return of();
  }
  
  /*
    Función que devuelve un array con los idPlan a los que está asociado un usuario.
    
    Se realiza una distinción entre Cliente y Entrenador ya que cada asignación almacena ambos identificadores,
    y según el rol del usuario nos será conveniente tomar uno u otro.
  */
  obtenerPlanesPorId(cliente: boolean, idCliente?: number): number[] {
    if (cliente){
      const asigId = this.asignaciones.filter(asignacion => asignacion.idCliente === idCliente);
      console.log('AQUI VIENE FILTRO');
      const idsPlanes = asigId.map(asignacion => asignacion.plan);
      console.log(idsPlanes);
      return idsPlanes;
    } 
    
    else {
      const asigId = this.asignaciones.filter(asignacion => asignacion.idEntrenador === idCliente);
      const idsPlanes = asigId.map(asignacion => asignacion.plan);
      console.log(asigId);
      return idsPlanes;
    }
  }
}
