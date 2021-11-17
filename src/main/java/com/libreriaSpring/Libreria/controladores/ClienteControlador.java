package com.libreriaSpring.Libreria.controladores;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Cliente;
import com.libreriaSpring.Libreria.entidades.Usuario;
import com.libreriaSpring.Libreria.servicios.ClienteServicio;
import com.libreriaSpring.Libreria.servicios.RolServicio;
import com.libreriaSpring.Libreria.servicios.UsuarioServicio;
import static java.util.Objects.isNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/clientes")
@PreAuthorize("hasRole('ADMIN')")
public class ClienteControlador {

    @Autowired
    private ClienteServicio cs;

    @Autowired
    private UsuarioServicio us;

    @Autowired
    private RolServicio rs;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView mostrarClientes() {
        ModelAndView mav = new ModelAndView("clientes");
        mav.addObject("clientes", cs.buscarTodos());
        mav.addObject("usuarios", us.buscarTodos());

        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearCliente() {
        ModelAndView mav = new ModelAndView("cliente-form");
        mav.addObject("cliente", new Cliente());
        mav.addObject("title", "Crear Cliente");
        mav.addObject("usuarios", us.buscarTodos());
        mav.addObject("roles", rs.buscarTodos());
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre, @RequestParam String apellido, @RequestParam(defaultValue = "0") Long documento, @RequestParam String telefono, @RequestParam String correo, @RequestParam String clave, @RequestParam String clave2, @RequestParam Integer idRol, RedirectAttributes a) throws ErrorServicio {
        try {
            us.crear(nombre, apellido, documento, clave2, correo, clave, clave2, idRol);
            a.addFlashAttribute("exito", "El cliente se guardó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/clientes/crear");
        }

        return new RedirectView("/clientes");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarCliente(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("cliente-form");
        mav.addObject("cliente", cs.buscarPorId(id));
        mav.addObject("title", "Editar Cliente");
        mav.addObject("roles", rs.buscarTodos());
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam Integer id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam(defaultValue = "0") Long documento, @RequestParam String telefono, @RequestParam String correo, @RequestParam String clave, @RequestParam String clave2, @RequestParam Integer idRol, RedirectAttributes a) {
        try {
            us.modificar(id, nombre, apellido, documento, telefono, correo, clave, clave2, idRol);
            a.addFlashAttribute("exito", "El cliente se modificó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
        }

        return new RedirectView("/clientes");
    }

    @PostMapping("/baja/{id}")
    public RedirectView baja(@PathVariable Integer id) {
        cs.baja(id);
        us.baja(cs.buscarPorId(id).getUsuario().getId());
        return new RedirectView("/clientes");
    }

    @PostMapping("/alta/{id}")
    public RedirectView alta(@PathVariable Integer id) {
        cs.alta(id);
        us.alta(id);
        return new RedirectView("/clientes");
    }
}
