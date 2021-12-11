package com.libreriaSpring.Libreria.servicios;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Rol;
import com.libreriaSpring.Libreria.entidades.Usuario;
import com.libreriaSpring.Libreria.repositorios.RolRepositorio;
import com.libreriaSpring.Libreria.repositorios.UsuarioRepositorio;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private ImagenServicio is;

    @Transactional
    public void crear(String nombre, String apellido, Long dni, String tel, String correo, String clave, String clave2, Integer idRol, MultipartFile imagen) throws Exception {

        validarCorreo(correo);
        validarDNI(dni);
        validarClave(clave, clave2);
        validarNombreTel(nombre, apellido, tel);

        Usuario u = new Usuario();
        u.setDocumento(dni);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setTelefono(tel);
        u.setCorreo(correo);
        u.setClave(encoder.encode(clave));
        if (ur.findAll().isEmpty()) {
            Rol r1 = new Rol("ADMIN");
            Rol r2 = new Rol("CLIENTE");
            rr.save(r1);
            rr.save(r2);
            u.setRol(r1);
        }else{
            u.setRol(rr.buscarRol("CLIENTE"));
        }
        if (!imagen.isEmpty()) {
            u.setImagen(is.copiar(imagen));
        }
        u.setAlta(true);
        ur.save(u);
        es.enviarThread(correo);
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = ur.findByCorreo(correo).orElseThrow(() -> new UsernameNotFoundException("No existe un usuario asociado al correo ingresado"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);

        session.setAttribute("id", usuario.getId());
        session.setAttribute("nombre", usuario.getNombre());
        session.setAttribute("apellido", usuario.getApellido());
        session.setAttribute("correo", usuario.getCorreo());
        session.setAttribute("imagen", usuario.getImagen());
        session.setAttribute("rol", usuario.getRol().getNombre());

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
    public void modificar(Integer id, String nombre, String apellido, Long dni, String tel, String correo, Integer idRol, MultipartFile imagen) throws Exception {
        
        Usuario u = buscarPorId(id);
 
        if (!u.getCorreo().equals(correo)) {
            validarCorreo(correo);
            u.setCorreo(correo);
        }

        if (apellido != null && !apellido.equals(u.getApellido())) {
            u.setApellido(apellido);
        }
        if (nombre != null && !nombre.equals(u.getNombre())) {
            u.setNombre(nombre);
        }
        if (tel != null && !tel.equals(u.getTelefono())) {
            validarNombreTel("aaa", "aaa", tel);
            u.setTelefono(tel);
        }       
       
        if (idRol != 0 && idRol != u.getRol().getId()) {
         u.setRol(rr.findById(idRol).orElse(null));           
        }

        if (!imagen.isEmpty()) {
            is.borrarImagen(u.getImagen());
            u.setImagen(is.copiar(imagen));   
        }
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
    
    public void clave (Integer id, String clave, String clave2) throws ErrorServicio{
      Usuario u = buscarPorId(id);
        
      validarClave(clave, clave2);
      u.setClave(encoder.encode(clave));    
     
      ur.save(u);
   
    }

    public void validarNombreTel(String nombre, String apellido, String tel) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ErrorServicio("El apellido no puede estar vacio.");
        }
        if (tel.length() < 6) {
            throw new ErrorServicio("El telefono debe tener al menos 6 digitos");
        }
    }

    public void validarCorreo(String correo) throws ErrorServicio {
        if (correo == null || correo.trim().isEmpty()) {
        throw new ErrorServicio("El correo no puede estar vacio.");
        }
        if (ur.existsUsuarioByCorreo(correo)) {
        throw new ErrorServicio("Ya existe un usuario asociado al correo ingresado");
        }
        if (!(correo.contains("@") && correo.contains(".com"))) {
         throw new ErrorServicio("Debe ingresar un formato de correo valido.");
        }
    }

    public void validarClave(String clave, String clave2) throws ErrorServicio {
        if (clave.length() < 8) {
            throw new ErrorServicio("La contraseña debe tener al menos 8 caracteres");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las contraseñas no coinciden");
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
