package com.hotdelivery.fragments.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.hotdelivery.R;
import com.hotdelivery.activitys.a2_register.RegisterInterface;
import com.hotdelivery.itens.deliveryman.HotDeliveryMan;
import com.hotdelivery.utils.EndPointMapping;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import apis.cache.CacheAPI;
import apis.document.APIDocument;
import apis.image.APIImage;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;

import com.hotdelivery.aplication.CustomApplication;
import com.hotdelivery.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import apis.userbuyer.BAccountBank;
import de.hdodenhof.circleimageview.CircleImageView;
import ernestoyaquello.com.verticalstepperform.Step;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class FMT_CompleteRegister extends Fragment {

    private Controller controller;
    private RegisterInterface regInterface;
    private static final int PICKPHOTO_REQUEST_CODE = 54766;

    public FMT_CompleteRegister(RegisterInterface exRegInterface) {
        regInterface = exRegInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE);
        View view = inflater.inflate(R.layout.fmt_reg_2_complete, null);
        controller = new Controller(view);

        return view;
    }

    public Controller getController() {
        return controller;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKPHOTO_REQUEST_CODE && data != null && data.getData() != null) {
            Uri uri = data.getData();

            final Bitmap bitmap = APIImage.readBitmap(getContext(), uri, 3);
            controller.civPhoto.setImageBitmap(bitmap);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, bout);

                    controller.photoSelected = (bout.toByteArray());
                }
            }).start();
        } else if (requestCode == 3650 && data != null) {
            Uri uri = data.getData();

            final Bitmap bitmap = APIImage.readBitmap(getContext(), uri, 3);

            LinearLayout ll = (LinearLayout) controller.stepView.getOpenStep().getContentLayout();
            ImageView ivFront = ll.findViewById(R.id.iv_identity_front);
            ivFront.setImageBitmap(bitmap);

            controller.getStepIdentity().getStepData().setFrontPhoto(bitmap);
            controller.getStepIdentity().validateData();
        } else if (requestCode == 3651 && data != null) {
            Uri uri = data.getData();

            final Bitmap bitmap = APIImage.readBitmap(getContext(), uri, 3);

            LinearLayout ll = (LinearLayout) controller.stepView.getOpenStep().getContentLayout();
            ImageView ivVerse = ll.findViewById(R.id.iv_identity_verse);
            ivVerse.setImageBitmap(bitmap);

            controller.getStepIdentity().getStepData().setVersePhoto(bitmap);
            controller.getStepIdentity().validateData();
        }
    }

    private void loadPhoto(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    private void sendPhotoProfile() {
        regInterface.showLoading(true);
        HotDeliveryMan account = CustomApplication.getAccount();

        byte[] profile = controller.getPhotoSelected();

        PhotoCompat photoCompat = new PhotoCompat();
        photoCompat.setId(account.getId());
        photoCompat.setIdDevice(account.getIdDevice());
        photoCompat.setProfile(profile);
        photoCompat.setFrontPhoto(controller.getStepIdentity().getStepData().getFrontPhotoBytes());
        photoCompat.setVersePhoto(controller.getStepIdentity().getStepData().getVersePhotoBytes());

        InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
        requestServer.setIdRequisition("sendPhotoProfile()");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
        requestServer.setOtherCryptographyRSA(getString(R.string.rsabig));
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(getString(R.string.domain_connection));
        requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
        requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.REGISTER_PHOTO);
        requestServer.setObjectTransport(photoCompat);
        requestServer.request(getContext(), new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {}

            @Override
            public void onSucess(Object object) {
                if (object != null) {
                    JSONObject response = null;
                    try {
                        response = new JSONObject(object.toString());
                        response = response.getJSONObject("map");

                        String result = response.getString("result");

                        if (result != null && result.equals("saved_succefully")) {

                            regInterface.nextStep();

                            regInterface.showLoading(false);
                        } else if (result != null && result.equals("saved_failed")) {
                            Toast.makeText(getContext(), "Erro ao salvar photos", Toast.LENGTH_SHORT).show();
                            controller.getStepView().cancelFormCompletionOrCancellationAttempt();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        regInterface.showLoading(false);
                        controller.getStepView().cancelFormCompletionOrCancellationAttempt();
                    }
                } else {
                    regInterface.showLoading(false);
                    controller.getStepView().cancelFormCompletionOrCancellationAttempt();
                    Toast.makeText(getContext(), "Nenhum Valor Retornado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                regInterface.showLoading(false);
                controller.getStepView().cancelFormCompletionOrCancellationAttempt();
            }
        });
    }

    private synchronized void sendDataToRegister() throws JSONException, NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {

        UserNameStep stepName = controller.getStepName();
        UserDateOfBirthStep stepDateOfBirth = controller.getDateOfBirthStep();
        UserCPFStep stepCpf = controller.getStepCpf();
        UserPhotoIdentity stepIdentity = controller.getStepIdentity();
        UserAccountBank stepAccount = controller.getAccountBank();
        UserCelStep stepCel = controller.getStepCel();

        JSONObject ua = new JSONObject();
        ua.put("id", CustomApplication.getAccount().getId());
        ua.put("id_dev", CustomApplication.getAccount().getIdDevice());
        ua.put("name", stepName.getStepData());
        ua.put("cpf", stepCpf.getStepData());
        ua.put("cel", stepCel.getStepData());
        ua.put("d_birth", stepDateOfBirth.getStepData());
        ua.put("ba_ac", stepAccount.getStepData().getAccount());
        ua.put("ba_ac_dv", stepAccount.getStepData().getAccountDv());
        ua.put("ba_ag", stepAccount.getStepData().getAgency());
        ua.put("ba_ag_dv", stepAccount.getStepData().getAgencyDv());
        ua.put("ba_c", stepAccount.getStepData().getBankCode());
        ua.put("ba_t", stepAccount.getStepData().getType());

        InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
        requestServer.setIdRequisition("sendDataToRegister()");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(getString(R.string.domain_connection));
        requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
        requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.NEW_REGISTER);
        requestServer.setObjectTransport(ua);
        requestServer.request(getContext(), new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
                regInterface.showLoading(loading);
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {
                    HotDeliveryMan response = new Gson().fromJson(object.toString(), HotDeliveryMan.class);

                    if (response != null && response.getIdRecipient() != null) {
                        HotDeliveryMan account = CustomApplication.getAccount();
                        account.setIdRecipient(response.getIdRecipient());

                        CacheAPI.saveObject(getContext(), HotDeliveryMan.class.getSimpleName(), account, true);

                        sendPhotoProfile();
                    } else {
                        regInterface.showLoading(false);
                        controller.getStepView().cancelFormCompletionOrCancellationAttempt();
                        Toast.makeText(getContext(), "Sem Resposta do Servidor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    regInterface.showLoading(false);
                    controller.getStepView().cancelFormCompletionOrCancellationAttempt();
                    Toast.makeText(getContext(), "Nenhum resultado retornado pelo servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                controller.getStepView().cancelFormCompletionOrCancellationAttempt();
            }
        });
    }

    public class Controller implements View.OnClickListener, StepperFormListener {

        private CircleImageView civPhoto;
        private TextView tvName;
        private byte[] photoSelected;

        private VerticalStepperFormView stepView;
        private UserNameStep stepName;
        private UserCPFStep stepCpf;
        private UserCelStep stepCel;
        private UserPhotoIdentity stepIdentity;
        private UserAccountBank accountBank;
        private UserDateOfBirthStep dateOfBirthStep;

        public Controller(View v) {

            stepName = new UserNameStep("Nome Completo");
            dateOfBirthStep = new UserDateOfBirthStep("Data de Nascimento");
            stepCpf = new UserCPFStep("CPF");
            stepCel = new UserCelStep("Celular");
            stepIdentity = new UserPhotoIdentity("Identidade (foto)");
            accountBank = new UserAccountBank("Conta Bancária");

            this.civPhoto = v.findViewById(R.id.civ_photo_user_reg);
            this.tvName = v.findViewById(R.id.tv_user_name_reg);
            this.stepView = v.findViewById(R.id.step_view);
            //this.etBoardVehicle = v.findViewById(R.id.et_boardvehicle_reg);
            this.civPhoto.setOnClickListener(this);

            this.tvName.setText(CustomApplication.getAccount().getName());

            stepView.setup(this, stepName, dateOfBirthStep, stepCpf, stepCel, stepIdentity, accountBank)
                    .allowNonLinearNavigation(true)
                    .displayBottomNavigation(false)
                    .lastStepNextButtonText("Concluir Cadastro")
                    .init();
            //this.etBoardVehicle.setOnKeyListener(this);
        }

        public VerticalStepperFormView getStepView() {
            return stepView;
        }

        public UserNameStep getStepName() {
            return stepName;
        }

        public UserDateOfBirthStep getDateOfBirthStep() {
            return dateOfBirthStep;
        }

        public UserCPFStep getStepCpf() {
            return stepCpf;
        }

        public UserPhotoIdentity getStepIdentity() {
            return stepIdentity;
        }

        public UserAccountBank getAccountBank() {
            return accountBank;
        }

        public UserCelStep getStepCel() {
            return stepCel;
        }

        public byte[] getPhotoSelected() {
            return photoSelected;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                /*case R.id.btn_confirm_reg:{
                    saveData();
                    break;
                }*/
                case R.id.civ_photo_user_reg: {
                    loadPhoto(PICKPHOTO_REQUEST_CODE);
                    break;
                }
            }
        }

        @Override
        public void onCompletedForm() {
            if (photoSelected != null) {

                try {
                    sendDataToRegister();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Selecione uma foto de Perfil", Toast.LENGTH_SHORT).show();
                stepView.cancelFormCompletionOrCancellationAttempt();
            }

        }

        @Override
        public void onCancelledForm() {

        }
    }

    public class UserNameStep extends Step<String> {

        private EditText userNameView;

        public UserNameStep(String stepTitle) {
            super(stepTitle);
        }

        @Override
        protected View createStepContentLayout() {
            // Here we generate the view that will be used by the library as the content of the step.
            // In this case we do it programmatically, but we could also do it by inflating an XML layout.
            userNameView = new EditText(getContext());
            userNameView.setSingleLine(true);
            userNameView.setBackground(getResources().getDrawable(R.drawable.rounded_edit));
            userNameView.setMinHeight(38 * 2);
            userNameView.setPadding(16 * 2, 2 * 2, 16 * 2, 2 * 2);
            userNameView.setHint("Seu nome");

            userNameView.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Whenever the user updates the user name text, we update the state of the step.
                    // The step will be marked as completed only if its data is valid, which will be
                    // checked automatically by the form with a call to isStepDataValid().
                    markAsCompletedOrUncompleted(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            userNameView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        Utils.SystemUI.hideSystemUI(getActivity());
                    return false;
                }
            });
            userNameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b)
                        Utils.SystemUI.hideSystemUI(getActivity());
                }
            });

            return userNameView;
        }

        @Override
        protected IsDataValid isStepDataValid(String name) {
            // The step's data (i.e., the user name) will be considered valid only if it is longer than
            // three characters. In case it is not, we will display an error message for feedback.
            // In an optional step, you should implement this method to always return a valid value.
            boolean isNameValid = name.contains(" ") && name.split(" ").length >= 2;
            String errorMessage = !isNameValid ? "Nome Completo Necessário" : "";

            return new IsDataValid(isNameValid, errorMessage);
        }

        @Override
        public String getStepData() {
            // We get the step's data from the value that the user has typed in the EditText view.
            Editable userName = userNameView.getText();
            return userName != null ? userName.toString() : "";
        }

        @Override
        public String getStepDataAsHumanReadableString() {
            // Because the step's data is already a human-readable string, we don't need to convert it.
            // However, we return "(Empty)" if the text is empty to avoid not having any text to display.
            // This string will be displayed in the subtitle of the step whenever the step gets closed.
            String userName = getStepData();
            return !userName.isEmpty() ? userName : "(Empty)";
        }

        @Override
        protected void onStepOpened(boolean animated) {
            // This will be called automatically whenever the step gets opened.
        }

        @Override
        protected void onStepClosed(boolean animated) {
            // This will be called automatically whenever the step gets closed.
        }

        @Override
        protected void onStepMarkedAsCompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as completed.
        }

        @Override
        protected void onStepMarkedAsUncompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as uncompleted.
        }

        @Override
        public void restoreStepData(String stepData) {
            // To restore the step after a configuration change, we restore the text of its EditText view.
            userNameView.setText(stepData);
        }
    }

    public class UserCPFStep extends Step<String> {

        private EditText userCpfView;

        public UserCPFStep(String stepTitle) {
            super(stepTitle);
        }

        @Override
        protected View createStepContentLayout() {
            // Here we generate the view that will be used by the library as the content of the step.
            // In this case we do it programmatically, but we could also do it by inflating an XML layout.
            userCpfView = new EditText(getContext());
            userCpfView.setSingleLine(true);
            userCpfView.setBackground(getResources().getDrawable(R.drawable.rounded_edit));
            userCpfView.setMinHeight(38 * 2);
            userCpfView.setPadding(16 * 2, 2 * 2, 16 * 2, 2 * 2);
            userCpfView.setHint("CPF");
            userCpfView.setInputType(InputType.TYPE_CLASS_NUMBER);

            Utils.FieldText.setMaskInEditText(userCpfView, "NNN.NNN.NNN-NN");

            userCpfView.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Whenever the user updates the user name text, we update the state of the step.
                    // The step will be marked as completed only if its data is valid, which will be
                    // checked automatically by the form with a call to isStepDataValid().
                    markAsCompletedOrUncompleted(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            userCpfView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        Utils.SystemUI.hideSystemUI(getActivity());
                    return false;
                }
            });
            userCpfView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b)
                        Utils.SystemUI.hideSystemUI(getActivity());
                }
            });

            return userCpfView;
        }

        @Override
        protected IsDataValid isStepDataValid(String cpf) {
            // The step's data (i.e., the user name) will be considered valid only if it is longer than
            // three characters. In case it is not, we will display an error message for feedback.
            // In an optional step, you should implement this method to always return a valid value.
            boolean isNameValid = APIDocument.isCPF(cpf);
            String errorMessage = !isNameValid ? "CPF Inválido" : "";

            return new IsDataValid(isNameValid, errorMessage);
        }

        @Override
        public String getStepData() {
            // We get the step's data from the value that the user has typed in the EditText view.
            Editable userCpf = userCpfView.getText();
            return userCpf != null ? userCpf.toString() : "";
        }

        @Override
        public String getStepDataAsHumanReadableString() {
            // Because the step's data is already a human-readable string, we don't need to convert it.
            // However, we return "(Empty)" if the text is empty to avoid not having any text to display.
            // This string will be displayed in the subtitle of the step whenever the step gets closed.
            String userName = getStepData();
            return !userName.isEmpty() ? userName : "(Empty)";
        }

        @Override
        protected void onStepOpened(boolean animated) {
            // This will be called automatically whenever the step gets opened.
        }

        @Override
        protected void onStepClosed(boolean animated) {
            // This will be called automatically whenever the step gets closed.
        }

        @Override
        protected void onStepMarkedAsCompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as completed.
        }

        @Override
        protected void onStepMarkedAsUncompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as uncompleted.
        }

        @Override
        public void restoreStepData(String stepData) {
            // To restore the step after a configuration change, we restore the text of its EditText view.
            userCpfView.setText(stepData);
        }
    }

    public class UserCelStep extends Step<String> {

        private EditText userCelView;

        public UserCelStep(String stepTitle) {
            super(stepTitle);
        }

        @Override
        protected View createStepContentLayout() {
            // Here we generate the view that will be used by the library as the content of the step.
            // In this case we do it programmatically, but we could also do it by inflating an XML layout.
            userCelView = new EditText(getContext());
            userCelView.setSingleLine(true);
            userCelView.setBackground(getResources().getDrawable(R.drawable.rounded_edit));
            userCelView.setMinHeight(38 * 2);
            userCelView.setPadding(16 * 2, 2 * 2, 16 * 2, 2 * 2);
            userCelView.setHint("Celular");
            userCelView.setInputType(InputType.TYPE_CLASS_NUMBER);

            Utils.FieldText.setMaskInEditText(userCelView, "(NN) NNNNN-NNNN");

            userCelView.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Whenever the user updates the user name text, we update the state of the step.
                    // The step will be marked as completed only if its data is valid, which will be
                    // checked automatically by the form with a call to isStepDataValid().
                    markAsCompletedOrUncompleted(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            userCelView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        Utils.SystemUI.hideSystemUI(getActivity());
                    return false;
                }
            });
            userCelView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b)
                        Utils.SystemUI.hideSystemUI(getActivity());
                }
            });

            return userCelView;
        }

        @Override
        protected IsDataValid isStepDataValid(String celular) {
            // The step's data (i.e., the user name) will be considered valid only if it is longer than
            // three characters. In case it is not, we will display an error message for feedback.
            // In an optional step, you should implement this method to always return a valid value.
            boolean isNameValid = celular.length() == 15;
            String errorMessage = !isNameValid ? "Número Inválido" : "";

            return new IsDataValid(isNameValid, errorMessage);
        }

        @Override
        public String getStepData() {
            // We get the step's data from the value that the user has typed in the EditText view.
            Editable userCpf = userCelView.getText();
            return userCpf != null ? userCpf.toString() : "";
        }

        @Override
        public String getStepDataAsHumanReadableString() {
            // Because the step's data is already a human-readable string, we don't need to convert it.
            // However, we return "(Empty)" if the text is empty to avoid not having any text to display.
            // This string will be displayed in the subtitle of the step whenever the step gets closed.
            String userName = getStepData();
            return !userName.isEmpty() ? userName : "(Empty)";
        }

        @Override
        protected void onStepOpened(boolean animated) {
            // This will be called automatically whenever the step gets opened.
        }

        @Override
        protected void onStepClosed(boolean animated) {
            // This will be called automatically whenever the step gets closed.
        }

        @Override
        protected void onStepMarkedAsCompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as completed.
        }

        @Override
        protected void onStepMarkedAsUncompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as uncompleted.
        }

        @Override
        public void restoreStepData(String stepData) {
            // To restore the step after a configuration change, we restore the text of its EditText view.
            userCelView.setText(stepData);
        }
    }

    public class UserDateOfBirthStep extends Step<String> {

        private EditText userDateOfBirth;

        public UserDateOfBirthStep(String stepTitle) {
            super(stepTitle);
        }

        @Override
        protected View createStepContentLayout() {
            // Here we generate the view that will be used by the library as the content of the step.
            // In this case we do it programmatically, but we could also do it by inflating an XML layout.
            userDateOfBirth = new EditText(getContext());
            userDateOfBirth.setSingleLine(true);
            userDateOfBirth.setBackground(getResources().getDrawable(R.drawable.rounded_edit));
            userDateOfBirth.setMinHeight(38 * 2);
            userDateOfBirth.setPadding(16 * 2, 2 * 2, 16 * 2, 2 * 2);
            userDateOfBirth.setHint("Data de Nascimento");
            userDateOfBirth.setInputType(InputType.TYPE_CLASS_NUMBER);

            Utils.FieldText.setMaskInEditText(userDateOfBirth, "NN-NN-NNNN");

            userDateOfBirth.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Whenever the user updates the user name text, we update the state of the step.
                    // The step will be marked as completed only if its data is valid, which will be
                    // checked automatically by the form with a call to isStepDataValid().
                    markAsCompletedOrUncompleted(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            userDateOfBirth.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        Utils.SystemUI.hideSystemUI(getActivity());
                    return false;
                }
            });
            userDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b)
                        Utils.SystemUI.hideSystemUI(getActivity());
                }
            });

            return userDateOfBirth;
        }

        @Override
        protected IsDataValid isStepDataValid(String date) {
            // The step's data (i.e., the user name) will be considered valid only if it is longer than
            // three characters. In case it is not, we will display an error message for feedback.
            // In an optional step, you should implement this method to always return a valid value.
            boolean isNameValid = date.matches("\\d{2}+\\-+\\d{2}+\\-+\\d{4}");
            String errorMessage = !isNameValid ? "Data Inválida" : "";

            return new IsDataValid(isNameValid, errorMessage);
        }

        @Override
        public String getStepData() {
            // We get the step's data from the value that the user has typed in the EditText view.
            Editable userCpf = userDateOfBirth.getText();
            return userCpf != null ? userCpf.toString() : "";
        }

        @Override
        public String getStepDataAsHumanReadableString() {
            // Because the step's data is already a human-readable string, we don't need to convert it.
            // However, we return "(Empty)" if the text is empty to avoid not having any text to display.
            // This string will be displayed in the subtitle of the step whenever the step gets closed.
            String dateOfBirth = getStepData();
            return !dateOfBirth.isEmpty() ? dateOfBirth : "(Empty)";
        }

        @Override
        protected void onStepOpened(boolean animated) {
            // This will be called automatically whenever the step gets opened.
        }

        @Override
        protected void onStepClosed(boolean animated) {
            // This will be called automatically whenever the step gets closed.
        }

        @Override
        protected void onStepMarkedAsCompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as completed.
        }

        @Override
        protected void onStepMarkedAsUncompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as uncompleted.
        }

        @Override
        public void restoreStepData(String stepData) {
            // To restore the step after a configuration change, we restore the text of its EditText view.
            userDateOfBirth.setText(stepData);
        }
    }

    public class UserPhotoIdentity extends Step<StepIdentity> implements View.OnClickListener {

        private LinearLayout base;
        private ImageView ivIdentityFront, ivIdentityVerse;

        public static final int LOAD_FRONT_REQUEST = 3650;
        public static final int LOAD_VERSE_REQUEST = 3651;

        public UserPhotoIdentity(String stepTitle) {
            super(stepTitle);
        }

        public void validateData() {
            StepIdentity identity = (StepIdentity) base.getTag();
            isStepDataValid(identity);
            markAsCompletedOrUncompleted(isStepDataValid());
        }

        @Override
        protected View createStepContentLayout() {
            // Here we generate the view that will be used by the library as the content of the step.
            // In this case we do it programmatically, but we could also do it by inflating an XML layout.
            base = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.step_identity, null);

            ivIdentityFront = base.findViewById(R.id.iv_identity_front);
            ivIdentityVerse = base.findViewById(R.id.iv_identity_verse);
            ivIdentityFront.setOnClickListener(this);
            ivIdentityVerse.setOnClickListener(this);

            return base;
        }

        @Override
        protected IsDataValid isStepDataValid(StepIdentity photos) {
            // The step's data (i.e., the user name) will be considered valid only if it is longer than
            // three characters. In case it is not, we will display an error message for feedback.
            // In an optional step, you should implement this method to always return a valid value.
            boolean isPhotoValid = photos != null && photos.isOK();
            String errorMessage = !isPhotoValid ? "Fotos Necessárias" : "";

            return new IsDataValid(isPhotoValid, errorMessage);
        }

        @Override
        public StepIdentity getStepData() {
            // We get the step's data from the value that the user has typed in the EditText view.
            StepIdentity identity = (StepIdentity) base.getTag();

            if (identity != null)
                return identity;
            else {
                identity = new StepIdentity();
                base.setTag(identity);

                return identity;
            }
        }

        @Override
        public String getStepDataAsHumanReadableString() {
            // Because the step's data is already a human-readable string, we don't need to convert it.
            // However, we return "(Empty)" if the text is empty to avoid not having any text to display.
            // This string will be displayed in the subtitle of the step whenever the step gets closed.
            StepIdentity identity = (StepIdentity) base.getTag();
            return identity != null && identity.isOK() ? "Fotos definidas" : "Não definido";
        }

        @Override
        protected void onStepOpened(boolean animated) {
            // This will be called automatically whenever the step gets opened.
        }

        @Override
        protected void onStepClosed(boolean animated) {
            // This will be called automatically whenever the step gets closed.
        }

        @Override
        protected void onStepMarkedAsCompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as completed.
        }

        @Override
        protected void onStepMarkedAsUncompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as uncompleted.
        }

        @Override
        public void restoreStepData(StepIdentity stepData) {
            // To restore the step after a configuration change, we restore the text of its EditText view.
            ivIdentityFront.setImageBitmap(stepData.getFrontPhoto());
            ivIdentityVerse.setImageBitmap(stepData.getVersePhoto());
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.iv_identity_front) {
                loadPhoto(LOAD_FRONT_REQUEST);
            } else if (view.getId() == R.id.iv_identity_verse) {
                loadPhoto(LOAD_VERSE_REQUEST);
            }
        }

        private void setPhotoBytes(Bitmap photo, int quality, boolean front) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, quality, bout);


                }
            }).start();
        }
    }

    public class UserAccountBank extends Step<BAccountBank> {

        private EditText etAgency, etAccount, etAccountDV;
        private Spinner typeAccount;
        private AutoCompleteTextView banks;
        private LinearLayout llBase;

        public UserAccountBank(String stepTitle) {
            super(stepTitle);
        }

        @Override
        protected View createStepContentLayout() {
            // Here we generate the view that will be used by the library as the content of the step.
            // In this case we do it programmatically, but we could also do it by inflating an XML layout.

            BAccountBank ac = new BAccountBank();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 8, 0, 8);

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(-1, LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0, 8, 16, 8);
            params2.weight = 1.75f;

            llBase = new LinearLayout(getContext());
            llBase.setOrientation(LinearLayout.VERTICAL);
            llBase.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
            llBase.setPadding(0, 16, 0, 16);

            etAgency = new EditText(getContext());
            etAgency.setSingleLine(true);
            etAgency.setBackground(getResources().getDrawable(R.drawable.rounded_edit));
            etAgency.setMinHeight(38 * 2);
            etAgency.setPadding(16 * 2, 2 * 2, 0, 2 * 2);
            etAgency.setHint("Agencia c/ Digito");
            etAgency.setLayoutParams(params);
            etAgency.setInputType(InputType.TYPE_CLASS_NUMBER);
            etAgency.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Whenever the user updates the user name text, we update the state of the step.
                    // The step will be marked as completed only if its data is valid, which will be
                    // checked automatically by the form with a call to isStepDataValid().
                    markAsCompletedOrUncompleted(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            etAgency.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        ac.setAgency(etAgency.getText().toString().substring(0, etAgency.getText().toString().length() - 1));
                        ac.setAgencyDv(etAgency.getText().toString().substring(etAgency.getText().toString().length() - 1));
                        markAsCompletedOrUncompleted(isStepDataValid());
                    }
                    return false;
                }
            });
            etAgency.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b && etAgency.getText().toString().length() > 0) {
                        ac.setAgency(etAgency.getText().toString().substring(0, etAgency.getText().toString().length() - 1));
                        ac.setAgencyDv(etAgency.getText().toString().substring(etAgency.getText().toString().length() - 1));
                        markAsCompletedOrUncompleted(isStepDataValid());
                    }
                }
            });
            Utils.FieldText.setMaskInEditText(etAgency, "NNNNN");

            llBase.addView(etAgency);

            etAccount = new EditText(getContext());
            etAccount.setSingleLine(true);
            etAccount.setBackground(getResources().getDrawable(R.drawable.rounded_edit));
            etAccount.setMinHeight(38 * 2);
            etAccount.setPadding(16 * 2, 2 * 2, 16 * 2, 2 * 2);
            etAccount.setHint("Conta s/ Digito");
            etAccount.setLayoutParams(params2);
            etAccount.setInputType(InputType.TYPE_CLASS_NUMBER);
            etAccount.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Whenever the user updates the user name text, we update the state of the step.
                    // The step will be marked as completed only if its data is valid, which will be
                    // checked automatically by the form with a call to isStepDataValid().
                    markAsCompletedOrUncompleted(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            etAccount.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (etAccount.getText() != null && !etAccount.getText().toString().isEmpty()) {
                            ac.setAccount(etAccount.getText().toString().substring(0, etAccount.getText().toString().length() - 1));
                            banks.clearFocus();
                            etAccountDV.requestFocus();
                            etAccountDV.setSelection(0);

                            markAsCompletedOrUncompleted(isStepDataValid());
                        }
                    }
                    return false;
                }
            });
            etAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (etAccount.getText() != null && !etAccount.getText().toString().isEmpty()) {
                        ac.setAccount(etAccount.getText().toString().substring(0, etAccount.getText().toString().length() - 1));
                        banks.clearFocus();
                        etAccountDV.requestFocus();
                        etAccountDV.setSelection(0);

                        markAsCompletedOrUncompleted(isStepDataValid());
                    }
                }
            });
            Utils.FieldText.setMaskInEditText(etAccount, "NNNNNNNNNNNNN");

            etAccountDV = new EditText(getContext());
            etAccountDV.setSingleLine(true);
            etAccountDV.setBackground(getResources().getDrawable(R.drawable.rounded_edit));
            etAccountDV.setMinHeight(38 * 2);
            etAccountDV.setPadding(16 * 2, 2 * 2, 16 * 2, 2 * 2);
            etAccountDV.setHint("Digito");
            //etAccountDV.setLayoutParams(params2);
            etAccountDV.setInputType(InputType.TYPE_CLASS_NUMBER);
            etAccountDV.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Whenever the user updates the user name text, we update the state of the step.
                    // The step will be marked as completed only if its data is valid, which will be
                    // checked automatically by the form with a call to isStepDataValid().
                    markAsCompletedOrUncompleted(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            etAccountDV.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (etAccountDV.getText() != null && !etAccountDV.getText().toString().isEmpty()) {
                            ac.setAccountDv(etAccountDV.getText().toString());

                            markAsCompletedOrUncompleted(isStepDataValid());
                        }
                    }
                    return false;
                }
            });
            etAccountDV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (etAccountDV.getText() != null && !etAccountDV.getText().toString().isEmpty()) {
                        ac.setAccountDv(etAccountDV.getText().toString());

                        markAsCompletedOrUncompleted(isStepDataValid());
                    }
                }
            });
            Utils.FieldText.setMaskInEditText(etAccountDV, "NN");

            LinearLayout ll = new LinearLayout(getContext());
            ll.setGravity(Gravity.CENTER);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.addView(etAccount);
            ll.addView(etAccountDV);

            llBase.addView(ll);

            banks = new AutoCompleteTextView(getContext());
            banks.setHint("Nome do Banco");
            banks.setSingleLine(true);
            banks.setBackground(getResources().getDrawable(R.drawable.rounded_edit));
            banks.setMinHeight(38 * 2);
            banks.setPadding(16 * 2, 2 * 2, 16 * 2, 2 * 2);
            banks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    markAsCompletedOrUncompleted(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            banks.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        Utils.SystemUI.hideSystemUI(getActivity());
                        Utils.SystemUI.hideKeyboard(getActivity(), banks);
                    }
                    return false;
                }
            });
            banks.setThreshold(1);
            banks.setLayoutParams(params);
            banks.setCompletionHint("Nome do Banco");
            banks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String bank = (String) adapterView.getItemAtPosition(i);
                    String code = bank.split(" ")[bank.split(" ").length - 1];

                    ac.setName(bank);
                    ac.setBankCode(code);

                    Utils.SystemUI.hideKeyboard(getContext(), banks);
                    Utils.SystemUI.hideSystemUI(getActivity());

                    markAsCompletedOrUncompleted(isStepDataValid());
                }
            });
            String[] allBanks = getResources().getStringArray(R.array.banks);
            ArrayAdapter<String> adpBanks = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allBanks);
            banks.setAdapter(adpBanks);

            llBase.addView(banks);

            String[] types = {"Corrente ou Poupança?", "Corrente", "Corrente Conjunta", "Poupança", "Poupança Conjunta"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, types);

            typeAccount = new Spinner(getContext());
            typeAccount.setMinimumHeight(16 * 2);
            typeAccount.setBackground(getResources().getDrawable(R.drawable.rounded_edit));
            typeAccount.setPadding(16 * 2, 2 * 2, 16 * 2, 2 * 2);
            typeAccount.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        Utils.SystemUI.hideSystemUI(getActivity());
                    return false;
                }
            });
            typeAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b)
                        Utils.SystemUI.hideSystemUI(getActivity());
                }
            });
            typeAccount.setLayoutParams(params);
            typeAccount.setAdapter(adapter);
            typeAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String typeBank = (String) adapterView.getSelectedItem();
                    switch (typeBank) {
                        case "Corrente": {
                            ac.setType(BAccountBank.Type.CURRENT);
                            break;
                        }
                        case "Corrente Conjunta": {
                            ac.setType(BAccountBank.Type.CURRENT_JOINT);
                            break;
                        }
                        case "Poupança": {
                            ac.setType(BAccountBank.Type.SAVINGS);
                            break;
                        }
                        case "Poupança Conjunta": {
                            ac.setType(BAccountBank.Type.SAVINGS_JOINT);
                            break;
                        }
                    }

                    markAsCompletedOrUncompleted(isStepDataValid());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            llBase.addView(typeAccount);

            llBase.setTag(ac);

            return llBase;
        }

        @Override
        protected IsDataValid isStepDataValid(BAccountBank accountBank) {
            String message = null;

            if (accountBank.getAgency() == null || accountBank.getAgency().isEmpty()) {
                message = "Agência não definida";
            } else if (accountBank.getAgencyDv() == null || accountBank.getAgencyDv().isEmpty()) {
                message = "DV Agência não definido";
            } else if (accountBank.getAccount() == null || accountBank.getAccount().isEmpty()) {
                message = "N° Conta não definido";
            } else if (accountBank.getAccountDv() == null || accountBank.getAccountDv().isEmpty()) {
                message = "DV Conta não definido";
            } else if (accountBank.getBankCode() == null || accountBank.getBankCode().isEmpty()) {
                message = "COD Banco não definido";
            } else if (accountBank.getType() == -1) {
                message = "Tipo Conta não deifnido";
            }

            String agency = accountBank.getAgency();
            String agencyDV = accountBank.getAgencyDv();
            String account = accountBank.getAccount();
            String accountDV = accountBank.getAccountDv();
            String bankCode = accountBank.getBankCode();
            int bankType = accountBank.getType();

            boolean bankValid = agency != null && agencyDV != null && account != null
                    && accountDV != null && bankCode != null && bankType > -1;

            return new IsDataValid(bankValid, message);
        }

        @Override
        public BAccountBank getStepData() {
            // We get the step's data from the value that the user has typed in the EditText view.

            BAccountBank bank = (BAccountBank) llBase.getTag();

            if (bank != null)
                return bank;
            else {
                bank = new BAccountBank();
                llBase.setTag(bank);

                return bank;
            }
        }

        @Override
        public String getStepDataAsHumanReadableString() {
            // Because the step's data is already a human-readable string, we don't need to convert it.
            // However, we return "(Empty)" if the text is empty to avoid not having any text to display.
            // This string will be displayed in the subtitle of the step whenever the step gets closed.
            BAccountBank bank = getStepData();

            String agency = bank.getAgency();
            String agencyDV = bank.getAgencyDv();
            String account = bank.getAccount();
            String accountDV = bank.getAccountDv();
            String bankCode = bank.getBankCode();

            boolean bankValid = agency != null && agencyDV != null && account != null
                    && accountDV != null && bankCode != null;

            return bankValid ? bank.getName() + "\nAgência - " +
                    bank.getAgency() + "-" + bank.getAgencyDv() + "\nConta - " +
                    bank.getAccount() + "-" + bank.getAccountDv() : "Banco não definido!";
        }

        @Override
        protected void onStepOpened(boolean animated) {
            // This will be called automatically whenever the step gets opened.
        }

        @Override
        protected void onStepClosed(boolean animated) {
            // This will be called automatically whenever the step gets closed.
        }

        @Override
        protected void onStepMarkedAsCompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as completed.
        }

        @Override
        protected void onStepMarkedAsUncompleted(boolean animated) {
            // This will be called automatically whenever the step is marked as uncompleted.
        }

        @Override
        public void restoreStepData(BAccountBank stepData) {
            String agency = stepData.getAgency() + "-" + stepData.getAgencyDv();
            String account = stepData.getAccount() + "-" + stepData.getAccountDv();
            String accountDv = stepData.getAccountDv();
            // To restore the step after a configuration change, we restore the text of its EditText view.
            etAgency.setText(agency);
            etAccount.setText(account);
            etAccountDV.setText(accountDv);
            banks.setText(stepData.getName());
        }


    }
}
