package com.libreriaSpring.Libreria.controladores;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Prestamo;
import com.libreriaSpring.Libreria.servicios.LibroServicio;
import com.libreriaSpring.Libreria.servicios.PrestamoServicio;
import com.libreriaSpring.Libreria.servicios.UsuarioServicio;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/prestamos")
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio ps;

    @Autowired
    private UsuarioServicio us;

    @Autowired
    private LibroServicio ls;

    @GetMapping
    public ModelAndView mostrarPrestamos() {
        ModelAndView mav = new ModelAndView("prestamos");
        mav.addObject("prestamos", ps.buscarTodos());
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearPrestamo() {
        ModelAndView mav = new ModelAndView("prestamo-form");
        mav.addObject("prestamo", new Prestamo());
        mav.addObject("usuarios", us.buscarTodos());
        mav.addObject("libros", ls.buscarTodos());
        mav.addObject("title", "Crear Prestamo");
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaPrestamo, @RequestParam("libro") Integer idLibro, @RequestParam("usuario") Integer idUsuario, RedirectAttributes a) throws ErrorServicio {
        try {
            ps.crearPrestamo(fechaPrestamo, idLibro, idUsuario);
            a.addFlashAttribute("exito", "El prestamo se ingresó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
        }

        return new RedirectView("/prestamos");
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editarPrestamo(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("prestamo-form");
        mav.addObject("prestamo", ps.buscarPorId(id));
        mav.addObject("title", "Editar Prestamo");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView eliminar(@RequestParam Integer id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDevolucion, RedirectAttributes a) throws ErrorServicio {
        try {
            ps.baja(id, fechaDevolucion);
            a.addFlashAttribute("exito", "La devolución se realizó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/prestamos");
    }
}
