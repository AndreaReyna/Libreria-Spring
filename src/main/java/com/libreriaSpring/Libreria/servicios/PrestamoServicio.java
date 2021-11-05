package com.libreriaSpring.Libreria.servicios;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Libro;
import java.util.Date;
import java.util.List;
import com.libreriaSpring.Libreria.entidades.Prestamo;
import com.libreriaSpring.Libreria.repositorios.ClienteRepositorio;
import com.libreriaSpring.Libreria.repositorios.LibroRepositorio;
import com.libreriaSpring.Libreria.repositorios.PrestamoRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio pr;

    @Autowired
    private ClienteRepositorio cr;

    @Autowired
    private LibroRepositorio lr;

    @Transactional
    public void crearPrestamo(Date fecha, Integer idLibro, Integer idCliente) throws ErrorServicio {

        Libro l = lr.findById(idLibro).orElse(null);

        if (l.getEjemplaresRestantes() < 1) {
            throw new ErrorServicio("No quedan ejemplares para prestar");
        }

        l.setEjemplaresPrestados(l.getEjemplaresPrestados() + 1);
        l.setEjemplaresRestantes(l.getEjemplaresRestantes() - 1);
        if (l.getEjemplaresRestantes() < 1) {
            l.setAlta(false);
        }
        lr.save(l);

        Prestamo p = new Prestamo();
        p.setFechaDevolucion(null);
        p.setFechaPrestamo(fecha);
        p.setCliente(cr.findById(idCliente).orElse(null));
        p.setLibro(l);
        p.setAlta(true);

        pr.save(p);

    }

    @Transactional(readOnly = true)
    public List<Prestamo> buscarTodos() {
        return pr.findAll();
    }

    @Transactional(readOnly = true)
    public Prestamo buscarPorId(Integer id) {
        Optional<Prestamo> prestamoOptional = pr.findById(id);
        return prestamoOptional.orElse(null);
    }

    @Transactional
    public void baja(Integer id, Date fechaDevolucion) throws ErrorServicio {
        Prestamo p = buscarPorId(id);

        if (fechaDevolucion.before(p.getFechaPrestamo())) {
            throw new ErrorServicio("La fecha de devolucion no puede ser anterior a la del prestamo");
        }
        Date d = new Date();
        if (fechaDevolucion.after(d)) {
            throw new ErrorServicio("La fecha de devolucion no puede ser posterior al dia de hoy.");
        }
        
        pr.baja(id, false, fechaDevolucion);

        Libro l = p.getLibro();
        l.setEjemplaresPrestados(l.getEjemplaresPrestados() - 1);
        l.setEjemplaresRestantes(l.getEjemplaresRestantes() + 1);
        if (l.getEjemplaresRestantes() > 0) {
            l.setAlta(true);
        }
        lr.save(l);
    }
}
