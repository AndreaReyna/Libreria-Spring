package com.libreriaSpring.Libreria.controladores;

import com.libreriaSpring.Libreria.servicios.RolServicio;
import com.libreriaSpring.Libreria.servicios.UsuarioServicio;
import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginControlador {

    @Autowired
    private UsuarioServicio us;

    @Autowired
    private RolServicio rs;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, Principal principal) {

        ModelAndView mv = new ModelAndView("login");
        if (error != null) {
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
    public ModelAndView signup(HttpServletRequest request, Principal principal) {
        ModelAndView mv = new ModelAndView("signup");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mv.addObject("exito", flashMap.get("exito"));
            mv.addObject("error", flashMap.get("error"));
            mv.addObject("nombre", flashMap.get("nombre"));
            mv.addObject("apellido", flashMap.get("apellido"));
            mv.addObject("documento", flashMap.get("documento"));
            mv.addObject("telefono", flashMap.get("telefono"));
            mv.addObject("correo", flashMap.get("correo"));
            mv.addObject("clave", flashMap.get("clave"));
        }

        if (principal != null) {
            mv.setViewName("redirect:/");
        }
        return mv;
    }

    @PostMapping("/registro")
    public RedirectView signup(@RequestParam String nombre, @RequestParam String apellido, @RequestParam(defaultValue = "0") Long documento, @RequestParam String telefono, @RequestParam String correo, @RequestParam String clave, @RequestParam String clave2, @RequestParam MultipartFile imagen, HttpServletRequest request, RedirectAttributes a) {
        RedirectView rw = new RedirectView("/login");

        try {
            us.crear(nombre, apellido, documento, telefono, correo, clave, clave2, 2, imagen);

            a.addFlashAttribute("exito", "El registro fue exitoso");
            request.login(correo, clave);
            
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
            a.addFlashAttribute("nombre", nombre);
            a.addFlashAttribute("apellido", apellido);
            a.addFlashAttribute("documento", documento);
            a.addFlashAttribute("telefono", telefono);
            a.addFlashAttribute("correo", correo);

            rw.setUrl("/signup");
        }
        return rw;
    }
    
        @GetMapping("/modificar/{id}")
        public ModelAndView editarUsuario(@PathVariable Integer id, HttpSession session) throws Exception {
        if (!session.getAttribute("id").equals(id)) {
            return new ModelAndView(new RedirectView("/"));
        }
        ModelAndView mav = new ModelAndView("usuario-form");
        mav.addObject("usuario", us.buscarPorId(id));
        mav.addObject("title", "Editar Usuario");
        mav.addObject("roles", rs.buscarTodos());
        mav.addObject("action", "modificar");
        return mav;
    }
     
    @GetMapping("/clave/{id}")
    public ModelAndView editarClave(@PathVariable Integer id, HttpSession session) throws Exception {
        if (!session.getAttribute("id").equals(id)) {
            return new ModelAndView(new RedirectView("/"));
        }
        ModelAndView mav = new ModelAndView("clave");
        mav.addObject("usuario", us.buscarPorId(id));
        mav.addObject("title", "Modificar contraseña");
        mav.addObject("action", "clave");
        return mav;
    }
    
     @PostMapping("/clave")
    public RedirectView clave(@RequestParam Integer id, @RequestParam String clave, @RequestParam String clave2, HttpSession session, RedirectAttributes a) {
        try {
            us.clave(id, clave, clave2);
            a.addFlashAttribute("exito", "La clave se modificó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/clave/" + id);
        }
       return new RedirectView("/");
    }
}