import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuardarPedidos } from './guardar-pedidos';

describe('GuardarPedidos', () => {
  let component: GuardarPedidos;
  let fixture: ComponentFixture<GuardarPedidos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuardarPedidos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuardarPedidos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
