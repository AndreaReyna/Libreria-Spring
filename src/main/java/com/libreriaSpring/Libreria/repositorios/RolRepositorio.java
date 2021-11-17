/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreriaSpring.Libreria.repositorios;

import com.libreriaSpring.Libreria.entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, Integer> {   
    
    @Query("SELECT a FROM Rol a WHERE a.nombre = :nombre")
    Rol buscarRol(@Param("nombre") String nombre);  
}
