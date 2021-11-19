package com.libreriaSpring.Libreria.controladores;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Usuario;
import com.libreriaSpring.Libreria.servicios.RolServicio;
import com.libreriaSpring.Libreria.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio us;

    @Autowired
    private RolServicio rs;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView mostrarUsuarios() {
        ModelAndView mav = new ModelAndView("usuarios");
        mav.addObject("usuarios", us.buscarTodos());
        return mav;
    }

    @GetMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView crearUsuario() {
        ModelAndView mav = new ModelAndView("usuario-form");
        mav.addObject("usuario", new Usuario());
        mav.addObject("title", "Crear usuario");
        mav.addObject("usuarios", us.buscarTodos());
        mav.addObject("roles", rs.buscarTodos());
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView guardar(@RequestParam String nombre, @RequestParam String apellido, @RequestParam(defaultValue = "0") Long documento, @RequestParam String telefono, @RequestParam String correo, @RequestParam String clave, @RequestParam String clave2, @RequestParam Integer idRol, @RequestParam MultipartFile imagen, RedirectAttributes a) throws ErrorServicio {
        try {
            us.crear(nombre, apellido, documento, clave2, correo, clave, clave2, idRol, imagen);
            a.addFlashAttribute("exito", "El cliente se guardó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/usuarios/crear");
        }

        return new RedirectView("/usuarios");
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editarUsuario(@PathVariable Integer id) throws Exception {
        ModelAndView mav = new ModelAndView("usuario-form");
        mav.addObject("usuario", us.buscarPorId(id));
        mav.addObject("title", "Editar Usuario");
        mav.addObject("roles", rs.buscarTodos());
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam Integer id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam(defaultValue = "0") Long documento, @RequestParam String telefono, @RequestParam String correo, @RequestParam String clave, @RequestParam String clave2, @RequestParam(defaultValue = "0") Integer idRol, @RequestParam MultipartFile imagen, HttpSession session, RedirectAttributes a) {
        try {
            us.modificar(id, nombre, apellido, documento, telefono, correo, clave, clave2, idRol, imagen);
            a.addFlashAttribute("exito", "El usuario se modificó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
             if (session.getAttribute("rol").equals("ADMIN")) {
            return new RedirectView("/usuarios/editar/" + id); 
        }
       return new RedirectView("/modificar/" + id);
        }
        if (session.getAttribute("rol").equals("ADMIN")) {
            return new RedirectView("/usuarios"); 
        }
       return new RedirectView("/");
    }

    @PostMapping("/baja/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView baja(@PathVariable Integer id) {
        us.baja(id);
        return new RedirectView("/usuarios");
    }

    @PostMapping("/alta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView alta(@PathVariable Integer id) {
        us.baja(id);
        return new RedirectView("/usuarios");
    }
}
