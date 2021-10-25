package com.libreriaSpring.Libreria.servicios;

import java.util.List;
import com.libreriaSpring.Libreria.entidades.Cliente;
import com.libreriaSpring.Libreria.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio repositorio;

    @Transactional
    public void crearCliente(Long dni, String nombre, String apellido, String tel) {
        Cliente c = new Cliente();

        c.setDocumento(dni);
        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setTelefono(tel);

        repositorio.save(c);
    }

    @Transactional
    public List<Cliente> obtenerClientes() {
        return repositorio.findAll();
    }
}
