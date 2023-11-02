/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.controllers;

import br.com.comprazencasa.collaborator.CollaboratorAccount;
import br.com.comprazencasa.collaborator.CollaboratorLogin;
import br.com.comprazencasa.collaborator.GroupOrders;
import br.com.comprazencasa.collaborator.ITOrderBuyer;
import br.com.comprazencasa.database.DaoCollaborators;
import br.com.comprazencasa.util.SendImage;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.List;

/**
 * REST Web Service
 *
 * @author Marlon
 */
@RestController
@RequestMapping("Collaborator")
@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CollaboratorManager {

    private final Gson gson ;
    private final DaoCollaborators dao ;
    /**
     * Creates a new instance of CollaboratorManager
     */
    @Autowired
    public CollaboratorManager() {
        this.gson = new Gson();
        this.dao = new DaoCollaborators();
    }

    
    @PostMapping(path = "/login")
    @Secured({"ADMIN","COLL"})
    @PreAuthorize("hasAnyRole('ADMIN','COLL')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String loginCollaborator(@RequestBody CollaboratorLogin login){
        WebResponses response = new WebResponses();
        
        CollaboratorAccount account = dao.login(login);
        if(account == null){
            response.setComunication(0);
        }else{
            response.setComunication(1);
            response.setJsonObject(gson.toJson(account));
        }
        
        return gson.toJson(response);
    }
    
    @PostMapping(path = "/register")
    @Secured("ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String registerCollaborator(@RequestBody CollaboratorAccount account){
        WebResponses response = new WebResponses();
        
        boolean sucess = dao.register(account);
        if(sucess){
            response.setComunication(0);
        }else{response.setComunication(1);}
        
        return gson.toJson(response);
    }
    
    @PostMapping(path = "/authorization")
    @Secured("ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String redirectCollaborator(@RequestBody CollaboratorAccount account){
        
        WebResponses response = new WebResponses();
        
        boolean sucess = dao.register(account);
        if(sucess){
            response.setComunication(0);
        }else{response.setComunication(1);}
        
        return gson.toJson(response);
    }
    
    @PostMapping(path = "/add-production")
    @Secured("ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String insertProduction(@RequestBody WebResponses request){
        
        GroupOrders group = gson.fromJson(request.getJsonObject(), GroupOrders.class);
        boolean status = false ;
        for (ITOrderBuyer o : group) {
            status = dao.insertProduction(o);
        }
        
        WebResponses response = new WebResponses();
        response.setComunication(status==true?3:0);
        
        return gson.toJson(response);
    }
    
    @PostMapping(path = "/get-all-coll-ids/{function}")
    @Secured("ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getAllIdColls(@PathVariable("function")String function){
        List<Integer> ids = dao.getAllCollIDS(function);
        
        WebResponses response = new WebResponses();
        response.setJsonObject(gson.toJson(ids));
        
        return gson.toJson(response);
    }

    @GetMapping(path = "/get-photo/{imgPath}")
    @Secured({"ADMIN","COLL"})
    @PreAuthorize("hasAnyRole('ADMIN','COLL')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public synchronized byte[] getPhoto(@PathVariable("imgPath")String imgPath){
        return dao.getPhoto(imgPath);
    }

    @GetMapping(path = "/insert-photo/{imgPath}")
    @Secured("ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public synchronized String insertPhoto(@PathVariable("imgPath") SendImage image){
        int result = dao.insertPhoto(image);

        WebResponses response = new WebResponses();
        response.setComunication(result);
        return new Gson().toJson(response);
    }
}
