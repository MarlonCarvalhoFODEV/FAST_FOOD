package br.com.comprazencasa.util.ascii;

public class ASCII {

    public static int encodeASCII(char caracter){
        return ((int)caracter);
    }

    public static char dencodeASCII(int caracterASCII){
        return ((char)caracterASCII);
    }

    public static String encodeASCII(String text){
        char[] chars = text.toCharArray();

        StringBuilder ASCII = new StringBuilder();
        for(char c : chars){
            ASCII.append(encodeASCII(c));
        }

        return ASCII.toString();
    }

    public static String encodeASCII(String text,String separator){
        char[] chars = text.toCharArray();
        int counter = 0 ;

        StringBuilder ASCII = new StringBuilder();
        for(char c : chars){
            counter++;

            ASCII.append(encodeASCII(c));

            if(counter < chars.length)
                ASCII.append(separator);
        }

        return ASCII.toString();
    }

    public static String dencodeASCII(String asciiEncoded,String separator){
        String[] asciiArray = asciiEncoded.split(separator);

        StringBuilder ASCIIDec = new StringBuilder();
        for(String leter : asciiArray){

            int leterAscii = Integer.valueOf(leter);
            ASCIIDec.append(dencodeASCII(leterAscii));
        }

        return ASCIIDec.toString();
    }
}
