package com.libreriaSpring.Libreria.controladores;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Usuario;
import com.libreriaSpring.Libreria.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    private UsuarioServicio us;
    @GetMapping("/crear")
    public ModelAndView crearAdmin() {
        ModelAndView mav = new ModelAndView("admin");
        mav.addObject("Usuario", new Usuario());
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre, @RequestParam String apellido, @RequestParam(defaultValue = "0") Long documento, @RequestParam String telefono, @RequestParam String correo, @RequestParam String clave, @RequestParam String clave2, RedirectAttributes a) throws ErrorServicio, Exception {
        RedirectView rw = new RedirectView("/admin");
        try {
            us.crear(nombre, apellido, documento, telefono, correo, clave, clave2, 1);
            a.addFlashAttribute("exito", "El admin se guardó correctamente!");
        } catch (ErrorServicio e) {         
            a.addFlashAttribute("error", e.getMessage()); 
            rw.setUrl("/admin/crear");
        }
        return rw;
    }
}
