package com.libreriaSpring.Libreria.repositorios;

import com.libreriaSpring.Libreria.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {

    @Modifying
    @Query("UPDATE Cliente a SET a.nombre = :nombre, a.apellido = :apellido, a.telefono = :telefono WHERE a.id = :id")
    void modificar(@Param("id") Integer id, @Param("nombre") String nombre, @Param("apellido") String apellido, @Param("telefono") String telefono);

    @Modifying
    @Query("UPDATE Cliente a SET a.alta = :alta WHERE a.id = :id")
    void baja(@Param("id") Integer id, @Param("alta") Boolean alta);

    @Query("SELECT a FROM Cliente a WHERE a.documento = :documento")
    Cliente buscarDni(@Param("documento") Long documento);
}
