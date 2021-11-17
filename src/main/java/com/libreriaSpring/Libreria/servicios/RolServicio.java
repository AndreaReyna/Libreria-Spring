/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreriaSpring.Libreria.servicios;

import com.libreriaSpring.Libreria.entidades.Editorial;
import com.libreriaSpring.Libreria.entidades.Rol;
import com.libreriaSpring.Libreria.repositorios.RolRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolServicio {

    @Autowired
    private RolRepositorio rr;

    @Transactional(readOnly = true)
    public List<Rol> buscarTodos() {
        return rr.findAll();
    }
}
