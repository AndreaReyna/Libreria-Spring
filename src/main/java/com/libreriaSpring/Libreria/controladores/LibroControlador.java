package com.libreriaSpring.Libreria.controladores;

import Excepciones.ErrorServicio;
import com.libreriaSpring.Libreria.entidades.Libro;
import com.libreriaSpring.Libreria.servicios.AutorServicio;
import com.libreriaSpring.Libreria.servicios.EditorialServicio;
import com.libreriaSpring.Libreria.servicios.LibroServicio;
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
@RequestMapping("/libros")
public class LibroControlador {

    @Autowired
    private LibroServicio ls;

    @Autowired
    private AutorServicio as;

    @Autowired
    private EditorialServicio es;

    @GetMapping
    public ModelAndView mostrarTodos() {
        ModelAndView mav = new ModelAndView("libros");
        mav.addObject("libros", ls.buscarTodos());
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearLibro() {
        ModelAndView mav = new ModelAndView("libro-form");
        mav.addObject("libro", new Libro());
        mav.addObject("autores", as.buscarTodos());
        mav.addObject("editoriales", es.buscarTodas());
        mav.addObject("title", "Crear Libro");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editarLibro(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("libro-form");
        mav.addObject("libro", ls.buscarPorId(id));
        mav.addObject("autores", as.buscarTodos());
        mav.addObject("editoriales", es.buscarTodas());
        mav.addObject("title", "Editar Libro");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam(defaultValue = "0") Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam("autor") Integer idAutor, @RequestParam("editorial") Integer idEd, RedirectAttributes a) throws ErrorServicio {
        try {
            ls.crear(isbn, titulo, anio, ejemplares, idAutor, idEd);
            a.addFlashAttribute("exito", "El libro se ingresó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/libros/crear");
        }
        return new RedirectView("/libros");
    }

    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView modificar(@RequestParam Integer id, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam("autor") Integer idAutor, @RequestParam("editorial") Integer idEd, RedirectAttributes a) throws ErrorServicio {
        try {
           
            ls.modificar(id, titulo, anio, ejemplares, idAutor, idEd);
            a.addFlashAttribute("exito", "El libro se modificó correctamente!");
        } catch (Exception e) {
            a.addFlashAttribute("error", e.getMessage());
        }

        return new RedirectView("/libros");
    }

    @PostMapping("/baja/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView baja(@PathVariable Integer id) {
        ls.baja(id);
        return new RedirectView("/libros");
    }

    @PostMapping("/alta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView alta(@PathVariable Integer id) {
        ls.alta(id);
        return new RedirectView("/libros");
    }

}
