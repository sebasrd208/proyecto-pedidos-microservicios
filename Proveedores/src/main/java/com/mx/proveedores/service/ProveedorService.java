package com.mx.proveedores.service;

import java.util.*;
import org.modelmapper.*;
import java.util.stream.*;
import com.mx.proveedores.dto.*;
import com.mx.proveedores.entity.*;
import com.mx.proveedores.repository.*;
import org.springframework.stereotype.*;
import com.mx.proveedores.excepciones.*;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.*;

@Service
public class ProveedorService {

    @Autowired
    iProveedorRepository repository;

    @Autowired
    ModelMapper mapper;

    public List<ProveedorResponseDTO> listar(){
        return repository.findAll(Sort.by(Sort.Direction.ASC, "idProveedor")).stream().map(proveedor -> mapper.map(proveedor, ProveedorResponseDTO.class)).collect(Collectors.toList());
    }

    public ProveedorResponseDTO guardar(ProveedorRequestDTO dto){
        repository.findByEmail(dto.getEmail()).ifPresent(c -> {
            throw new RuntimeException("El email ya existe, intenta con otro.");
        });

        Proveedor proveedor = mapper.map(dto, Proveedor.class);

        return mapper.map(repository.save(proveedor), ProveedorResponseDTO.class);
    }

    public ProveedorResponseDTO buscar(int idProveedor){
        Proveedor proveedor = repository.findById(idProveedor)
                .orElseThrow(()-> new ProveedorNotFoundException("Proveedor no encontrado con el id " + idProveedor));

        return mapper.map(proveedor, ProveedorResponseDTO.class);
    }

    public ProveedorResponseDTO editar(ProveedorRequestDTO dto) {
        Proveedor proveedor = repository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new ProveedorNotFoundException("Cliente no econtrado con el id " + dto.getIdProveedor()));

        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setDireccion(dto.getDireccion());

        return mapper.map(repository.save(proveedor), ProveedorResponseDTO.class);
    }

    public void eliminar(int idProveedor){
        Proveedor proveedor = repository.findById(idProveedor)
                .orElseThrow(() -> new ProveedorNotFoundException("Cliente no econtrado con el id " + idProveedor));

        repository.delete(proveedor);
    }
}
