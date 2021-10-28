package com.libreriaSpring.Libreria.repositorios;

import com.libreriaSpring.Libreria.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, Integer> {

    @Modifying
    @Query("UPDATE Editorial a SET a.nombre = :nombre WHERE a.id = :id")
    void modificar(@Param("id") Integer id, @Param("nombre") String nombre);

    @Modifying
    @Query("UPDATE Editorial a SET a.alta = :alta WHERE a.id = :id")
    void baja(@Param("id") Integer id, @Param("alta") Boolean alta);

    @Query("SELECT a FROM Editorial a WHERE a.nombre = :nombre")
    Editorial buscarNombre(@Param("nombre") String nombre);
}
