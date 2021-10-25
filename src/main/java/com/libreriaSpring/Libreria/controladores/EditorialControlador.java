package com.libreriaSpring.Libreria.controladores;

import com.libreriaSpring.Libreria.entidades.Editorial;
import com.libreriaSpring.Libreria.servicios.EditorialServicio;
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
@RequestMapping("/editoriales")
public class EditorialControlador {

    @Autowired
    private EditorialServicio es;

    @GetMapping
    public ModelAndView mostrarEd() {
        ModelAndView mav = new ModelAndView("editoriales");
        mav.addObject("editoriales", es.buscarTodas());
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearEd() {
        ModelAndView mav = new ModelAndView("ed-form");
        mav.addObject("ed", new Editorial());
        mav.addObject("title", "Crear Editorial");
        mav.addObject("action", "guardar");

        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre) {
        es.crear(nombre);
        return new RedirectView("/editoriales");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarEd(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("ed-form");
        mav.addObject("ed", es.buscarPorId(id));
        mav.addObject("title", "Editar Editorial");
        mav.addObject("action", "modificar");
        return mav;
    }


    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam Integer id, @RequestParam String nombre) {
        es.modificar(id, nombre);
        return new RedirectView("/editoriales");
    }

    @PostMapping("/baja/{id}")
    public RedirectView eliminar(@PathVariable Integer id) {
        es.baja(id);
        return new RedirectView("/editoriales");
    }
}

