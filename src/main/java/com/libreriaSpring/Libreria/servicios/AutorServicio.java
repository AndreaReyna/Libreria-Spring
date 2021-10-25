package com.libreriaSpring.Libreria.servicios;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Autor;
import com.libreriaSpring.Libreria.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio AutorRepositorio;

    @Transactional
    public void crear(String nombre) throws ErrorServicio {
        //validar(nombre);

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(true);

        AutorRepositorio.save(autor);
    }

    @Transactional(readOnly = true)
    public List<Autor> buscarTodos() {
        return AutorRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Autor buscarPorId(Integer id) {
        Optional<Autor> autorOptional = AutorRepositorio.findById(id);
        return autorOptional.orElse(null);
    }

    @Transactional
    public void modificar(Integer id, String nombre) {
        AutorRepositorio.modificar(id, nombre);
    }

    @Transactional
    public void baja(Integer id) {
        AutorRepositorio.baja(id, false);
    }

    public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del autor no puede ser nulo.");
        }
//        List<Autor> autores = AutorRepositorio.findAll();
//        for (Autor a : autores) {
//            if (a.getNombre() == nombre) {
//                throw new ErrorServicio("El autor ya se encuentra registrado.");
//            }
//        }


    }
}
