package com.firebase.firebase;

import org.springframework.stereotype.Component;

/**
 *
 * @author rodot
 */
@Component
public class Comentario {

    private String id;

    private String idUser;

    private String nameUser;

    private String libro;

    private String ofertaLibro;

    /**
     * @return the nameUser
     */
    public String getNameUser() {
        return nameUser;
    }

    /**
     * @param nameUser the nameUser to set
     */
    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    /**
     * @return the ofertaLibro
     */
    public String getOfertaLibro() {
        return ofertaLibro;
    }

    /**
     * @param ofertaLibro the ofertaLibro to set
     */
    public void setOfertaLibro(String ofertaLibro) {
        this.ofertaLibro = ofertaLibro;
    }

    /**
     * @return the idUser
     */
    public String getIdUser() {
        return idUser;
    }

    /**
     * @param idUser the idUser to set
     */
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    /**
     * @return the libro
     */
    public String getLibro() {
        return libro;
    }

    /**
     * @param libro the libro to set
     */
    public void setLibro(String libro) {
        this.libro = libro;
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

}
