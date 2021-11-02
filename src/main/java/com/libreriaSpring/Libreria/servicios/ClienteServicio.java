package com.libreriaSpring.Libreria.servicios;

import Excepciones.ErrorServicio;
import java.util.List;
import com.libreriaSpring.Libreria.entidades.Cliente;
import com.libreriaSpring.Libreria.repositorios.ClienteRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio cr;

    @Transactional
    public void crear(Long documento, String nombre, String apellido, String tel) throws ErrorServicio {

        validar(documento);

        Cliente c = new Cliente();
        c.setDocumento(documento);
        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setTelefono(tel);
        c.setAlta(true);
        cr.save(c);
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarTodos() {
        return cr.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Integer id) {
        Optional<Cliente> clienteOptional = cr.findById(id);
        return clienteOptional.orElse(null);
    }

    @Transactional
    public void modificar(Integer id, String nombre, String apellido, String telefono) {
        cr.modificar(id, nombre, apellido, telefono);
    }

    @Transactional
    public void baja(Integer id) {
        cr.baja(id, false);
    }

    @Transactional
    public void alta(Integer id) {
        cr.baja(id, true);
    }

    public void validar(Long dni) throws ErrorServicio {

        if (dni == 0) {
            throw new ErrorServicio("El DNI no puede estar vacio.");
        }

        if ((cr.buscarDni(dni) != null)) {
            throw new ErrorServicio("El DNI ya se encuentra registrado.");
        }

        if ((dni.toString().length()!=8)) {
            throw new ErrorServicio("El DNI debe tener 8 digitos");
        }
    }
    
    public void validarTel(String tel) throws ErrorServicio {
        if (tel.length()<6) {
            throw new ErrorServicio("El telefono debe tener al menos 6 digitos");
        }
    }   
}
