package br.com.hotbmanager.adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbmanager.R;
import br.com.hotbmanager.application.CustomApplication;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;
import br.com.hotbmanager.utils.EndPointMapping;

public class AdapterHours extends RecyclerView.Adapter<AdapterHours.Holder> {

    private Context context;
    private ArrayList<JSONObject> itens;

    public AdapterHours(Context context) {
        this.context = context;
        this.itens = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.it_opening_hour, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {
            holder.bindValues(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void clear() {
        itens.clear();
        notifyDataSetChanged();
    }

    public void addItem(JSONObject obj) {
        itens.add(obj);
        notifyItemInserted(itens.size() - 1);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
            View.OnFocusChangeListener {

        private ImageView ivIconExpandable;
        private TextView tvDay, tvHours;

        private ConstraintLayout baseAction;
        private LinearLayout expandableLayout;
        private EditText etOpeningHour, etCloseHour;
        private Switch enableDay;

        private JSONObject obj;
        private boolean expandable = false;
        private boolean backValue = false;

        private TimePickerDialog dialogOpened;

        public Holder(@NonNull View v) {
            super(v);

            baseAction = v.findViewById(R.id.cl_base_action_it_opening_hour);

            ivIconExpandable = v.findViewById(R.id.iv_icon_it_opening_hour);
            tvDay = v.findViewById(R.id.tv_day_opening_hour);

            expandableLayout = v.findViewById(R.id.cl_expandable_it_opening_hour);

            tvHours = v.findViewById(R.id.tv_timer_opening_hours);
            etOpeningHour = v.findViewById(R.id.et_opening_hour);
            etCloseHour = v.findViewById(R.id.et_closing_hour);

            enableDay = v.findViewById(R.id.swt_opening);
            enableDay.setOnCheckedChangeListener(this);
        }

        public void bindValues(int position) throws JSONException {
            obj = itens.get(position);

            if (!obj.has("exp"))
                obj.put("exp", expandable);

            String day = obj.has("day") ? obj.getString("day") : "(não definido)";
            String openinHour = obj.has("opening_hour") ? obj.getString("opening_hour") : "(não definido)";
            String closeHour = obj.has("closing_hour") ? obj.getString("closing_hour") : "(não definido)";
            boolean isOperating = obj.has("is_operating") ? obj.getBoolean("is_operating") : false;

            ivIconExpandable.setImageResource(expandable ? R.drawable.ic_collapsible : R.drawable.ic_expandable);

            tvDay.setText(day);
            tvHours.setText(openinHour + " às " + closeHour);

            etOpeningHour.clearFocus();
            etOpeningHour.setText(openinHour);
            etOpeningHour.setOnClickListener(this);

            etCloseHour.clearFocus();
            etCloseHour.setText(closeHour);
            etCloseHour.setOnClickListener(this);

            baseAction.setOnClickListener(this);
            expandableLayout.setVisibility(expandable ? View.VISIBLE : View.GONE);

            //enableDay.setChecked(obj.getBoolean("opened"));
            enableDay.setChecked(isOperating);
        }

        private synchronized void updateDayOperation(boolean opening, int idDay) {
            try {

                final HotManagerAccount ac = CustomApplication.getAccount();

                String openingH = etOpeningHour.getText().toString();
                //openingH = openingH.substring(openingH.length()-3);

                String closingH = etCloseHour.getText().toString();
                //closingH = closingH.substring(openingH.length()-3);

                JSONObject json = new JSONObject();
                json.put("1", ac.getId());
                json.put("2", ac.getIdDevice());
                json.put("3", idDay);
                json.put("4", opening);
                json.put("5", openingH);
                json.put("6", closingH);

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("updateDayOperation()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(context.getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.Manager.CONTROLLER);
                requestServer.setFunction(EndPointMapping.Manager.Functions.UPDATE_DAY_OPENING);
                requestServer.setObjectTransport(json);
                requestServer.request(context, new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {

                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {
                            try {

                                JSONObject ob = new JSONObject(object.toString());
                                ob = ob.getJSONObject("map");

                                boolean defined = ob.has("result") ? ob.getBoolean("result") : false;

                                if (ob == null || !defined) {
                                    enableDay.setChecked(backValue);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                enableDay.setChecked(false);
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
                enableDay.setChecked(false);
                Log.i("openingDayWeb", "JSONException: " + opening);
            }
        }

        private void openTimerPicker(final boolean openingHour, final EditText et) {

            if (dialogOpened != null)
                dialogOpened.cancel();

            dialogOpened = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();

                        StringBuilder sb = new StringBuilder();
                        sb.append(hour);
                        sb.append(":");
                        sb.append(minute);

                        et.setText(sb.toString());

                        try {
                            obj.put(openingHour ? "opening_hour" : "closing_hour", sb.toString());

                            updateDayOperation(enableDay.isChecked(), obj.getInt("day_n"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 0, 0, true);

            dialogOpened.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                }
            });
            dialogOpened.show();
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.cl_base_action_it_opening_hour: {
                    try {
                        expandable = !obj.getBoolean("exp");
                        obj.put("exp", expandable);
                        obj.put("opened", enableDay.isChecked());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    notifyItemChanged(getLayoutPosition());
                    break;
                }
                case (R.id.et_opening_hour): {
                    openTimerPicker(true,(EditText) view);
                    break;
                }
                case R.id.et_closing_hour: {
                    openTimerPicker(false, (EditText) view);
                    break;
                }
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean opening) {
            try {
                backValue = !opening;
                updateDayOperation(opening, obj.has("day_n") ? obj.getInt("day_n") : -1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFocusChange(View view, boolean b) {

            if (b && expandable) {
                //openTimerPicker((EditText) view);
            }
        }
    }
}
