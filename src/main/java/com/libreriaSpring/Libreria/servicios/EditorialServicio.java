package com.libreriaSpring.Libreria.servicios;

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
    public void crear(String nombre){
        Editorial ed = new Editorial();
        
   
        ed.setNombre(nombre);
        ed.setAlta(true);
        
        er.save(ed);
    }
    
    @Transactional(readOnly = true)
    public List<Editorial> buscarTodas(){
       return er.findAll();
    }
    
    @Transactional(readOnly = true)
    public Editorial buscarPorId(Integer id) {
        Optional<Editorial> edOptional = er.findById(id);
        return edOptional.orElse(null);
    }

    @Transactional
    public void modificar(Integer id, String nombre) {
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

}
