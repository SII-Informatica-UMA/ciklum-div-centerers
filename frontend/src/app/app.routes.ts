import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ForgottenPasswordComponent } from './forgotten-password/forgotten-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { ListadoUsuarioComponent } from './listado-usuario/listado-usuario.component';
import { PrincipalComponent } from './principal/principal.component';
import { ListadoSesionComponent } from './listado-sesion/listado-sesion.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'forgotten-password',
    component: ForgottenPasswordComponent
  },
  {
    path: 'reset-password',
    component: ResetPasswordComponent
  },
  {
    path: 'usuarios',
    component: ListadoUsuarioComponent
  },
  // Definimos el path necesario para nuestro microservicio en concreto
  {
    path: 'sesion',
    component: ListadoSesionComponent
  },
  {
    path: '',
    component: PrincipalComponent
  }
];
