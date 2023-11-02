package br.com.comprazencasa.controllers;

import br.com.comprazencasa.connections.GetHttpSessionConfigurator;
import br.com.comprazencasa.connections.MessageBox;
import br.com.comprazencasa.connections.UserMessage;
import br.com.comprazencasa.database.DaoChat;
import br.com.comprazencasa.database.DaoCollaborators;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ServerEndpoint(value = "/MessageExchange")
public class MessageReceptor {

    private static final int FIRST_CONNECTION = 1000 ;
    private static final int CHAT_SUPPORT_ONLINE = 1004 ;
    private static final int COUNT_CONNECTION = 1009 ;

    private static final int SUPPORT_ONLINE = 1006 ;
    private static final int SUPPORT_OFFLINE = 1008 ;

    private final DaoChat dao ;
    private static final Map<Integer, Session> collaborators = new HashMap<>();;
    private static final Map<Integer, MessageBox> messagesNotSendCollaborators = new HashMap<>();;
    private static final Map<Integer, Session> users = new HashMap<>();
    private static final Map<Integer, MessageBox> messagesNotSendUsers = new HashMap<>();


    @Autowired
    public MessageReceptor(){
        this.dao = new DaoChat();
    }

    private synchronized void verifySupportOnline(UserMessage msg){

        if(users.containsKey(-15) && users.get(-15).isOpen() == true){
            try{
                msg.setMessage(""+SUPPORT_ONLINE);
                sendMessageUsers(msg, new Gson().toJson(msg));
            }
            catch(Exception e){
                users.remove(-15);
            }
        }
        else{
            msg.setMessage(""+SUPPORT_OFFLINE);
            //pop.setJsonObject(new Gson().toJson(msg));
            //sendMessage(msg, new Gson().toJson(msg));
            users.remove(-15);
        }
    }

    private synchronized void sendMessageUsers(UserMessage msg, String json){

        for(int idDestiny : msg.getIdDestiny()){
            if(users.get(idDestiny) != null && users.get(idDestiny).isOpen()){
                try {
                    users.get(idDestiny).getBasicRemote().sendText(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                users.remove(idDestiny);
                addMessageNotSendUsers(idDestiny, msg);
            }
        }
    }

    private synchronized void sendMessageCollaborators(UserMessage msg, String json){

        List<Integer> ids = new DaoCollaborators().getAllCollIDS(msg.getTypeDestiny());
        for (Integer idConn : collaborators.keySet()) {

            if(ids.contains(idConn)){
                try {
                    collaborators.get(idConn).getBasicRemote().sendText(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addMessageNotSendUsers(int idUser, UserMessage msg){

        if(!messagesNotSendUsers.containsKey(idUser)){
            MessageBox msgBox = new MessageBox(idUser, this);
            msgBox.addMessage(msg);
            msgBox.start();
            messagesNotSendUsers.put(idUser, msgBox);
        }
        else{
            messagesNotSendUsers.get(idUser).addMessage(msg);
        }
    }

    public void removeKeyMsgNotSendUsers(int idUser){
        messagesNotSendUsers.remove(idUser);
    }

    public void removeKeyMsgNotSendCollaborators(int idUser){
        messagesNotSendCollaborators.remove(idUser);
    }

    public Map<Integer, Session> getUsers(){
        return users;
    }

    @OnOpen
    public void onOpen(Session session) {

    }

    @OnMessage
    public void onMessage(Session ws, String json) {
        UserMessage msg = new Gson().fromJson(json, UserMessage.class);
        if(msg.getTypeDestiny() != null &&
                msg.getTypeDestiny().equals(UserMessage.BUYER) ||
                msg.getTypeDestiny() != null &&
                        msg.getTypeDestiny().equals(UserMessage.DELIVERYMAN)){

            switch(msg.getTypeCommunication()){
                case FIRST_CONNECTION:{
                    collaborators.put(msg.getIdSender(), ws);
                    break;
                }
                case UserMessage.COMMUNICATION:{
                    sendMessageCollaborators(msg, json);
                    break;
                }
                case CHAT_SUPPORT_ONLINE:{
                    verifySupportOnline(msg);
                    break;
                }
            }
        }
        else{
            switch(msg.getTypeCommunication()){
                case FIRST_CONNECTION:{
                    users.put(msg.getIdSender(), ws);
                    break;
                }
                case UserMessage.COMMUNICATION:{
                    sendMessageUsers(msg, json);
                    break;
                }
                case CHAT_SUPPORT_ONLINE:{
                    //System.out.println("VERIFY SUPPORT");
                    verifySupportOnline(msg);
                    break;
                }
                case COUNT_CONNECTION:{
                    //System.out.println("VERIFY SUPPORT");
                    ArrayList<Integer> destinys =new ArrayList<>();
                    destinys.add(-77777777);

                    UserMessage msgs = new UserMessage();
                    msgs.setTypeCommunication(UserMessage.COMMUNICATION);
                    msgs.setIdDestinys(destinys);
                    msgs.setMessage("COUNT");
                    msg.setIdSender(users.size());
                    sendMessageUsers(msgs, json);
                    break;
                }
            }
        }
    }

    @OnError
    public void onError(Throwable t) {
        //System.err.println("USUARIO DESCONECTADO! - ERROR ");
    }

    @OnClose
    public void onClose(Session session) {
        //System.err.println("USUARIO DESCONECTADO!");
    }

}
