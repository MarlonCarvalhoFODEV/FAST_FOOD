package com.c.hotburguerdelivery.acitivtys.bdelivery;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.comprazencasa.colaborador.Collaborator.CollaboratorAccount;
import com.comprazencasa.colaborador.Collaborator.CollaboratorFunction;
import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.application.CustomApplication;
import com.comprazencasa.colaborador.itens.GroupOrders;
import com.comprazencasa.colaborador.itens.ITOrderBuyer;
import com.comprazencasa.colaborador.mservices.MessageManager;
import com.comprazencasa.colaborador.mservices.ServiceConnectionCZC;
import com.comprazencasa.colaborador.service_chat.UserMessage;
import com.comprazencasa.colaborador.zutils.customapplication.ResultReceiverCZC;
import com.comprazencasa.colaborador.zutils.webservice.RequestWs;
import com.comprazencasa.colaborador.zutils.webservice.WebResponseIT;
import com.comprazencasa.colaborador.zutils.webservice.WebResponses;
import com.comprazencasa.colaborador.zutils.webservice.controller.WebController;
import com.google.gson.Gson;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

public class TurnByTurnActivity extends AppCompatActivity implements OnNavigationReadyCallback
, ProgressChangeListener, WebResponseIT, ServiceConnection {

    private static String TAG = "TurnByTurnActivity";

    private NavigationView navigationView ;
    private DirectionsRoute currentRoute ;
    private NavigationMapRoute navigationMapRoute;

    private GroupOrders group ;
    private Point originPosition ;

    private ResultReceiverCZC resultReceiverCZC ;
    private MessageManager msgManager ;
    private Intent intentService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_turn_by_turn);

        resultReceiverCZC = ((CustomApplication)getApplicationContext()).getResultReceiver();
        resultReceiverCZC.setReceiverMessage(this);

        intentService = ((CustomApplication) getApplicationContext()).getIntent();
        bindService(intentService, this, Context.BIND_AUTO_CREATE);

        String json = (String) getIntent().getExtras().get("group_orders");
        group = new Gson().fromJson(json, GroupOrders.class);

        LatLng latLng = (LatLng) getIntent().getExtras().get("origin_position");
        originPosition = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
        currentRoute = (DirectionsRoute) getIntent().getExtras().get("current_route");

        navigationView = findViewById(R.id.navigation_view_turn_by_turn);
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this, setCameraPosition());
    }

    private CameraPosition setCameraPosition(){
        return new CameraPosition.Builder()
                .target(new LatLng(originPosition.latitude(), originPosition.longitude()))
                .zoom(15)
                .build();
    }

    private void showDialog(final ITOrderBuyer order){

        View view = LayoutInflater.from(this).inflate(R.layout.dg_collection, null);
        ((TextView)view.findViewById(R.id.tv_client_name_dg)).setText(order.getClientName());
        ((TextView)view.findViewById(R.id.tv_cel_name_dg)).setText(order.getCel());
        if(order.getPaymentLocal() > 0)
            ((TextView) view.findViewById(R.id.tv_value_dg)).setText("COBRAR: "+ order.getPurchaseValue().replace(".",",")+" Em "+order.getIntallment());
        ((TextView) view.findViewById(R.id.tv_street_dg)).setText(order.getStreet());
        ((TextView) view.findViewById(R.id.tv_complement_dg)).setText(order.getComplements());

        final AlertDialog dg =  new  AlertDialog.Builder(this).create();
        dg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dg.setView(view);
        dg.setCancelable(false);
        dg.show();

        ((Button) view.findViewById(R.id.btn_delivery_finalized)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dg.dismiss();
                group.remove(order);

                UserMessage msg = new UserMessage();
                msg.setIdDestinys(group.getIdUsers());
                msg.setIdOrder(group.getIdOrders());
                msg.setTypeCommunication(UserMessage.COMMUNICATION);
                msg.setMessage(ITOrderBuyer.STATUS_FINALIZED);

                requestUpdateStatusOrder(order, ITOrderBuyer.STATUS_FINALIZED);
                saveProductionDeliveryman(order);
                msgManager.sendMessage(new Gson().toJson(msg));

                for(ITOrderBuyer newO : group){

                    if(order.getLatitude() == newO.getLatitude() && order.getLongitude() == newO.getLongitude()
                    && (newO.getPurchaseValue() != order.getPurchaseValue())){

                        showDialog(newO);
                        break;
                    }

                }
            }
        });
    }

    private void requestUpdateStatusOrder(ITOrderBuyer order, String status){
        StringBuilder url = new StringBuilder();
        url.append(WebController.Orders.CONTROLLER);
        url.append(WebController.Orders.Method.UPDATE_ORDER);

        CollaboratorAccount account = ((CustomApplication)getApplicationContext()).getAccount(this);

        order.setCompanyFunction(account.getFunction().getCompanyFunction());
        order.setStatus(status);

        if(account.getFunction().getCompanyFunction().equalsIgnoreCase(CollaboratorFunction.F_BUYER)){
            order.setIdBuyer(account.getId());
            order.setIdDeliveryMan(0);
        }
        else if(account.getFunction().getCompanyFunction().equalsIgnoreCase(CollaboratorFunction.F_DELIVERYMAN)){
            order.setIdBuyer(0);
            order.setIdDeliveryMan(account.getId());
        }

        new RequestWs(this,this)
                .request(Request.Method.POST, url, order);
    }

    @Override
    public void onNavigationReady(boolean isRunning) {
            NavigationViewOptions options = NavigationViewOptions.builder()
                    .progressChangeListener(this)
                    .directionsRoute(currentRoute)
                    .shouldSimulateRoute(true)
                    .build();

            navigationView.startNavigation(options);
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        navigationView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        navigationView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationView.onDestroy();

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.N_MR1 && msgManager != null)
            unbindService(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onBackPressed() {

        if(pointRemaining == 0 && !navigationView.onBackPressed()){
            super.onBackPressed();
            navigationView.onBackPressed();
        }
        else{
            Toast.makeText(this, "Faltam "+group.size()+" entregas ainda.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProductionDeliveryman(ITOrderBuyer order){
        StringBuilder method = new StringBuilder();
        method.append(WebController.Collaborator.CONTROLLER);
        method.append(WebController.Collaborator.Method.ADD_PRODUCTION);

        order.setIdBuyer(0);

        WebResponses sendRequest = new WebResponses();
        sendRequest.setJsonObject(new Gson().toJson(new GroupOrders(order)));
        new RequestWs(this, this)
                .request(Request.Method.POST,method,sendRequest);
    }

    private int pointRemaining = -1 ;
    private int positionOrder = 0 ;

    @Override
    public void onProgressChange(Location location, RouteProgress routeProgress) {

        if(pointRemaining == -1) {
            pointRemaining = routeProgress.remainingWaypoints();
        }

        if (pointRemaining != routeProgress.remainingWaypoints() && pointRemaining >= routeProgress.remainingWaypoints()
                || (routeProgress.voiceInstruction() != null && pointRemaining == 1
                && routeProgress.voiceInstruction().getAnnouncement().equalsIgnoreCase("Você chegou ao seu destino"))){

            pointRemaining = routeProgress.remainingWaypoints();

            showDialog(group.get(positionOrder));
        }
    }

    @Override
    public void webResponse(WebResponses response) {
        switch (response.getComunication()){
            case 0:{
                Toast.makeText(this, "Não foi possível finalizar!", Toast.LENGTH_LONG).show();
                break;
            }
            case 1:{
                if(group.size() == 0) {
                    Toast.makeText(this, "Entrega finalizada com sucesso!", Toast.LENGTH_SHORT).show();
                    setResult(300);
                    finish();
                }
                else{
                    Toast.makeText(this, "Finalizou de "+group.get(0).getClientName(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void receiverMessage(UserMessage msg) {

    }

    @Override
    public void interfaceController(WebResponses response) {

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        ServiceConnectionCZC.InfoController t = (ServiceConnectionCZC.InfoController) service;
        msgManager = t.getMessageManager();
        if (getIntent().hasExtra("CLICKED")) {
            //msgManager.clearMessageFromID(chat.getIdProduct());
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
