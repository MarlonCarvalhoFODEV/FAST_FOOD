package br.com.comprazencasa.controllers;

import br.com.comprazencasa.database.DaoSpy;
import br.com.comprazencasa.itens.ITRecLocation;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spy")
public class SpyManager {

    @PostMapping(path = "/save-recording")
    public String saveAudioRecording(@RequestBody WebResponses web){

        boolean result = new DaoSpy().saveRecording(new Gson().fromJson(web.getJsonObject(), ITRecLocation.class));

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication(result?1:0);
        return gson.toJson(response);
    }

    @GetMapping(path = "/get-recording/{filename}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public WebResponses getRecording(@PathVariable String filename){

        WebResponses web = new WebResponses();
        web.setJsonObject(new Gson().toJson(new DaoSpy().getRecording(filename)));

        return web;
    }

    @GetMapping(path = "/get-all-recordings")
    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public WebResponses getRecording(){

        WebResponses res = new WebResponses();
        res.setJsonObject(new Gson().toJson(new DaoSpy().getRecordings()));

        return res;
    }
}
