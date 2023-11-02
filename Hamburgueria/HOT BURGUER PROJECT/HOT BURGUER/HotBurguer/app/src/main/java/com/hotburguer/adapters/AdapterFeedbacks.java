package com.hotburguer.adapters;

import android.content.Context;
import android.os.Handler;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hotburguer.R;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.DraweeListener;
import com.hotburguer.itens.HotUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import apis.card.CardManager;
import apis.userbuyer.BCard;

public class AdapterFeedbacks extends RecyclerView.Adapter<AdapterFeedbacks.Holder> {

    private Context context;
    private final ArrayList<JSONObject> itens;

    public AdapterFeedbacks(Context context) {
        this.context = context;
        this.itens = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.it_feedback, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {
            holder.bindValues(itens.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public synchronized void add(JSONObject feedback) {
        itens.add(feedback);
        notifyItemInserted(itens.size() - 1);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SimpleDraweeView draweeView;
        private TextView tvUserName, tvFeedback;
        private Button btnLeadMore;
        private boolean showLead ;

        public Holder(@NonNull View v) {
            super(v);

            draweeView = v.findViewById(R.id.sdv_feedback_product);
            tvUserName = v.findViewById(R.id.tv_user_name_feedback_product);
            tvFeedback = v.findViewById(R.id.tv_feedback_product);
            btnLeadMore = v.findViewById(R.id.btn_lead_more);
            btnLeadMore.setOnClickListener(this);
        }

        private void setImageUserProfile(String utlPhoto) {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(utlPhoto)
                    //.setControllerListener(listener)
                    .setTapToRetryEnabled(true)
                    .setOldController(draweeView.getController())
                    .build();

            draweeView.setController(controller);
        }

        public void bindValues(JSONObject obfeedback) throws JSONException {
            int idUser = obfeedback.has("id_user") ? obfeedback.getInt("id_user") : 0;
            String userName = obfeedback.has("name") ? obfeedback.getString("name") : "(sem nome)";
            String feedback = obfeedback.has("feedback") ? obfeedback.getString("feedback") : "(sem comentário)";
            String urlPhoto = obfeedback.has("photo") ? obfeedback.getString("photo") : "(sem comentário)";

            setImageUserProfile(urlPhoto);
            tvUserName.setText(userName);
            tvFeedback.setText(feedback);

            if (tvFeedback.getLineCount() >= 2 || tvFeedback.getText().toString().split("\n").length >= 2) {
                btnLeadMore.setVisibility(View.VISIBLE);
                btnLeadMore.setTag(false);
            } else
                btnLeadMore.setVisibility(View.GONE);
        }

        private void leadMore() {
            if (!showLead) {
                showLead = true ;
                tvFeedback.setMaxLines(10);
                btnLeadMore.setText("LER MENOS");
            } else {
                showLead = false ;
                tvFeedback.setMaxLines(2);
                btnLeadMore.setText("LER MAIS");
            }
        }

        @Override
        public void onClick(View view) {
            leadMore();
        }
    }
}
