package br.com.megasena.dao;

import apis.encrypt.RSA;
import br.com.megasena.itens.ITMegaSena;
import br.com.megasena.itens.ITPadraoBinario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DaoAlgoritim {


    public ITMegaSena getMegaSena(ArrayList<Integer> numbers) {
        Integer[][] quadrante =
                {
                        {01, 02, 03, 04, 05, 11, 12, 13, 14, 15, 21, 22, 23, 24, 25},
                        {06, 07, 8, 9, 10, 16, 17, 18, 19, 20, 26, 27, 28, 29, 30},
                        {31, 32, 33, 34, 35, 41, 42, 43, 44, 45, 51, 52, 53, 54, 55},
                        {36, 37, 38, 39, 40, 46, 47, 48, 49, 50, 56, 57, 58, 59, 60}
                };

        Integer[][] quadrante15 =
                {
                        {01, 02, 11, 12},{03, 13, 04, 14},{05, 06, 15, 16},{07, 17, 8, 18},{9, 10, 19, 20},
                        {21, 22, 31, 32},{23, 24, 33, 34},{25, 26, 35, 36},{27, 28, 37, 38},{29, 30, 39, 40},
                        {41, 42, 51, 52},{43, 44, 53, 54},{45, 46, 55, 56},{47, 48, 57, 58},{49, 50, 59, 60},
                };

        Integer[][] quadrante10 =
                {
                        {01, 02, 11, 12},{03, 13, 04, 14},{05, 06, 15, 16},{07, 17, 8, 18},{9, 10, 19, 20},
                        {21, 22, 31, 32},{23, 24, 33, 34},{25, 26, 35, 36},{27, 28, 37, 38},{29, 30, 39, 40},
                        {41, 42, 51, 52},{43, 44, 53, 54},{45, 46, 55, 56},{47, 48, 57, 58},{49, 50, 59, 60},
                };

        Integer[][] subQuadrante =
                {
                        {12, 13, 14, 15, 22, 23, 24, 25},
                        {16, 17, 18, 19, 26, 27, 28, 29},
                        {32, 33, 34, 35, 42, 43, 44, 45},
                        {36, 37, 38, 39, 46, 47, 48, 49}
                };



        ArrayList<Integer> q1 = new ArrayList<>();
        return null ;
    }

    public static void main(String[] args) {
       /* DaoMegaSena mega = new DaoMegaSena();
        //ArrayList<ITMegaSena> concs = mega.getConcursos(0,"");
        *//*Map<Integer, Map<Integer, Integer>> maisSorteados = mega.getMaisSorteados();
        int coluna = 2 ;
        for(Integer key : maisSorteados.get(coluna).keySet()){
            System.out.println("NUMERO: "+key+"  "+maisSorteados.get(coluna).get(key)+"x");
        }*//*

        Map<ITPadraoBinario,Integer> pads = new HashMap<>();

        ArrayList<ITPadraoBinario> padB = new DaoMegaSena().getPadraoBinario("2019-01-01","2019-12-01");

        for(ITPadraoBinario pad : padB){
            if(pads.containsKey(pad)) {
                int count = pads.get(pad);
                pad.setCount((count+1));
                pads.put(pad,(count+1));
            }
            else{
                pads.put(pad,0);
            }
        }

        ArrayList<ITPadraoBinario> padroesRepetidos = new ArrayList<>();
        for(ITPadraoBinario pad : pads.keySet()){
            pad.setCount(pads.get(pad));

            if(pad.getCount() > 0)
                padroesRepetidos.add(pad);
        }

        for(ITPadraoBinario p : padroesRepetidos)
            System.out.println("CONCURSO: "+p.getConcurso()+" - DATA("+p.getMes()+"): "+p.getData()+" - "+Arrays.toString(p.toArray())+" "+p.getCount()+"x");

*/

        RSA rsa = new RSA(null,null);
        RSA.Keys keys = rsa.generateKey(2048);
        System.out.println("PRIVATE: "+keys.getPrivateKeyBase64());
        System.out.println("PUBLIC: "+keys.getPublicKeyBase64());
    }
}
