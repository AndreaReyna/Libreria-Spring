package com.libreriaSpring.Libreria.servicios;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Usuario;
import com.libreriaSpring.Libreria.repositorios.RolRepositorio;
import com.libreriaSpring.Libreria.repositorios.UsuarioRepositorio;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private RolRepositorio rr;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private EmailServicio es;

    @Transactional
    public void crear(String nombre, String apellido, Long dni, String tel, String correo, String clave, String clave2, Integer idRol) throws Exception {

        validarCorreo(correo);
        validarDNI(dni);
        validaciones(nombre, apellido, tel, clave, clave2);

        Usuario u = new Usuario();
        u.setDocumento(dni);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setTelefono(tel);
        u.setCorreo(correo);
        u.setClave(encoder.encode(clave));
        u.setRol(rr.getById(idRol));

        u.setAlta(true);
        ur.save(u);
        es.enviarThread(correo);
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = ur.findByCorreo(correo).orElseThrow(() -> new UsernameNotFoundException("No existe un usuario asociado al correo ingresado"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());

        return new User(usuario.getCorreo(), usuario.getClave(), Collections.singletonList(authority));
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return ur.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Integer id) {
        Optional<Usuario> uOptional = ur.findById(id);
        return uOptional.orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorCorreo(String correo) {
        Optional<Usuario> uOptional = ur.findByCorreo(correo);
        return uOptional.orElse(null);
    }

    @Transactional
    public void modificar(Integer id, String nombre, String apellido, Long dni, String tel, String correo, String clave, String clave2, Integer idRol) throws Exception {

        Usuario u = buscarPorId(id);
        if (!u.getCorreo().equals(correo)) {
            validarCorreo(correo);
            u.setCorreo(correo);
        }

        validaciones(nombre, apellido, tel, clave, clave2);
        
        u.setApellido(apellido);
        u.setNombre(nombre);
        u.setTelefono(tel);
        u.setClave(encoder.encode(clave));
        u.setRol(rr.findById(idRol).orElse(null));
        u.setAlta(true);
        ur.save(u);
    }

    @Transactional
    public void baja(Integer id) {
        ur.baja(id, false);
    }

    @Transactional
    public void alta(Integer id) {
        ur.baja(id, true);
    }

    public void validaciones(String nombre, String apellido, String tel, String clave, String clave2) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ErrorServicio("El apellido no puede estar vacio.");
        }
        if (clave.length() < 8) {
            throw new ErrorServicio("La contraseña debe tener al menos 8 caracteres");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las contraseñas no coinciden");
        }

        if (tel.length() < 6) {
            throw new ErrorServicio("El telefono debe tener al menos 6 digitos");
        }

    }

    public void validarCorreo(String correo) throws ErrorServicio {
        if (ur.existsUsuarioByCorreo(correo)) {
            throw new ErrorServicio("Ya existe un usuario asociado al correo ingresado");
        }
    }

    public void validarDNI(Long dni) throws ErrorServicio {
        if ((ur.buscarDni(dni) != null)) {
            throw new ErrorServicio("El DNI ya se encuentra registrado.");
        }
        if (dni == 0) {
            throw new ErrorServicio("El DNI no puede estar vacio.");
        }

        if ((dni.toString().length() != 8)) {
            throw new ErrorServicio("El DNI debe tener 8 digitos");
        }
    }
}
