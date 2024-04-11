import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListadoSesionComponent } from './listado-sesion.component';

describe('ListadoSesionComponent', () => {
  let component: ListadoSesionComponent;
  let fixture: ComponentFixture<ListadoSesionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListadoSesionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListadoSesionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
