import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuardarProveedores } from './guardar-proveedores';

describe('GuardarProveedores', () => {
  let component: GuardarProveedores;
  let fixture: ComponentFixture<GuardarProveedores>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuardarProveedores]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuardarProveedores);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
