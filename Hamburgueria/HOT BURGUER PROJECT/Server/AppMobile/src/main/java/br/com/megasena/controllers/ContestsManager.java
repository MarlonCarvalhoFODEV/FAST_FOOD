package br.com.megasena.controllers;

import apis.encrypt.AES;
import br.com.comprazencasa.controllers.AddressManager;
import br.com.megasena.dao.*;
import br.com.megasena.itens.*;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("contests")
public class ContestsManager {

    @PostMapping(path = "/get-data-contest")
    public String getDataContest(@RequestBody String post,HttpServletRequest req) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        JSONObject ob = new DaoCryptography().decrypt(post);
        String idDevice = ob.has("id_dev")?ob.getString("id_dev"):null;
        int idGame = ob.has("gm")?ob.getInt("gm"):-1;
        String ip = req.getHeader("X-FORWARDED-FOR");

        Gson gson = new Gson();
        JSONObject response = null ;
        switch (idGame){
            case 0:{
                ITMegaSena sen = new DaoMegaSena().getUltimoSorteio(ip);
                response = new JSONObject(gson.toJson(sen));
                response.put("est",sen.getValorEstimadoProximoConcurso());
                response.put("cc",sen.getConcurso());
                response.put("dat",sen.getData());
                response.put("tim",sen.getTimeDiff());
                response.put("type",idGame);
                break;
            }
            case 1:{
                ITLotoFacil loto = new DaoLotoFacil().getUltimoSorteio(ip);
                response = new JSONObject(gson.toJson(loto));
                response.put("est",loto.getEstimativaPremio());
                response.put("cc",loto.getConcurso());
                response.put("dat",loto.getData());
                response.put("tim",loto.getTimeDiff());
                response.put("type",idGame);
                break;
            }
            case 2:{
                ITQuina quina = new DaoQuina().getUltimoSorteio(ip);
                response = new JSONObject(gson.toJson(quina));
                response.put("est",quina.getEstimativaPremioProximoSorteio());
                response.put("cc",quina.getConcurso());
                response.put("dat",quina.getData());
                response.put("tim",quina.getTimeDiff());
                response.put("type",idGame);
                break;
            }
            case 3:{
                ITLotoMania lotoMania = new DaoLotoMania().getUltimoSorteio(ip);
                response = new JSONObject(gson.toJson(lotoMania));
                response.put("est",lotoMania.getEstimativaPremio());
                response.put("cc",lotoMania.getConcurso());
                response.put("dat",lotoMania.getData());
                response.put("tim",lotoMania.getTimeDiff());
                response.put("type",idGame);
                break;
            }
            case 4:{
                // TIME MANIA
                ITTimeMania lotoMania = new DaoTimeMania().getUltimoSorteio(ip);
                response = new JSONObject(gson.toJson(lotoMania));
                response.put("est",lotoMania.getEstimativaPremio());
                response.put("cc",lotoMania.getConcurso());
                response.put("dat",lotoMania.getData());
                response.put("tim",lotoMania.getTimeDiff());
                response.put("type",idGame);
                break;
            }
            case 5:{
                // DUPLA SENA
                ITDuplaSena duplaSena = new DaoDuplaSena().getUltimoSorteio(ip);
                response = new JSONObject(gson.toJson(duplaSena));
                response.put("est",duplaSena.getEstimativaPremio());
                response.put("cc",duplaSena.getConcurso());
                response.put("dat",duplaSena.getData());
                response.put("tim",duplaSena.getTimeDiff());
                response.put("type",idGame);
                break;
            }
            case 6:{
                // LOTECA
                ITLoteca loteca = new DaoLoteca().getUltimoSorteio(ip);
                response = new JSONObject(gson.toJson(loteca));
                response.put("est",loteca.getEstimativaPremio());
                response.put("cc",loteca.getConcurso());
                response.put("dat",loteca.getData());
                response.put("tim",loteca.getTimeDiff());
                response.put("type",idGame);
                break;
            }
            case 7:{
                // DIA DE SORTE
                //ITLotoMania lotoMania = new DaoLotoMania().getUltimoSorteio(ip);
                response = new JSONObject();
                response.put("not",1);
                response.put("type",idGame);
                break;
            }
            case 8:{
                // SUPER SETE
                ITTimeMania lotoMania = new DaoTimeMania().getUltimoSorteio(ip);
                response = new JSONObject(gson.toJson(lotoMania));
                response.put("est",lotoMania.getEstimativaPremio());
                response.put("cc",lotoMania.getConcurso());
                response.put("dat",lotoMania.getData());
                response.put("tim",lotoMania.getTimeDiff());
                response.put("type",idGame);
                break;
            }
            default:{
                response = new JSONObject();
                response.put("not",1);
                response.put("type",idGame);
            }
        }

        return AES.encryptToAESJson(response) ;
    }

    @PostMapping(path = "/get-contest")
    public String getContest(@RequestBody String post,HttpServletRequest req) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        JSONObject ob = new DaoCryptography().decrypt(post);

        String idDevice = ob.has("id_dev")?ob.getString("id_dev"):null;
        int idGame = ob.has("gm")?ob.getInt("gm"):-1;
        int concurso = ob.has("cc")?ob.getInt("cc"):-1;
        String ip = req.getHeader("X-FORWARDED-FOR");

        Gson gson = new Gson();
        JSONObject response = null ;
        switch (idGame){
            case 0:{
                ITMegaSena sen = new DaoMegaSena().getSorteio(concurso,ip);
                response = new JSONObject(gson.toJson(sen));
                response.put("est",sen.getValorEstimadoProximoConcurso());
                response.put("cc",sen.getConcurso());
                response.put("dat",sen.getData());
                response.put("tim",sen.getTimeDiff());
                response.put("type",idGame);
                break;
            }
            case 1:{
                ITLotoFacil loto = new DaoLotoFacil().getUltimoSorteio(ip);
                response = new JSONObject(gson.toJson(loto));
                response.put("est",loto.getEstimativaPremio());
                response.put("cc",loto.getConcurso());
                response.put("dat",loto.getData());
                response.put("tim",loto.getTimeDiff());
                response.put("type",idGame);
                break;
            }
        }

        return AES.encryptToAESJson(response) ;
    }

    @PostMapping(path = "/get-numbers-most-likely")
    public String numbersMostLikely(@RequestBody String post,HttpServletRequest req) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        JSONObject ob = new DaoCryptography().decrypt(post);

        String idDevice = ob.has("id_dev")?ob.getString("id_dev"):null;
        int idGame = ob.has("gm")?ob.getInt("gm"):-1;
        int column = ob.has("cc")?ob.getInt("cc"):-1;
        String ip = req.getHeader("X-FORWARDED-FOR");

        Gson gson = new Gson();

        Map<Integer, Integer> response = null ;
        switch (idGame){
            case 0:{
                response = new DaoMegaSena().getProbabilidadeColor(column);
                break;
            }
            case 1:{

                break;
            }
        }

        return AES.encryptToAESJson(response) ;
    }
}
