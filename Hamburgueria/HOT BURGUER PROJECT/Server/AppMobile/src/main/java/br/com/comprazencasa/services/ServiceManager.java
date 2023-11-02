package br.com.comprazencasa.services;

import apis.APIEmailServer;
import apis.APIGuanabara;

public class ServiceManager {

    private APIEmailServer apiEmailServer ;
    private APIGuanabara apiGuanabara ;

    public ServiceManager(){
       // this.apiGuanabara = new APIGuanabara();
        this.apiEmailServer = new APIEmailServer();

    }

    public APIGuanabara getApiGuanabara() {
        return apiGuanabara;
    }

    public APIEmailServer getApiEmailServer() {
        return apiEmailServer;
    }
}
