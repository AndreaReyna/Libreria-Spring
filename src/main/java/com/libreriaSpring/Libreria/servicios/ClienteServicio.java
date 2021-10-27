package com.libreriaSpring.Libreria.servicios;

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
    public void crear(Long documento, String nombre, String apellido, String tel) {
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
}
