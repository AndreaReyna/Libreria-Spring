package com.libreriaSpring.Libreria.controladores;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Cliente;
import com.libreriaSpring.Libreria.servicios.ClienteServicio;
import static java.util.Objects.isNull;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ClienteControlador {

    @Autowired
    private ClienteServicio cs;

    @GetMapping
    public ModelAndView mostrarClientes() {
        ModelAndView mav = new ModelAndView("clientes");
        mav.addObject("clientes", cs.buscarTodos());
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearCliente() {
        ModelAndView mav = new ModelAndView("cliente-form");
        mav.addObject("cliente", new Cliente());
        mav.addObject("title", "Crear Cliente");
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam(defaultValue = "0") Long documento, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono, RedirectAttributes a) throws ErrorServicio {
        try {
            cs.crear(documento, nombre, apellido, telefono);
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
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam Integer id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono, RedirectAttributes a) {
        try {
            cs.modificar(id, nombre, apellido, telefono);
            a.addFlashAttribute("exito", "El cliente se modificó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
        }

        return new RedirectView("/clientes");
    }

    @PostMapping("/baja/{id}")
    public RedirectView baja(@PathVariable Integer id) {
        cs.baja(id);
        return new RedirectView("/clientes");
    }

    @PostMapping("/alta/{id}")
    public RedirectView alta(@PathVariable Integer id) {
        cs.alta(id);
        return new RedirectView("/clientes");
    }
}
