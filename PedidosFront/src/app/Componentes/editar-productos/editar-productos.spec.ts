import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarProductos } from './editar-productos';

describe('EditarProductos', () => {
  let component: EditarProductos;
  let fixture: ComponentFixture<EditarProductos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditarProductos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarProductos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
