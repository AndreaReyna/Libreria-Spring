package com.libreriaSpring.Libreria.servicios;

import java.util.Date;
import java.util.List;
import com.libreriaSpring.Libreria.entidades.Cliente;
import com.libreriaSpring.Libreria.entidades.Libro;
import com.libreriaSpring.Libreria.entidades.Prestamo;
import com.libreriaSpring.Libreria.repositorios.PrestamoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio repositorio;

    @Transactional
    public void CrearPrestamo(Date fechaPrestamo, Libro libro, Cliente cliente) {
        Prestamo p = new Prestamo();
        p.setFechaDevolucion(null);
        p.setFechaPrestamo(fechaPrestamo);
        p.setCliente(cliente);
        p.setLibro(libro);

        repositorio.save(p);
    }

    @Transactional
    public List<Prestamo> obtenerPrestamos() {
        return repositorio.findAll();
    }
}
