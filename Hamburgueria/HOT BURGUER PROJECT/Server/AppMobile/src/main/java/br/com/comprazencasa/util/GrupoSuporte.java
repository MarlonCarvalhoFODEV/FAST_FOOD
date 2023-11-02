package br.com.comprazencasa.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrupoSuporte implements Serializable {

    private int idIcon ;
    private String titulo ;
    private List<ChildSuporte> subTitlulos ;

    public GrupoSuporte(){
        this.subTitlulos = new ArrayList<>();
    }

    public GrupoSuporte(int id, String titulo){
        this.idIcon = id ;
        this.titulo = titulo ;
        this.subTitlulos = new ArrayList<>();
    }

    public int getIdIcon() {
        return idIcon;
    }

    public void setIdIcon(int idIcon) {
        this.idIcon = idIcon;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void addSubItem(ChildSuporte childSuporte){
        subTitlulos.add(childSuporte);
    }

    public void delSubItem(int position){
        subTitlulos.remove(position);
    }

    public ChildSuporte getSubtitulo(int position){
        return subTitlulos.get(position);
    }

    public int getSizeSubTitulos(){
        return subTitlulos.size();
    }
}
