/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.connections;

import br.com.comprazencasa.controllers.MessageReceptor;
import com.google.gson.Gson;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class MessageBox extends Thread{

    private final int idUser ;
    private final Map<Integer, UserMessage> messages ;
    private final MessageReceptor server ;
    
    public MessageBox(int idUser, MessageReceptor serverChat){
        this.idUser = idUser ;
        this.messages = new HashMap<>();
        this.server = serverChat ;
    }
    
    public void addMessage(UserMessage pop){
        this.messages.put(idUser, pop);
    }
    
    private synchronized void sendMessage(Session client, Map<Integer, UserMessage> msgSup, UserMessage msg){
        try {
            client.getBasicRemote().sendText(new Gson().toJson(msg));
            msgSup.remove(msg.getIdDestiny());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    @Override
    public void run() {
        
        Map<Integer, UserMessage> msgSup = messages ;

        Session client ;
        while(true){
            if((client = getUser(idUser)) != null && client.isOpen()){
                for(UserMessage msg : msgSup.values()){
                    sendMessage(client, msgSup, msg);
                    
                    messages.remove(idUser);
                    if(messages.size() == 0) {
                        msgSup = messages ;
                        break;
                    }
                }
            }else if(containUser(idUser) && getUser(idUser) == null){
                server.getUsers().remove(idUser);
            }
            
            if(msgSup.isEmpty()) {
                server.removeKeyMsgNotSendUsers(idUser);
                break;
            }
            try {Thread.sleep(1000);} catch (InterruptedException ex) {
                Logger.getLogger(MessageBox.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   
    private Session getUser(int idUser){
        return server.getUsers().get(idUser);
    }
    
    private boolean containUser(int idUser){
        return server.getUsers().containsKey(idUser);
    }
   
}
