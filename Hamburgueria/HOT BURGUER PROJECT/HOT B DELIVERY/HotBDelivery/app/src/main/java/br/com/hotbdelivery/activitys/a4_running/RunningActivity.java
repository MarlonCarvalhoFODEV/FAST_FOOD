package br.com.hotbdelivery.activitys.a4_running;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import apis.finance.FinanceAPI;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.location.LAddress;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.fragments.dialogs.DialogInformative;
import br.com.hotbdelivery.itens.HotDelivery;
import br.com.hotbdelivery.itens.HotRunning;
import br.com.hotbdelivery.utils.CacheApp;
import br.com.hotbdelivery.utils.EndPointMapping;

public class RunningActivity extends AppCompatActivity implements OnNavigationReadyCallback, NavigationListener
        , ProgressChangeListener, View.OnLongClickListener {

    private NavigationView navigationView;

    private FrameLayout flProgress;

    private HotRunning running;
    private ArrayList<LAddress> wayPoints;
    private DirectionsRoute currentRoute;

    private int pointRemaining = -1;
    private int positionDelivery = -1;

    public static final int RESULT = 325;
    public static final String TAG = "JFJDJSJJJEJJJELL";

    private List<Integer> delivereds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_running);

        running = CacheApp.getRunning(this);
        currentRoute = (DirectionsRoute) getIntent().getExtras().get("route");

        wayPoints = new ArrayList<>();
        wayPoints.addAll(running.getAddresses());

        flProgress = findViewById(R.id.fl_progress_running);

        navigationView = findViewById(R.id.navigation_view_turn_by_turn);
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this, setCameraPosition());

        final FloatingActionButton fabNotify = findViewById(R.id.fab_notification);
        fabNotify.setOnLongClickListener(this);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fabNotify.getLayoutParams();
        params.anchorGravity = Gravity.BOTTOM;
        params.setAnchorId(R.id.feedbackFab);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                View fabFeedback = navigationView.findViewById(R.id.feedbackFab);
                if (fabFeedback.getVisibility() == View.VISIBLE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fabNotify.setVisibility(View.VISIBLE);
                        }
                    });
                    cancel();
                }
            }
        }, 1000, 1000);
    }

    private void showLoading(boolean show) {
        flProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private CameraPosition setCameraPosition() {
        return new CameraPosition.Builder()
                .target(new com.mapbox.mapboxsdk.geometry.LatLng(running.getOriginLat(), running.getOriginLng()))
                .zoom(15)
                .build();
    }

    private final void iArrivedAtTheCollectionPoint(final int[] idAllOrders) {
        try {
            HotDeliveryMan account = CustomApplication.getAccount();

            JSONObject json = new JSONObject();
            json.put("id", account.getId());
            json.put("id_dev", account.getIdDevice());
            json.put("id_recipient", account.getIdRecipient());
            json.put("id_all_orders", idAllOrders);
            json.put("name", account.getName());

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("iArrivedAtTheCollectionPoint()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getString(R.string.domain_connection));
            requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
            requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.ARRIVED_COLLECT_POINT);
            requestServer.setObjectTransport(json);
            requestServer.request(this, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    showLoading(loading);
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        try {
                            JSONObject ob = new JSONObject(object.toString());
                            ob = ob.getJSONObject("map");

                            boolean result = ob.getBoolean("result");
                            if (result) {
                                Toast.makeText(RunningActivity.this, "Coleta Notificada!!!", Toast.LENGTH_LONG).show();

                                StringBuilder sb = new StringBuilder();

                                for (int i = 0; i < idAllOrders.length; i++) {
                                    sb.append("pedido: ");
                                    sb.append(idAllOrders[i]);
                                    sb.append("\n");
                                }

                                final DialogInformative dialog = new DialogInformative(RunningActivity.this);
                                dialog.setCancelable(false);
                                dialog.setCancelableTouchOutside(false);
                                dialog.setTitle("Peguei os Pedidos");
                                dialog.setMessage(sb.toString());
                                dialog.addButton("SIM", Color.WHITE,
                                        getResources().getDrawable(R.drawable.rounded_button_confirm),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                imOnMyWay(idAllOrders);
                                                dialog.dismiss();
                                            }
                                        });
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final void imOnMyWay(int[] idOrders) {
        try {

            HotDeliveryMan account = CustomApplication.getAccount();

            JSONObject json = new JSONObject();
            json.put("id", account.getId());
            json.put("id_dev", account.getIdDevice());
            json.put("id_recipient", account.getIdRecipient());
            json.put("id_orders", idOrders);

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("imOnMyWay()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getString(R.string.domain_connection));
            requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
            requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.IM_ON_MY_WAY);
            requestServer.setObjectTransport(json);
            requestServer.request(this, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    showLoading(loading);
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        try {
                            JSONObject ob = new JSONObject(object.toString());
                            boolean result = ob.getBoolean("result");
                            if (result)
                                Toast.makeText(RunningActivity.this, "A caminho...", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final void iArrivedAtThePlace(final int idOrder) {
        try {
            HotDeliveryMan account = CustomApplication.getAccount();

            JSONObject json = new JSONObject();
            json.put("id", account.getId());
            json.put("id_dev", account.getIdDevice());
            json.put("id_recipient", account.getIdRecipient());
            json.put("id_order", idOrder);

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("iArrivedAtThePlace()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getString(R.string.domain_connection));
            requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
            requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.I_ARRIVED_AT_THE_PLACE);
            requestServer.setObjectTransport(json);
            requestServer.request(this, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    showLoading(loading);
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        try {
                            JSONObject ob = new JSONObject(object.toString());
                            ob = ob.getJSONObject("map");

                            boolean result = ob.getBoolean("result");
                            if (result) {
                                Toast.makeText(RunningActivity.this, "Cliente Notificado!!!", Toast.LENGTH_SHORT).show();

                                final DialogInformative dialog = new DialogInformative(RunningActivity.this);
                                dialog.setTitle("Finalizar Entrega?");
                                dialog.setCancelableTouchOutside(false);
                                dialog.setCancelable(false);
                                dialog.setMessage("pressione para finalizar...");
                                dialog.setPadding(16 * 2, false);
                                dialog.addButton("Finalizar", Color.WHITE, getResources().getDrawable(R.drawable.rounded_button_confirm), new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        dialog.dismiss();
                                        completeDelivery(idOrder);
                                        return false;
                                    }
                                });
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final void completeDelivery(int idOrder) {
        try {
            HotDeliveryMan account = CustomApplication.getAccount();

            JSONObject json = new JSONObject();
            json.put("id", account.getId());
            json.put("id_dev", account.getIdDevice());
            json.put("id_recipient", account.getIdRecipient());
            json.put("id_order", idOrder);

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("completeDelivery()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getString(R.string.domain_connection));
            requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
            requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.COMPLETE_DELIVERY);
            requestServer.setObjectTransport(json);
            requestServer.request(this, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    showLoading(loading);
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        pointRemaining--;
                        try {
                            JSONObject ob = new JSONObject(object.toString());
                            ob = ob.getJSONObject("map");

                            boolean result = ob.getBoolean("result");
                            double receivable = ob.getDouble("receivable");

                            if (result) {
                                Toast.makeText(RunningActivity.this, "Entrega Finalizada!!!", Toast.LENGTH_SHORT).show();

                                if (positionDelivery == running.getDeliverys().size() - 1) {
                                    Intent intent = new Intent();
                                    intent.putExtra("amount",
                                            FinanceAPI.format(receivable * running.getDeliverys().size() - 1, 2));

                                    setResult(RESULT, intent);
                                    finish();
                                } else {
                                    // Show custom toast

                                    delivereds.add(running.getDeliverys().get(positionDelivery).getIdOrder());

                                    //CacheApp.saveRunning(RunningActivity.this, delivery);
                                    showCustomToast(receivable);
                                    // execute sound money
                                }
                            } else {
                                Toast.makeText(RunningActivity.this, "Error Ao Finalizar", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final int[] removerElement(int[] array, int position) {
        int[] neo = new int[array.length - 1];

        boolean otherPosition = false;
        for (int i = 0; i < array.length; i++) {
            int add = array[i];

            System.out.println(add);

            if (i != position) {
                if (!otherPosition)
                    neo[i] = add;
                else
                    neo[i - 1] = add;
            } else
                otherPosition = true;
        }
        array = neo;
        return array;
    }

    private final void showDialogNotification(final int idOrder) {

        final boolean collectionPoint = positionDelivery == 0;
        final boolean deliveryPoint = positionDelivery > 0;
        final boolean finalizedDelivery = pointRemaining == wayPoints.size() - 1;

        String title = "Notificar Chegada";
        String address = wayPoints.get(positionDelivery).getfull();
        String toDestiny = collectionPoint ? "Ao ponto de Coleta " : "Ao cliente ";

        HotDelivery delivery = running.getDeliverys().get(idOrder);

        StringBuilder sb = new StringBuilder();
        sb.append(toDestiny);

        if (!collectionPoint) {
            sb.append(delivery.getClientName().split(" ")[0]);
            sb.append("\npedido: ");
            sb.append(delivery.getIdOrder());
            sb.append("\nendereço: ");
            sb.append(address);
        } else {
            sb.append(delivery.getClientName());
        }


        final DialogInformative dialog = new DialogInformative(this);
        dialog.setTitle(title);
        dialog.setMessage(sb.toString());
        dialog.getTvMessage().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        dialog.getTvMessage().setGravity(Gravity.LEFT);
        dialog.setCancelable(true);
        dialog.setCancelableTouchOutside(true);
        dialog.addButton("Não", Color.TRANSPARENT, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.addButton("Sim", Color.WHITE, getResources().getDrawable(R.drawable.rounded_button_confirm), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (collectionPoint) {
                    iArrivedAtTheCollectionPoint(running.getIdOrders());
                } else if (deliveryPoint) {
                    iArrivedAtThePlace(idOrder);
                } /*else if (finalizedDelivery) {
                    completeDelivery(idOrder);
                }*/

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private final void showCustomToast(double receivable) {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_toast, null);
        TextView tvMessage = view.findViewById(R.id.tv_achievements);

        StringBuilder sb = new StringBuilder();
        sb.append("Você Recebeu\n");
        sb.append("+ R$ ");
        sb.append(receivable);

        tvMessage.setText(sb.toString());

        Toast toast = new Toast(this);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setMargin(0, 32);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();

        //declare variables
        MediaPlayer player = MediaPlayer.create(this, R.raw.cash_register);
        player.start();
    }

    @Override
    public void onProgressChange(@NotNull Location location, @NotNull RouteProgress routeProgress) {
        if (pointRemaining == -1) {
            pointRemaining = routeProgress.remainingWaypoints();
        }

        if (pointRemaining != routeProgress.remainingWaypoints() && pointRemaining >= routeProgress.remainingWaypoints()
                || (routeProgress.voiceInstruction() != null && pointRemaining == 1
                && routeProgress.voiceInstruction().getAnnouncement().equalsIgnoreCase("Você chegou ao seu destino"))) {

            pointRemaining = routeProgress.remainingWaypoints();
            positionDelivery++;

            int id = wayPoints.get(positionDelivery).getId();
            showDialogNotification(id);
        }
    }

    @Override
    public void onBackPressed() {
        if (pointRemaining == 0 && !navigationView.onBackPressed()) {
            super.onBackPressed();

            if (navigationView != null)
                navigationView.onBackPressed();
        } else {
            Toast.makeText(this, "Faltam " + wayPoints.size() + " entregas ainda.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNavigationReady(boolean isRunning) {
        NavigationViewOptions options = NavigationViewOptions.builder()
                .progressChangeListener(this)
                .directionsRoute(currentRoute)
                //.shouldSimulateRoute(true)
                .build();

        navigationView.startNavigation(options);
    }

    @Override
    public void onCancelNavigation() {
        navigationView.stopNavigation();
        finish();
    }

    @Override
    public void onNavigationFinished() {
        finish();
    }

    @Override
    public void onNavigationRunning() {

    }

    @Override
    public void onLowMemory() {
        navigationView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        if (navigationView != null)
            navigationView.onSaveInstanceState(outState);

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (navigationView != null)
            navigationView.onRestoreInstanceState(savedInstanceState);

        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onStart() {
        if (navigationView != null)
            navigationView.onStart();

        super.onStart();
    }

    @Override
    protected void onPause() {
        if (navigationView != null)
            navigationView.onPause();

        super.onPause();
    }

    @Override
    protected void onResume() {

        if (navigationView != null)
            navigationView.onResume();

        super.onResume();
    }

    @Override
    protected void onStop() {
        if (navigationView != null)
            navigationView.onStop();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (navigationView != null)
            navigationView.onDestroy();

        super.onDestroy();
    }

    @Override
    public boolean onLongClick(View view) {
        if (positionDelivery < running.getDeliverys().size()) {
            int actualOrder = running.getAddresses().get(positionDelivery).getId();
            if (!delivereds.contains(actualOrder))
                showDialogNotification(running.getAddresses().get(positionDelivery).getId());
            else if(positionDelivery+1 < running.getAddresses().size()-1)
                showDialogNotification(running.getAddresses().get(positionDelivery+1).getId());
        }
        return false;
    }
}
