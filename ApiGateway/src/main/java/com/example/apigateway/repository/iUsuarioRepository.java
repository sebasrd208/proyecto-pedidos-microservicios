package com.example.apigateway.repository;

import java.util.*;
import com.example.apigateway.entity.*;
import org.springframework.data.jpa.repository.*;

public interface iUsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);
}
