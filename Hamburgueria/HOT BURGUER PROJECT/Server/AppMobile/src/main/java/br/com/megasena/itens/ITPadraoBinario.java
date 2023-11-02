package br.com.megasena.itens;

import java.util.ArrayList;
import java.util.Map;

public class ITPadraoBinario extends ArrayList<Integer> {

    private int id ;
    private int concurso ;
    private int count ;
    private String mes ;
    private String data ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConcurso() {
        return concurso;
    }

    public void setConcurso(int concurso) {
        this.concurso = concurso;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ArrayList<ITMegaSena> getJogos01(Map<Integer,Integer> c1, Map<Integer,Integer> c2, Map<Integer,Integer> c3, Map<Integer,Integer> c4, Map<Integer,Integer> c5, Map<Integer,Integer> c6){

        return null;
    }


    public int parImpar(int number){
        String n = String.valueOf(number);
        int lastNumber = Integer.valueOf(n.substring(n.length()-1));

        if (lastNumber == 0 || lastNumber == 2 || lastNumber == 4 || lastNumber == 6 || lastNumber == 8)
            return 0;
        return 1 ;
    }
}
