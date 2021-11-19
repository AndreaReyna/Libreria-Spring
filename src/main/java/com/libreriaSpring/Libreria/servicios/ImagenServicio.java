package com.libreriaSpring.Libreria.servicios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImagenServicio {
    
    @Value("${my.property}")
    private String directory;

    public String copiar(MultipartFile archivo) throws Exception {
        try {
            String imagen = archivo.getOriginalFilename();
            Path ruta = Paths.get(directory, imagen).toAbsolutePath();
            Files.copy(archivo.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);
            return imagen;
        } catch (IOException e) {
            throw new Exception("Error al guardar la imagen");
        }
    }
}
