package com.libreriaSpring.Libreria.servicios;

import Excepciones.ErrorServicio;
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
    public void crearPrestamo(Integer idLibro, Integer idCliente) throws ErrorServicio {
        Prestamo p = new Prestamo();
        p.setFechaDevolucion(null);
        p.setFechaPrestamo(new Date());
        p.setCliente(cr.findById(idCliente).orElse(null));
        p.setLibro(lr.findById(idLibro).orElse(null));
        if (p.getLibro().getEjemplares()>=1){
        lr.prestamo(p.getLibro().getId(), p.getLibro().getEjemplaresPrestados()+1, p.getLibro().getEjemplaresRestantes()-1);
        }else{
            throw new ErrorServicio("No se puede realizar el prestamo");
        }
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
    public void baja(Integer id, Date fechaDevolucion) {
        pr.baja(id, false, fechaDevolucion);
        Prestamo p = buscarPorId(id);
        lr.devolucion(p.getLibro().getId(), p.getLibro().getEjemplaresPrestados()-1, p.getLibro().getEjemplaresRestantes()+1);
    }
}
