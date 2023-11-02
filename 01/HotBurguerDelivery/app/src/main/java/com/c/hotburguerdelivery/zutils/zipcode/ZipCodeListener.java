package com.c.hotburguerdelivery.zutils.zipcode;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.comprazencasa.colaborador.zutils.webservice.volleycustom.JsonRequest;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

public class ZipCodeListener implements TextWatcher {

    private Context context ;
    private String zipCode ;
    private ProgressBar progress;
    private ZipIT zipController ;

    public ZipCodeListener(Context context, ZipIT zipController){
        this.context = context ;
        this.zipController = zipController ;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {

        zipCode = s.toString();

        if(s.length() == 8){
            new AddressRequest(zipController).execute();
        }
    }

    public class AddressRequest extends AsyncTask<Void, Void, Address > {

        private WeakReference<Activity> activity ;

        public AddressRequest(ZipIT zipController){
            this.activity = new WeakReference<>((Activity) context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress = new ProgressBar(context);
            //progress.setIndeterminate(true);
            //progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected Address doInBackground(Void... params) {

            String jsonString = JsonRequest.request("https://viacep.com.br/ws/"+zipCode+"/json/");
            return new Gson().fromJson(jsonString, Address.class) ;
        }

        @Override
        protected void onPostExecute(Address address) {
            super.onPostExecute(address);

            if(address != null){
                zipController.responseAddress(address);
                progress.setVisibility(View.GONE);
            }else{
                Toast.makeText(context, "CEP Inválido\nverifique-o e tente novamenete", Toast.LENGTH_SHORT).show();
            }
        }
    }
}