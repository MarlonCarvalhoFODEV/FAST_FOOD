package br.com.megasena.itens;

import java.util.HashMap;
import java.util.Map;

public class ITTabelas {



    public static class MegaSena{
        public static final int[][] megaSena = {
                {1,2,3,4,5,6,7,8,9,10},
                {11,12,13,14,15,16,17,18,19,20},
                {21,22,23,24,25,26,27,28,29,30},
                {31,32,33,34,35,36,37,38,39,40},
                {41,42,43,44,45,46,47,48,49,50},
                {51,52,53,54,55,56,57,58,59,60}
        };

        private static final Integer[][] quadranteQuatro =
                {
                        {01, 02, 03, 04, 05, 11, 12, 13, 14, 15, 21, 22, 23, 24, 25},
                        {06, 07, 8, 9, 10, 16, 17, 18, 19, 20, 26, 27, 28, 29, 30},
                        {31, 32, 33, 34, 35, 41, 42, 43, 44, 45, 51, 52, 53, 54, 55},
                        {36, 37, 38, 39, 40, 46, 47, 48, 49, 50, 56, 57, 58, 59, 60}
                };

        private static final Integer[][] subQuadranteQuatro =
                {
                        {12, 13, 14, 15, 22, 23, 24, 25},
                        {16, 17, 18, 19, 26, 27, 28, 29},
                        {32, 33, 34, 35, 42, 43, 44, 45},
                        {36, 37, 38, 39, 46, 47, 48, 49}
                };

        private static final Integer[][] quadranteDez =
                {
                        {01, 02, 11, 12, 21, 22},{03, 13, 04, 14, 23, 24},{05, 06, 15, 16, 25, 26},{07, 17, 8, 18,27, 28},{9, 10, 19, 20,29, 30},
                        {31, 32,41, 42, 51, 52},{33, 34, 43, 44, 53, 54},{35, 36, 45, 46, 55, 56},{37, 38,47, 48, 57, 58},{ 39, 40, 49, 50, 59, 60},
                };

        private static final Integer[][] quadranteQuinze =
                {
                        {01, 02, 11, 12},{03, 13, 04, 14},{05, 06, 15, 16},{07, 17, 8, 18},{9, 10, 19, 20},
                        {21, 22, 31, 32},{23, 24, 33, 34},{25, 26, 35, 36},{27, 28, 37, 38},{29, 30, 39, 40},
                        {41, 42, 51, 52},{43, 44, 53, 54},{45, 46, 55, 56},{47, 48, 57, 58},{49, 50, 59, 60},
                };

        private static  Map<String, Integer[][]> quadrantes ;

        public static Map<String, Integer[][]> getQuadrantes() {
            quadrantes = new HashMap<>();
            quadrantes.put("Q4",quadranteQuatro);
            quadrantes.put("SQ4",subQuadranteQuatro);
            quadrantes.put("Q10",quadranteDez);
            quadrantes.put("Q15",quadranteQuinze);
            return quadrantes;
        }
    }

}
