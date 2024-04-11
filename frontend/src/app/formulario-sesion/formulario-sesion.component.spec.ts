import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioSesionComponent } from './formulario-sesion.component';

describe('FormularioSesionComponent', () => {
  let component: FormularioSesionComponent;
  let fixture: ComponentFixture<FormularioSesionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormularioSesionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormularioSesionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
