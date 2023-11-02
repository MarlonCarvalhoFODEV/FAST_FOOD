package com.c.hotburguerdelivery.zutils.webservice;


import com.comprazencasa.colaborador.service_chat.UserMessage;

public interface WebResponseIT {

    public void webResponse(WebResponses response);

    public void receiverMessage(UserMessage msg);

    public void interfaceController(WebResponses response);

}
