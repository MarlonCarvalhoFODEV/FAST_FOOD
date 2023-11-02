package br.com.comprazencasa.controllers;

import br.com.comprazencasa.database.DaoAdmin;
import br.com.comprazencasa.database.DaoServerConfig;
import br.com.comprazencasa.util.ServerConfig;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("serverconfig")
public class ServerConfigManager {

    @GetMapping(value = "/get-config")
    @Secured({"ADMIN","COLL"})
    @PreAuthorize("hasAnyRole('ADMIN','COLL')")
    public ServerConfig getServerConfiguration(){
        return new DaoServerConfig().getServerConfig();
    }

    @PutMapping(value = "/update-config")
    @Secured({"ADMIN","COLL"})
    @PreAuthorize("hasAnyRole('ADMIN','COLL')")
    public boolean updateServerConfig(@RequestBody ServerConfig serverConfig){
        DaoAdmin.setDaoServerConfig(serverConfig);
        ServerConfig c = new DaoServerConfig().updateServerConfig(serverConfig);
        return (c==null);
    }
}
