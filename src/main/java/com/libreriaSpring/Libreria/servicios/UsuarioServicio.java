package com.libreriaSpring.Libreria.servicios;

import com.libreriaSpring.Libreria.entidades.Usuario;
import com.libreriaSpring.Libreria.repositorios.UsuarioRepositorio;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio ur;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public void crear(String nombre, String apellido, String correo, String clave) throws Exception {
        if (ur.existsUsuarioByCorreo(correo)) {
            throw new Exception("Ya existe un usuario asociado al correo ingresado");
        }

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setCorreo(correo);
        u.setClave(encoder.encode(clave));
        u.setAlta(true);
        ur.save(u);
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = ur.findByCorreo(correo).orElseThrow(() -> new UsernameNotFoundException("No existe un usuario asociado al correo ingresado"));
        return new User(usuario.getCorreo(), usuario.getClave(), Collections.emptyList());
    }
}
