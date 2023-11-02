package br.com.comprazencasa.controllers;

import br.com.comprazencasa.database.DaoPayment;
import br.com.comprazencasa.market.Pedido;
import com.mercadopago.exceptions.MPException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

@RestController
@RequestMapping("webhook")
public class WebHookManager {

    @PostMapping(value = "/notification-mercado-pago")
    public HttpStatus receiverNotification(HttpServletRequest request) throws MPException {

        new DaoPayment().receiverNotification(request.getParameter("id"));

        return ResponseEntity.ok().build().getStatusCode();
    }

}
