package com.c.hotburguerdelivery.zutils.customapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.comprazencasa.colaborador.service_chat.UserMessage;
import com.comprazencasa.colaborador.zutils.webservice.WebResponseIT;

public class ResultReceiverCZC extends ResultReceiver {
    private WebResponseIT receiverMessage ;

    public ResultReceiverCZC(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        UserMessage msg = (UserMessage) resultData.getSerializable("msg");

        if(receiverMessage!=null) receiverMessage.receiverMessage(msg);
        super.onReceiveResult(resultCode, resultData);
    }

    public void setReceiverMessage(WebResponseIT receiverMessage) {
        this.receiverMessage = receiverMessage;
    }
}
