package br.com.comprazencasa.controllers;

import br.com.comprazencasa.analyrics.CzcAnalytics;
import br.com.comprazencasa.database.DaoAnalytics;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.Produces;

@RequestMapping("CzcAnalyrics")
public class AnalyticsManager {

    private final Gson gson ;
    private final DaoAnalytics dao ;

    @Autowired
    public AnalyticsManager(){
        this.gson = new Gson();
        this.dao = new DaoAnalytics();
    }

    @PostMapping(path = "/get-analytics")
    @Secured({"ADMIN","COLL"})
    @PreAuthorize("hasAnyRole('ADMIN','COLL')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public WebResponses insertAnalysis(CzcAnalytics analytics){
        boolean result = dao.insertAnalytics(analytics);

        WebResponses response = new WebResponses();
        response.setComunication(578);

        return response ;
    }

    @PostMapping(path = "/get-analytics")
    @Secured({"ADMIN","COLL"})
    @PreAuthorize("hasAnyRole('ADMIN','COLL')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public WebResponses getAna(CzcAnalytics analytics){
        boolean result = dao.insertAnalytics(analytics);

        WebResponses response = new WebResponses();
        response.setComunication(578);

        return response ;
    }
}
