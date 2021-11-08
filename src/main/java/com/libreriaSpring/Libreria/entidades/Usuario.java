package com.libreriaSpring.Libreria.entidades;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class) 
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false, unique = true)
    private String correo;
    @Column(nullable = false)
    private String clave;
   
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime creacion;
    
    @LastModifiedDate
    private LocalDateTime modificacion;

    private Boolean alta;

    public Usuario() {
    }

    public Usuario(Integer id, String nombre, String apellido, String correo, String clave, LocalDateTime creacion, LocalDateTime modificacion, Boolean alta) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.clave = clave;
        this.creacion = creacion;
        this.modificacion = modificacion;
        this.alta = alta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public LocalDateTime getCreacion() {
        return creacion;
    }

    public void setCreacion(LocalDateTime creacion) {
        this.creacion = creacion;
    }

    public LocalDateTime getModificacion() {
        return modificacion;
    }

    public void setModificacion(LocalDateTime modificacion) {
        this.modificacion = modificacion;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

}
