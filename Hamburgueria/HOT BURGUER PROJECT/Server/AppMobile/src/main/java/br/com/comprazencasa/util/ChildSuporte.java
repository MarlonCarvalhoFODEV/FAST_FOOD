package br.com.comprazencasa.util;

import java.io.Serializable;

public class ChildSuporte implements Serializable {

    private String titulo, htmlFileName;

    public ChildSuporte(){}

    public ChildSuporte(String titulo, String htmlFileName){
        this.titulo = titulo ;
        this.htmlFileName = htmlFileName ;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getHtmlFileName() {
        return htmlFileName;
    }

    public void setHtmlFileName(String htmlFileName) {
        this.htmlFileName = htmlFileName;
    }
}