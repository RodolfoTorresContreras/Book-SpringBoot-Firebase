package com.firebase.firebase;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

public class Producto {

    /**
     * @return the persona
     */
    public String getPersona() {
        return persona;
    }

    /**
     * @param persona the persona to set
     */
    public void setPersona(String persona) {
        this.persona = persona;
    }

    private String id;

    private String genero;

    private String nombre;

    private String autor;

    private String idioma;

    private String descripcion;

    private String img;

    private String condicion;

    private String precioInicial;

    private String precioPuja;

    private String persona;

    @NotNull
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date duracion;

    public Producto() {
        super();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the genero
     */
    public String getGenero() {
        return genero;
    }

    /**
     * @param genero the genero to set
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the autor
     */
    public String getAutor() {
        return autor;
    }

    /**
     * @param autor the autor to set
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * @return the idioma
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * @param idioma the idioma to set
     */
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return the condicion
     */
    public String getCondicion() {
        return condicion;
    }

    /**
     * @param condicion the condicion to set
     */
    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    /**
     * @return the precioInicial
     */
    public String getPrecioInicial() {
        return precioInicial;
    }

    /**
     * @param precioInicial the precioInicial to set
     */
    public void setPrecioInicial(String precioInicial) {
        this.precioInicial = precioInicial;
    }

    /**
     * @return the precioPuja
     */
    public String getPrecioPuja() {
        return precioPuja;
    }

    /**
     * @param precioPuja the precioPuja to set
     */
    public void setPrecioPuja(String precioPuja) {
        this.precioPuja = precioPuja;
    }

    /**
     * @return the duracion
     */
    public Date getDuracion() {
        return duracion;
    }

    /**
     * @param duracion the duracion to set
     */
    public void setDuracion(Date duracion) {
        this.duracion = duracion;
    }

}
