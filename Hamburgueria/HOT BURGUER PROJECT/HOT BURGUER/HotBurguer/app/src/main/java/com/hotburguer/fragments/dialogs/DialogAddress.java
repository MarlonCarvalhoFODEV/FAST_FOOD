package com.hotburguer.fragments.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.hotburguer.R;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.fragments.address.AddressInterface;

import apis.location.LAddress;

public class DialogAddress extends DialogFragment {

    private LAddress address;
    private Controller controller;
    private AddressInterface interfaceAddress;

    public static String DIALOG_TAG = "dialog_address";

    public DialogAddress(LAddress address, AddressInterface interfaceAddress) {
        this.address = address;
        this.interfaceAddress = interfaceAddress;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dg_address_saved, null);
        controller = new Controller(view);

        setCancelable(true);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getDialog().setOnDismissListener(controller);
    }

    @Override
    public void onDestroy() {
        interfaceAddress.setAddressFull(address);
        super.onDestroy();
    }

    public class Controller implements View.OnClickListener, Dialog.OnDismissListener{

        private TextInputLayout tilCountry;
        private EditText etCountry, etState, etCity, etNeighborhood, etStreet, etNumber, etComplement, etCep;
        private Button btnSaveAddress;

        public Controller(View v) {
            this.tilCountry = v.findViewById(R.id.til_country_address);
            this.etCountry = v.findViewById(R.id.et_country_address);
            this.etState = v.findViewById(R.id.et_state_address);
            this.etCity = v.findViewById(R.id.et_city_address);
            this.etNeighborhood = v.findViewById(R.id.et_neighborhood_address);
            this.etStreet = v.findViewById(R.id.et_street_address);
            this.etNumber = v.findViewById(R.id.et_number_address);
            this.etComplement = v.findViewById(R.id.et_complement_address);
            this.etCep = v.findViewById(R.id.et_cep_address);

            this.btnSaveAddress = v.findViewById(R.id.btn_save_address);
            this.btnSaveAddress.setOnClickListener(this);

            setAddress();
        }

        private void setAddress() {
            if (address.getContry() != null && !address.getContry().isEmpty())
                etCountry.setText(address.getContry());
            else
                tilCountry.setVisibility(View.GONE);

            etState.setText(address.getState());
            etCity.setText(address.getCity());
            etNeighborhood.setText(address.getNeighborhood());
            etStreet.setText(address.getStreet());
            etNumber.setText(String.valueOf(address.getNumber()));
            etCep.setText(address.getCEP());
        }

        private void saveAddress() {
            address.setNumber(Integer.valueOf(etNumber.getText().toString()));
            address.setComplement(etComplement.getText().toString());
            address.setConfirmed(true);
            interfaceAddress.setAddressFull(address);

            if (validateDatas()) {
                dismiss();
            }
        }

        private boolean validateDatas() {
            String country = address.getContry();
            String state = address.getState();
            String city = address.getCity();
            String neighborhood = address.getNeighborhood();
            String street = address.getStreet();
            String cep = address.getCEP();
            int number = address.getNumber();

            if (country != null && !country.isEmpty()) {
                if (state != null && !state.isEmpty()) {
                    if (city != null && !city.isEmpty()) {
                        if (neighborhood != null && !neighborhood.isEmpty()) {
                            if (street != null && !street.isEmpty()) {
                                if (cep != null && !cep.isEmpty()) {
                                    if (number > 0) {
                                        return true;
                                    } else {
                                        Toast.makeText(getContext(), "Número inválido", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Cep inválido", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Rua inválida", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Bairro inválido", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Cidade inválida", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Estado inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "País inválido", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        @Override
        public void onClick(View view) {

            saveAddress();

        }

        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            if(address.isConfirmed()){
                CustomApplication.getUser().setAddress(address);
                CustomApplication.getOrder().setAddress(address);
            }
        }
    }
}
