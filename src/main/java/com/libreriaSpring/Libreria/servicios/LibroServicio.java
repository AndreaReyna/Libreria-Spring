package com.libreriaSpring.Libreria.servicios;

import Excepciones.ErrorServicio;
import java.util.List;
import com.libreriaSpring.Libreria.entidades.Libro;
import com.libreriaSpring.Libreria.repositorios.AutorRepositorio;
import com.libreriaSpring.Libreria.repositorios.EditorialRepositorio;
import com.libreriaSpring.Libreria.repositorios.LibroRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio lr;

    @Autowired
    private AutorRepositorio ar;

    @Autowired
    private EditorialRepositorio er;

    @Transactional
    public void crear(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer idAutor, Integer idEditorial) throws ErrorServicio {
        validarTitulo(titulo);
        validarIsbn(isbn);
        
        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(0);
        libro.setEjemplaresRestantes(ejemplares);
        libro.setAnio(anio);
        libro.setAlta(true);
        libro.setAutor(ar.findById(idAutor).orElse(null));
        libro.setEditorial(er.findById(idEditorial).orElse(null));

        lr.save(libro);
    }

    @Transactional(readOnly = true)
    public List<Libro> buscarTodos() {
        return lr.findAll();
    }

    @Transactional(readOnly = true)
    public Libro buscarPorId(Integer id) {
        Optional<Libro> libroOptional = lr.findById(id);
        return libroOptional.orElse(null);
    }

    @Transactional
    public void modificar(Integer id, String titulo, Integer anio) throws ErrorServicio {
        validarTitulo(titulo);
        lr.modificar(id, titulo, anio);
    }

    @Transactional
    public void baja(Integer id) {
        lr.baja(id, false);
    }

    @Transactional
    public void alta(Integer id) {
        lr.baja(id, true);
    }

    public void validarTitulo(String titulo) throws ErrorServicio {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ErrorServicio("El titulo del libro no puede estar vacio.");
        }

        if ((lr.buscarNombre(titulo) != null)) {
            throw new ErrorServicio("El libro ya se encuentra registrado con ese titulo.");
        }

    }

    public void validarIsbn(Long isbn) throws ErrorServicio {
        if (isbn == 0) {
            throw new ErrorServicio("El ISBN del libro no puede estar vacio.");
        }

        if ((lr.buscarIsbn(isbn) != null)) {
            throw new ErrorServicio("El libro ya se encuentra registrado con ese ISBN.");
        }
    }
}
