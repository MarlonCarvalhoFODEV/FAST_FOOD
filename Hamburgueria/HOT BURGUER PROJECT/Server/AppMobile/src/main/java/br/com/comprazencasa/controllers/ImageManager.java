/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.controllers;

import br.com.comprazencasa.database.DaoImage;
import br.com.comprazencasa.util.SendImage;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author Marlon
 */
@RestController
@RequestMapping("Image")
public class ImageManager {

    /**
     * Creates a new instance of ImagesManager
     */
    @Autowired
    public ImageManager() {
    }

    @PostMapping(path = "/insert")
    public synchronized String saveImage(@RequestBody SendImage image){
        int result = new DaoImage().getProductImage().insertImageDB(image);

        WebResponses response = new WebResponses();
        response.setComunication(result);
        return new Gson().toJson(response);
    }

    @PostMapping(path = "/insert-for-user")
    public synchronized String saveImageForUser(@RequestBody SendImage image){
        int result = new DaoImage().getUserImage().insertImageDB(image);

        WebResponses response = new WebResponses();
        response.setComunication(result);
        return new Gson().toJson(response);
    }

    @GetMapping(path = "/get-image/{imgPath}")
    public synchronized byte[] getImage(@PathVariable("imgPath")String imgPath){
        return new DaoImage().getProductImage().getImageDB(imgPath);
    }

    @GetMapping(path = "/get-image-user/{imgPath}", produces = MediaType.IMAGE_JPEG_VALUE)
    public synchronized @ResponseBody byte[] getImageUser(@PathVariable("imgPath")String imgPath){

        return new DaoImage().getUserImage().selectImageDB(imgPath);
    }

    @GetMapping(path = "/get-image-promotional/{imgPath}")
    public synchronized byte[] getImagePromotion(@PathVariable("imgPath")String imgPath){

        return new DaoImage().getProductImage().getImagePromotionDB(imgPath);
    }
  
    @PostMapping(path ="/update")
    public synchronized String updateImage(@RequestBody SendImage image){

        boolean result = new DaoImage().updateImage(image);

        WebResponses response = new WebResponses();
        response.setComunication(result?1:0);
        return new Gson().toJson(response);
    }
    
    @PostMapping(path ="/delete")
    public synchronized String deleteImage(@RequestBody SendImage image){
        boolean result = new DaoImage().deleteImage(image);
        
        WebResponses response = new WebResponses();
        response.setComunication(result?1:0);
        return new Gson().toJson(response);
    }

    @GetMapping(path ="/get-others-image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public  @ResponseBody byte[] getOthersImage(@PathVariable int id){
        return new DaoImage().getOtherImage().getImageDB(id);
    }

}
