package com.c.hotburguerdelivery.service_chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.acitivtys.cbuyer.BuyerActivity;

import java.util.List;
import java.util.Map;

public class CZCNotification {

    public static final String STATUS_WAIT = "wait";
    public static final String STATUS_IN_PRODUCTION = "in_production";
    public static final String STATUS_FOR_DELIVERY = "for_delivery";
    public static final String STATUS_DELIVERY_ON_MY_WAY = "on_my_way";
    public static final String STATUS_FINALIZED = "finalized";


    public static void notificationOne(Context context, UserMessage msg){

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context,0, new Intent(context, BuyerActivity.class),0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(msg.getMessage());
        builder.setContentTitle(msg.getMessage()).setColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setContentText("OOH Glória, manda mais JESUS :)");
        builder.setSmallIcon(R.drawable.ic_notification);
        //builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_cred_card));

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.addLine("OOH Glória, manda mais JESUS :)");
        //String[] descs = decodeMessage(msg.getMessage()).split(" ");
        //for (int i=0; i < descs.length; i++){
        //    style.addLine(descs[i]);
        //}

        builder.setStyle(style);
        builder.setContentIntent(p);

        Notification n = builder.build();
        n.flags = Notification.FLAG_AUTO_CANCEL ;
        n.vibrate = new long[]{150, 300, 150, 600};// {150mils (espera) , 300mils (vibração), 150mils (espera) , 300mils (vibração)}

        nm.notify(405684, n);

        try{

            Uri soundNotify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, soundNotify);
            toque.play();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void notificationDeliveryman(Context context, UserMessage msg){

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context,0, new Intent(context, BuyerActivity.class),0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(msg.getMessage());
        builder.setContentTitle(msg.getMessage()).setColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setContentText("Vamosh que vamosh!");
        builder.setSmallIcon(R.drawable.ic_notification);
        //builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_cred_card));

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.addLine("Vamosh que vamosh!");
        //String[] descs = decodeMessage(msg.getMessage()).split(" ");
        //for (int i=0; i < descs.length; i++){
        //    style.addLine(descs[i]);
        //}

        builder.setStyle(style);
        builder.setContentIntent(p);

        Notification n = builder.build();
        n.flags = Notification.FLAG_AUTO_CANCEL ;
        n.vibrate = new long[]{150, 300, 150, 600};// {150mils (espera) , 300mils (vibração), 150mils (espera) , 300mils (vibração)}

        nm.notify(405684, n);

        try{

            Uri soundNotify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, soundNotify);
            toque.play();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static String decodeMessage(String msg){
        switch (msg){
            case STATUS_IN_PRODUCTION: return "Estamos preparando o seu pedido.";
            case STATUS_FOR_DELIVERY: return "Pronto para entrega.";
            case STATUS_DELIVERY_ON_MY_WAY: return "A caminho!";
            case STATUS_FINALIZED: return "Seu pedido chegou!!";
            default: return "Opss!! Erro na comunicação." ;
        }
    }

    public static void notificationMessages(Context context, UserMessage msg, Map<Integer, List<String>> msgs){
        String formatNotify;

        /*if(msgs.containsKey(msg.getIdProduct())){

            formatNotify = msg.getProductName().substring(0,20);
            msgs.get(msg.getIdProduct()).add(formatNotify);
        }
        else{
            formatNotify = msg.getProductName().substring(0,20);

            List<String> list = new ArrayList<>();
            list.add(formatNotify);

            msgs.put(msg.getIdProduct(),list);
        }

        StringBuilder sup = new StringBuilder();
        if(!msgs.isEmpty()){
            for(List<String> messagesProduct : msgs.values()){

                if(msgs.size()>1) {
                    sup.append(messagesProduct.get(0) + "... (" + messagesProduct.size()
                            + (messagesProduct.size() == 1 ? " Mensagem" : " Mensagens") + ")┬");
                }
                else {
                    sup.append(messagesProduct.get(0) + "... (" + messagesProduct.size()
                            + (messagesProduct.size() == 1 ? " Mensagem" : " Mensagens") + ")");
                }
            }
        }*/

        Intent intent = null;

        /*if(HomeActivity.active == false){
            intent = new Intent(context, HomeActivity.class);
            intent.putExtra("OPEN_MESSAGE",msgs.size());
            intent.putExtra("SIZE_MESSAGE",msgs.size());
            intent.putExtra("CLICKED",true);
        }
        else if(msgs.size() >= 2){
            intent = new Intent(context, MessageActivity.class);
            intent.putExtra("OPEN_MESSAGE", 1);
            intent.putExtra("CLICKED",true);
        }
        else {

            intent = new Intent(context, ChatActivity.class);

            Chat chat = new Chat();
            chat.setIdSender(msg.getIdDestiny());
            chat.setIdDestiny(msg.getIdSender());
            chat.setIdAnnouncer(msg.getIdAnoouncer());
            chat.setIdProduct(msg.getIdProduct());
            chat.setDestinyName(msg.getUserName());
            chat.setSenderName(msg.getDestinyName());
            chat.setTitleAds(msg.getProductName());
            chat.setPathImgDestiny(msg.getPathPhotoUser());
            chat.setPathImgAds(msg.getPathPhotoAds());
            chat.setiOwner((msg.getIdAnoouncer()==msg.getIdDestiny()));

            intent.putExtra("chat", chat);
            intent.putExtra("CLICKED",true);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(sup.toString().substring(0,sup.indexOf(")")+1)).setColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setContentText(msg.getUserName()+" enviou uma me");
        builder.setSmallIcon(R.drawable.ic_pop_market_express_notify);
        //builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_cred_card));

        PendingIntent p = PendingIntent.getActivity(context,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(p);

        if(msgs.size() >1) {
            NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
            String[] descs = sup.toString().split("┬");
            for (int i = 1; i < descs.length; i++) {
                style.addLine(descs[i]);
            }
            builder.setStyle(style);
        }

        Notification n = builder.build();
        n.flags = Notification.FLAG_AUTO_CANCEL ;
        n.vibrate = new long[]{150, 300, 150, 600};// {150mils (espera) , 300mils (vibração), 150mils (espera) , 300mils (vibração)}

        builder.setTicker(msg.getUserName()+" se interessou no seu produto");
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(405684, n);

        try{

            Uri soundNotify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, soundNotify);
            toque.play();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }*/}
}
