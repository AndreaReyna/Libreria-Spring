
package com.libreriaSpring.Libreria.repositorios;

import com.libreriaSpring.Libreria.entidades.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByCorreo(String correo);
    
    boolean existsUsuarioByCorreo(String correo);
    
    @Modifying
    @Query("UPDATE Usuario a SET a.alta = :alta WHERE a.id = :id")
    void baja(@Param("id") Integer id, @Param("alta") Boolean alta);
    
    @Modifying
    @Query("UPDATE Usuario a SET a.nombre = :nombre, a.apellido = :apellido, a.telefono = :telefono, a.correo = :correo WHERE a.id = :id")
    void modificar(@Param("id") Integer id, @Param("nombre") String nombre, @Param("apellido") String apellido, @Param("telefono") String telefono, @Param("correo") String correo);

    @Query("SELECT a FROM Usuario a WHERE a.documento = :documento")
    Usuario buscarDni(@Param("documento") Long documento);
}
