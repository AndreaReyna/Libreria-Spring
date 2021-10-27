package com.libreriaSpring.Libreria.controladores;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Prestamo;
import com.libreriaSpring.Libreria.servicios.ClienteServicio;
import com.libreriaSpring.Libreria.servicios.LibroServicio;
import com.libreriaSpring.Libreria.servicios.PrestamoServicio;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/prestamos")
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio ps;
    
    @Autowired
    private ClienteServicio cs;
    
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
        mav.addObject("clientes", cs.buscarTodos());
        mav.addObject("libros", ls.buscarTodos());
        mav.addObject("title", "Crear Prestamo");
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam("libro") Integer idLibro, @RequestParam("cliente") Integer idCliente) throws ErrorServicio{
        ps.crearPrestamo(idLibro, idCliente);
        return new RedirectView("/prestamos");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarPrestamo(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("prestamo-form");
        mav.addObject("prestamo", ps.buscarPorId(id));
        mav.addObject("title", "Editar Prestamo");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/modificar")
    public RedirectView eliminar(@RequestParam Integer id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDevolucion) {
        ps.baja(id, fechaDevolucion);
        return new RedirectView("/prestamos");
    }
}
