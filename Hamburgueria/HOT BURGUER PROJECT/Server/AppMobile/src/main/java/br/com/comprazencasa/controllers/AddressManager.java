/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.controllers;

import br.com.comprazencasa.database.DaoAddress;
import br.com.comprazencasa.geo.LatLng;
import br.com.comprazencasa.geo.Street;
import br.com.comprazencasa.util.GrupoSuporte;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * REST Web Service
 *
 * @author Marlon
 */
@RestController
@RequestMapping("Address")
public class AddressManager {

    private final Gson gson ;
    private final DaoAddress dao ;

    /**
     * Creates a new instance of RegionsManager
     */
    @Autowired
    public AddressManager() {
        this.gson = new Gson();
        this.dao = new DaoAddress();
    }

    @GetMapping(path = "/get-address/{cep}")
    @Secured({"USER","ADMIN","COLL"})
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getAddressFromCEP(@PathVariable("cep") String cep){
        return gson.toJson(dao.getStreetFromCEP(cep));
    }

    @GetMapping(path = "/postal-code-operation/{cep}")
    @Secured({"USER","ADMIN","COLL"})
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String operationNeighborhoodFromCEP(@PathVariable("cep") String cep){

        Street street = dao.verifyOperationNeighborhood(cep);
        WebResponses web = new WebResponses();
        web.setComunication(street!=null?1:100);
        web.setJsonObject(gson.toJson(street));

        return gson.toJson(web);
    }

    @PostMapping(path = "/get-address-from-coordinate")
    @Secured({"USER","ADMIN","COLL"})
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getAddressFromCoordinate(@RequestBody LatLng latLng){
        Street street = dao.getStreetFromCoordinate(latLng);

        WebResponses response = new WebResponses();
        response.setJsonObject(gson.toJson(street));
        response.setComunication(street!=null?2:-1);

        return gson.toJson(response);
    }

    @PostMapping(path = "/get-cep-from-coordinate")
    @Secured({"USER","ADMIN","COLL"})
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getCEPFromCoordinate(@RequestBody LatLng latLng){
        String cep = dao.getCepFromCoordinate(latLng);

        WebResponses response = new WebResponses();
        response.setJsonObject(cep);
        response.setComunication(cep!=null?2:0);

        return gson.toJson(response);
    }

    @PostMapping(path = "/get-from-info")
    @Secured({"USER","ADMIN","COLL"})
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getAddressFromInfo(@RequestBody WebResponses webRes){

        Object object = gson.fromJson(webRes.getJsonObject(), Object.class);

        WebResponses response = new WebResponses();

        if(object instanceof String && object.toString().length() == 8){
            String cep = dao.getCepFromCoordinate(null);
            response.setJsonObject(cep);
            response.setComunication(cep!=null?2:0);

        } else if(object instanceof LatLng){
            Street street = dao.getStreetFromCoordinate((LatLng) object);
            response.setJsonObject(gson.toJson(street));
            response.setComunication(street!=null?1:0);

        } else if(object instanceof String){
            Street street = dao.getStreetFromAddress((String) object);
            response.setJsonObject(gson.toJson(street));
            response.setComunication(street!=null?1:0);
        }

        return gson.toJson(response);
    }

    @PostMapping(path = "/locals-delivery")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getLocalsDelivery(@RequestBody WebResponses web){
        List<GrupoSuporte> locals = dao.getLocalsDelivery();

        WebResponses response = new WebResponses();
        response.setJsonObject(gson.toJson(locals));
        response.setComunication(locals.size()>0?1:0);

        return gson.toJson(response);
    }

    /*@RequestMapping(value = "/", method = RequestMethod.GET)
    public Response myMethod() throws URISyntaxException {
        URI uri = new URI("https://www.google.com");
        return Response.status (Response.Status.TEMPORARY_REDIRECT).location( uri ).build();
    }*/

    @GetMapping("/")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String index() {

        return "index";
    }
}
