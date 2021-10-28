package com.libreriaSpring.Libreria.controladores;

import Excepciones.ErrorServicio;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    public RedirectView guardar(@RequestParam String nombre, RedirectAttributes a) throws ErrorServicio {
        try {
            es.crear(nombre);
            a.addFlashAttribute("exito", "La editorial se guardó correctamente!");
        } catch (ErrorServicio e) {
            a.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/editoriales/crear");
        }
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
    public RedirectView modificar(@RequestParam Integer id, @RequestParam String nombre, RedirectAttributes a) throws ErrorServicio {
        try {
            es.modificar(id, nombre);
            a.addFlashAttribute("exito", "La editorial se modificó correctamente!");
        } catch (ErrorServicio e) {
            a.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/editoriales");
    }

    @PostMapping("/baja/{id}")
    public RedirectView baja(@PathVariable Integer id) {
        es.baja(id);
        return new RedirectView("/editoriales");
    }

    @PostMapping("/alta/{id}")
    public RedirectView alta(@PathVariable Integer id) {
        es.alta(id);
        return new RedirectView("/editoriales");
    }
}
