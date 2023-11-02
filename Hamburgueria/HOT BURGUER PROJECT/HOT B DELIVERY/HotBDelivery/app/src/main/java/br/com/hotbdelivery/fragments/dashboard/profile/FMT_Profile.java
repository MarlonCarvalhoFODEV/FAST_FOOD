package br.com.hotbdelivery.fragments.dashboard.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import apis.cache.CacheAPI;
import apis.finance.FinanceAPI;
import apis.image.APIImage;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.userbuyer.BAccountBank;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.fragments.dashboard.profile.listeners.BankTypeListener;
import br.com.hotbdelivery.fragments.dialogs.DialogInformative;
import br.com.hotbdelivery.fragments.register.PhotoCompat;
import br.com.hotbdelivery.utils.EndPointMapping;
import br.com.hotbdelivery.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FMT_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FMT_Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Controller controller ;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FMT_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FMT_Configuration.
     */
    // TODO: Rename and change types and number of parameters
    public static FMT_Profile newInstance(String param1, String param2) {
        FMT_Profile fragment = new FMT_Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fmt_dash_profile, container, false);

        controller = new Controller(view);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == controller.REQUEST_PHOTO && data != null && data.getData() != null) {
            Uri uri = data.getData();

            final Bitmap bitmap = APIImage.readBitmap(getContext(), uri, 3);
            //controller.civPhotoProfile.setImageBitmap(bitmap);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, bout);

                    controller.photoSelected = (bout.toByteArray());
                }
            }).start();
        }
    }

    public class Controller implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

        private final TextView tvCountDelivery, tvCountReceivable, tvTotalAvailable;//tvMsgLoading;//tvUserName
        //private final RatingBar userEvaluation ;
        private final EditText etCel, etEmail, etAgency, etAccount, etAccountDigit ;
        private final AutoCompleteTextView etBankName ;

        private final Spinner spTypeAccountBank ;
        private final CircleImageView civCountDelivery, civReceivable, civAvailable ;//civPhotoProfile
        private final Button  btnUpdateProfile;//btnEditProfile,;
        //private final LinearLayout llProgress ;

        private SwipeRefreshLayout swip ;

        private byte[] photoSelected ;

        private boolean unlockPanel = false ;
        private static final int REQUEST_PHOTO = 467 ;

        public Controller(View view){

            //civPhotoProfile = view.findViewById(R.id.civ_photo_profile_config);
            civCountDelivery = view.findViewById(R.id.civ_back_1_config);
            civReceivable = view.findViewById(R.id.civ_back_3_config);
            civAvailable = view.findViewById(R.id.civ_back_2_config);

            //tvUserName = view.findViewById(R.id.tv_user_name_config);
            tvCountDelivery = view.findViewById(R.id.tv_count_delivered_config);
            tvCountReceivable = view.findViewById(R.id.tv_count_receivable_config);
            tvTotalAvailable = view.findViewById(R.id.tv_count_available_config);
            //tvMsgLoading = view.findViewById(R.id.tv_message_loading_config);

           // btnEditProfile = view.findViewById(R.id.btn_edit_profile_config);
            btnUpdateProfile = view.findViewById(R.id.btn_update_profile_config);
            //userEvaluation = view.findViewById(R.id.rb_user_evaluation_config);

            etCel = view.findViewById(R.id.et_cel_config);
            etEmail = view.findViewById(R.id.et_email_config);
            etAgency = view.findViewById(R.id.et_agency_config);
            etAccount = view.findViewById(R.id.et_account_bank_config);
            etAccountDigit = view.findViewById(R.id.et_account_bank_digit_config);
            etBankName = view.findViewById(R.id.et_bank_config);

            spTypeAccountBank = view.findViewById(R.id.sp_type_account_bank_config);
            swip = view.findViewById(R.id.swip_profile);

            //llProgress = view.findViewById(R.id.ll_progress_profile_config);

            setDataAllComponents();
        }

        private final void showProgress(boolean show){
            //llProgress.setVisibility(show?View.VISIBLE:View.GONE);
            swip.setRefreshing(show);
        }

        private final void setDataAllComponents(){
            HotDeliveryMan ac = CustomApplication.getAccount();

            //setImageFromURL(ac.getId(), civPhotoProfile);

            //tvUserName.setText(ac.getName());
            etCel.setText(ac.getContact().getPhone().getFullNumber());
            etEmail.setText(ac.getEmail());
            etAgency.setText(ac.getBankAccount().getAgency());
            etAccount.setText(ac.getBankAccount().getAccount());
            etAccountDigit.setText(ac.getBankAccount().getAccountDv());

            //userEvaluation.setRating(0.0f);

            spTypeAccountBank.setOnItemSelectedListener(new BankTypeListener());

           // btnEditProfile.setOnClickListener(this);
            btnUpdateProfile.setOnClickListener(this);
            swip.setOnRefreshListener(this);
            //civPhotoProfile.setOnClickListener(this);

            String[] banks = getContext().getResources().getStringArray(R.array.banks);
            ArrayAdapter<String> adpBanks = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, banks);
            etBankName.setAdapter(adpBanks);
            etBankName.setText(ac.getBankAccount().getName());

            String cel = ac.getContact().getPhone().getFullNumber();
            if(cel.length() == 11)
                Utils.FieldText.setMaskInEditText(etCel, "(NN) NNNNN-NNNN");
            else
                Utils.FieldText.setMaskInEditText(etCel, "+NN(NN) NNNNN-NNNN");
            Utils.FieldText.setMaskInEditText(etAgency, "NNNNN");
            Utils.FieldText.setMaskInEditText(etAccount, "NNNNNNNNNNNNN");
            Utils.FieldText.setMaskInEditText(etAccountDigit, "NN");

            loadDatas();
        }

        private final void setImageFromURL(int idUser, final ImageView imgView) {
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.domain_connection));
            sb.append(EndPointMapping.DeliveryMan.CONTROLLER);
            sb.append(EndPointMapping.DeliveryMan.Functions.GET_IMAGE_PROFILE);
            sb.append("/");
            sb.append(idUser);

            Glide.with(getContext())
                    .asBitmap()
                    .load(sb.toString())
                    .placeholder(R.drawable.ic_img_fail)
                    .error(R.drawable.ic_img_fail)
                    .timeout(6000)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(imgView);
        }

        private final void loadDatas() {

            HotDeliveryMan ac = CustomApplication.getAccount();

            JSONObject data = new JSONObject();
            try {
                data.put("id", ac.getId());
                data.put("id_dev",ac.getIdDevice());
                data.put("id_rec", ac.getIdRecipient());

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("loadDatas()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
                requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.GET_ACHIEVEMENTS);
                requestServer.setObjectTransport(data);
                requestServer.request(getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        showProgress(loading);
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {

                            JSONObject response = null ;
                            try {

                                response = new JSONObject(object.toString());
                                response = response.getJSONObject("map");

                                boolean results = response.getBoolean("result");
                                if(results){
                                    int totalRunning = response.getInt("running");
                                    double receivable = FinanceAPI.format(response.getDouble("receivable")/100,2);
                                    double available = FinanceAPI.format(response.getDouble("available"),2);
                                    double transferred = FinanceAPI.format(response.getDouble("transferred"),2);

                                    tvCountDelivery.setText(String.valueOf(totalRunning));
                                    tvCountReceivable.setText(String.valueOf("R$ "+receivable).replace(".",","));
                                    tvTotalAvailable.setText(String.valueOf("R$ "+available).replace(".",","));

                                    if(available > 0){
                                        civAvailable.setImageResource(R.color.success);
                                        openCountAvailable(available);
                                    }

                                    if(receivable > 0){
                                        civReceivable.setImageResource(R.color.colorRatingGold);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Formato Incorreto", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Nenhum resultado retornado pelo servidor", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Erro de conexão com o servidor", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private final void openPhotoSelector(int requestCode){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, requestCode);
        }

        private final void openCountDelivery(){}

        private final void openCountReceivable(){}

        private final void openCountAvailable(final double amount){

            BAccountBank bank = CustomApplication.getAccount().getBankAccount();

            StringBuilder sb = new StringBuilder();
            sb.append("Deseja sacar o valor para ");
            sb.append(bank.getName());
            sb.append("\n");
            sb.append("Agencia - ");
            sb.append(bank.getAgency()+bank.getAgencyDv());
            sb.append("\n");
            sb.append("Conta - ");
            sb.append(bank.getAccount());
            sb.append("-");
            sb.append(bank.getAccountDv());

            final DialogInformative dialog = new DialogInformative(getContext());
            dialog.setTitle("Disponível Para Saque R$ "+amount);
            dialog.setMessage(sb.toString());
            dialog.setPadding(16*2,true);
            dialog.addButton("Não", Color.TRANSPARENT, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.addButton("Sim", Color.WHITE, R.drawable.rounded_button_confirm, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    transferBalance(amount);
                }
            });
        }

        private final void sendPhotoProfile() {
            showProgress(true);

            HotDeliveryMan account = CustomApplication.getAccount();

            byte[] profile = controller.getPhotoSelected();

            PhotoCompat photoCompat = new PhotoCompat();
            photoCompat.setId(account.getId());
            photoCompat.setProfile(profile);

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("sendPhotoProfile()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
            //requestServer.setOtherCryptographyRSA(getString(R.string.rsabig));
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getString(R.string.domain_connection));
            requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
            requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.REGISTER_PHOTO);
            requestServer.setObjectTransport(photoCompat);
            requestServer.request(getContext(), new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    showProgress(loading);
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        JSONObject response = null;
                        try {
                            response = new JSONObject(object.toString());
                            response = response.getJSONObject("map");

                            String result = response.getString("result");

                            if (result != null && result.equals("saved_succefully")) {

                            } else if (result != null && result.equals("saved_failed")) {
                                Toast.makeText(getContext(), "Erro ao salvar photos", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showProgress(false);
                        }
                    } else {
                        showProgress(false);
                        Toast.makeText(getContext(), "Nenhum Valor Retornado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    showProgress(false);
                }
            });
        }

        private final byte[] getPhotoSelected() {
            return photoSelected ;
        }

        public final void lockUnlockPanelData(boolean lock){
            //civPhotoProfile.setEnabled(lock);
            civCountDelivery.setEnabled(lock);
            civReceivable.setEnabled(lock);
            civAvailable.setEnabled(lock);

            //tvUserName.setEnabled(lock);
            tvCountDelivery.setEnabled(lock);
            tvCountReceivable.setEnabled(lock);
            tvTotalAvailable.setEnabled(lock);

            //btnEditProfile.setEnabled(lock);
            btnUpdateProfile.setEnabled(lock);
            //userEvaluation.setEnabled(lock);

            etCel.setEnabled(lock);
            //etEmail.setEnabled(lock);
            etAgency.setEnabled(lock);
            etAccount.setEnabled(lock);
            etAccountDigit.setEnabled(lock);
            etBankName.setEnabled(lock);

            spTypeAccountBank.setEnabled(lock);
            //llProgress.setEnabled(lock);

            unlockPanel=lock;
        }

        private void updateProfile(){
            sendPhotoProfile();
        }

        private final void transferBalance(double amount){
            HotDeliveryMan ac = CustomApplication.getAccount();

            JSONObject data = new JSONObject();
            try {
                data.put("id", ac.getId());
                data.put("id_dev",ac.getIdDevice());
                data.put("id_rec", ac.getIdRecipient());
                data.put("amount", amount);

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("transferBalance()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
                requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.TRANSFER_BALANCE);
                requestServer.setObjectTransport(data);
                requestServer.request(getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        showProgress(loading);
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {

                            JSONObject response = null ;
                            try {

                                response = new JSONObject(object.toString());
                                response = response.getJSONObject("map");

                                boolean results = response.getBoolean("result");
                                if(results){
                                    Toast.makeText(getContext(), "Solicitado com Sucesso!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Formato Incorreto", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Nenhum resultado retornado pelo servidor", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Erro de conexão com o servidor", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                /*case R.id.civ_photo_profile_config:{
                    openPhotoSelector(REQUEST_PHOTO);
                    break;
                }*/
                /*case R.id.btn_edit_profile_config:{
                    lockUnlockPanelData(!unlockPanel);
                    break;
                }*/
                case R.id.btn_update_profile_config:{

                    break;
                }
            }
        }

        @Override
        public void onRefresh() {
            loadDatas();
        }
    }
}