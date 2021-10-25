package com.libreriaSpring.Libreria.controladores;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Autor;
import com.libreriaSpring.Libreria.servicios.AutorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/autores")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping
    public ModelAndView mostrarAutores() {
        ModelAndView mav = new ModelAndView("autores");
        mav.addObject("autores", autorServicio.buscarTodos());
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearAutor() {
        ModelAndView mav = new ModelAndView("autor-form");
        mav.addObject("autor", new Autor());
        mav.addObject("title", "Crear Autor");
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre) throws ErrorServicio {
        autorServicio.crear(nombre);
        return new RedirectView("/autores");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarAutor(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("autor-form");
        mav.addObject("autor", autorServicio.buscarPorId(id));
        mav.addObject("title", "Editar Autor");
        mav.addObject("action", "modificar");
        return mav;
    }


    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam Integer id, @RequestParam String nombre) {
        autorServicio.modificar(id, nombre);
        return new RedirectView("/autores");
    }

    @PostMapping("/baja/{id}")
    public RedirectView eliminar(@PathVariable Integer id) {
        autorServicio.baja(id);
        return new RedirectView("/autores");
    }
}