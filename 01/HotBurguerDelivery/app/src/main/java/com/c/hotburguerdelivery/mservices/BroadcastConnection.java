package com.c.hotburguerdelivery.mservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastConnection extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, ServiceConnectionCZC.class);
        context.startService(intent);
    }
}
