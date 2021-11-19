package com.libreriaSpring.Libreria.servicios;

import Excepciones.ErrorServicio;
import java.util.List;
import com.libreriaSpring.Libreria.entidades.Editorial;
import com.libreriaSpring.Libreria.repositorios.EditorialRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio er;

    @Transactional
    public void crear(String nombre) throws ErrorServicio {
        validar(nombre);
        Editorial ed = new Editorial();
        ed.setNombre(nombre);
        ed.setAlta(true);

        er.save(ed);
    }

    @Transactional(readOnly = true)
    public List<Editorial> buscarTodas() {
        return er.findAll();
    }

    @Transactional(readOnly = true)
    public Editorial buscarPorId(Integer id) {
        Optional<Editorial> edOptional = er.findById(id);
        return edOptional.orElse(null);
    }

    @Transactional
    public void modificar(Integer id, String nombre) throws ErrorServicio {
        Editorial e = er.findById(id).orElse(null);
        if (!e.getNombre().equals(nombre)) {
         validar(nombre);   
        }     
        er.modificar(id, nombre);
    }

    @Transactional
    public void baja(Integer id) {
        er.baja(id, false);
    }

    @Transactional
    public void alta(Integer id) {
        er.baja(id, true);
    }

    public void validar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre de la editorial no puede estar vacia.");
        }

        if ((er.buscarNombre(nombre) != null)) {
            throw new ErrorServicio("La editorial ya se encuentra registrada.");
        }
    }
}
