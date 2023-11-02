package br.com.comprazencasa.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageManipuler {
    public static void main(String[] args) {

        String pathIn = "C:\\COMPRAZEM_CASA_RESOURCES\\Carrefour\\";
        String pathOut = "C:\\COMPRAZEM_CASA_RESOURCES\\Guanabara\\Nova pasta\\";

        File[] files = new File("C:\\COMPRAZEM_CASA_RESOURCES\\Carrefour\\").listFiles();

        int count = 0 ;
        for(File f : files){

            optimizeImage(pathIn+f.getName(), pathOut+f.getName().replaceAll(".png",".jpg"));

            //count++;
            if(count == 2) break;
        }
    }

    public static synchronized void optimizeImage(String pathIn, String pathOut){
        try {
            System.out.println(pathIn);
            File input = new File(pathIn);
            File output = new File(pathOut);

            BufferedImage image = ImageIO.read(input);
           if(image != null){
               BufferedImage result = new BufferedImage(
                       image.getWidth(),
                       image.getHeight(),
                       BufferedImage.TYPE_INT_RGB);
               result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
               ImageIO.write(result, "jpg", output);

           }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
