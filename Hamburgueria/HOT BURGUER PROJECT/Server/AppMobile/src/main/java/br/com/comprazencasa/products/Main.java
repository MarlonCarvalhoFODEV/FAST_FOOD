package br.com.comprazencasa.products;

import apis.APIGuanabara;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String cel = "+55 (21) 99221-9815";
        String ddi = cel.substring(0,3);
        String ddd = cel.substring(cel.indexOf("(")+1,cel.indexOf(")"));
        String number = cel.substring(cel.indexOf(")")+2).replace(" ","").replace("-","");

        System.out.println("DDI: "+ddi);
        System.out.println("DDD: "+ddd);
        System.out.println("NUMBER: "+number);
    }

}
