package com.libreriaSpring.Libreria.repositorios;

import com.libreriaSpring.Libreria.entidades.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Integer> {

    @Modifying
    @Query("UPDATE Libro l SET l.titulo = :titulo, l.anio = :anio WHERE l.id = :id")
    void modificar(@Param("id") Integer id, @Param("titulo") String titulo, @Param("anio") Integer anio);

    @Modifying
    @Query("UPDATE Libro a SET a.alta = :alta WHERE a.id = :id")
    void baja(@Param("id") Integer id, @Param("alta") Boolean alta);

    @Query("SELECT a FROM Libro a WHERE a.titulo = :titulo")
    Libro buscarNombre(@Param("titulo") String titulo);

    @Query("SELECT a FROM Libro a WHERE a.isbn = :isbn")
    Libro buscarIsbn(@Param("isbn") Long isbn);

}
