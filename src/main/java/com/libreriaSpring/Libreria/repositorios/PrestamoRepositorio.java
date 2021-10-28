package com.libreriaSpring.Libreria.repositorios;

import com.libreriaSpring.Libreria.entidades.Prestamo;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, Integer> {

    @Modifying
    @Query("UPDATE Prestamo a SET a.alta = :alta, a.fechaDevolucion = :fechaDevolucion WHERE a.id = :id")
    void baja(@Param("id") Integer id, @Param("alta") Boolean alta, @Param("fechaDevolucion") Date fechaDevolucion);
}
