import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarProveedores } from './listar-proveedores';

describe('ListarProveedores', () => {
  let component: ListarProveedores;
  let fixture: ComponentFixture<ListarProveedores>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarProveedores]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListarProveedores);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
