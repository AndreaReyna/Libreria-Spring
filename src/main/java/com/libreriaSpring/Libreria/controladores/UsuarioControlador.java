package com.libreriaSpring.Libreria.controladores;

import com.libreriaSpring.Libreria.servicios.UsuarioServicio;
import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UsuarioControlador {
    
    @Autowired
    private UsuarioServicio us;
    
    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, Principal principal){
        
        ModelAndView mv = new ModelAndView("login");
        if (error != null){
            mv.addObject("error", "Correo o contraseña incorrecta");
        }
        if (logout != null) {
            mv.addObject("logout", "Salió correctamente de la plataforma");
        }
        if (principal != null) {
            mv.setViewName("redirect:/");
        }
        return mv;
    }
    
    @GetMapping("/signup")
    public ModelAndView signup(HttpServletRequest request, Principal principal){
        ModelAndView mv = new ModelAndView("signup");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        
        if (flashMap != null) {
            mv.addObject("exito", flashMap.get("exito"));
            mv.addObject("error", flashMap.get("error"));
            mv.addObject("nombre", flashMap.get("nombre"));
            mv.addObject("apellido", flashMap.get("apellido"));
            mv.addObject("correo", flashMap.get("correo"));
            mv.addObject("clave", flashMap.get("clave"));
                        
        }
        
        if (principal != null) {
            mv.setViewName("redirect:/");
        }
        return mv;
     }
    
    @PostMapping("/registro")
    public RedirectView signup(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, @RequestParam String clave, RedirectAttributes a){
            RedirectView rw = new RedirectView("/login");
            
            try {
            us.crear(nombre, apellido, correo, clave);
            a.addFlashAttribute("exito", "El registro fue exitoso");
            
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
            a.addFlashAttribute("nombre", nombre);
            a.addFlashAttribute("apellido", apellido);
            a.addFlashAttribute("correo", correo);
            a.addFlashAttribute("clave", clave);
            
            rw.setUrl("/signup");
        }  
            return rw;
    }
}
