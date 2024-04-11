import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { BackendFakeService } from "./backend.fake.service";
import { Sesion } from "../entities/sesion";

@Injectable({
  providedIn: 'root'
})
export class SesionesService {
  _id?: number;
  constructor(private backend: BackendFakeService) {}


  get idSesion(): number | undefined {
    return this._id;
  }

  set idSesion(r: number | undefined) {
    this._id = r;
  }

  getSesiones(): Observable<Sesion[]> {
    return this.backend.getSesiones();
  }

  aniadirSesion(sesion: Sesion): Observable<Sesion> {
    return this.backend.postSesion(sesion);
  }

  editarSesion(sesion: Sesion): Observable<Sesion> {
    return this.backend.putSesion(sesion);
  }

  eliminarSesion(id: number): Observable<void> {
    return this.backend.deleteSesion(id);
  }
}
