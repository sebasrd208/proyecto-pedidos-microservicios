import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarProductos } from './listar-productos';

describe('ListarProductos', () => {
  let component: ListarProductos;
  let fixture: ComponentFixture<ListarProductos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarProductos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListarProductos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
