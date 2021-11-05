
package com.libreriaSpring.Libreria.repositorios;

import com.libreriaSpring.Libreria.entidades.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByCorreo(String correo);
    
    boolean existsUsuarioByCorreo(String correo);
}
