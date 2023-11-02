package com.hotburguer.fragments.dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hotburguer.R;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.adapters.AdapterReuseAddress;
import com.hotburguer.fragments.address.AddressInterface;
import com.hotburguer.itens.HotReuseAddress;

import java.util.ArrayList;

import apis.location.LAddress;

public class DialogResuseAddress extends DialogFragment {

    public static final String DIALOG_TAG = "dialog_resuse_address";
    private ArrayList<HotReuseAddress> addresses;
    private LAddress addressSelected;

    private Controller controller;
    private AddressInterface interfaceAddress;

    public DialogResuseAddress(ArrayList<HotReuseAddress> addresses, AddressInterface interfaceAddress) {
        this.addresses = addresses;
        this.interfaceAddress = interfaceAddress;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dg_reuse_address, null);
        controller = new Controller(view);

        setCancelable(true);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        showLoading(false);

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        CustomApplication.hideSystemUI(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    public void showLoading(boolean show) {
        controller.getProgress().setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public class Controller implements AdapterView.OnItemClickListener, View.OnClickListener {

        private AdapterReuseAddress adapter;
        private RelativeLayout progress;
        private ListView list;
        private Button btnCloseDialog ;

        public Controller(View v) {
            adapter = new AdapterReuseAddress(getContext(), DialogResuseAddress.this, addresses);
            list = v.findViewById(R.id.lv_reuse_address);
            list.setAdapter(adapter);
            list.setOnItemClickListener(this);
            progress = v.findViewById(R.id.rl_base_progress_dg_reuse_address);
            btnCloseDialog = v.findViewById(R.id.btn_close_dg_reuse_address);
            btnCloseDialog.setOnClickListener(this);
        }

        public RelativeLayout getProgress() {
            return progress;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            addressSelected = adapter.getAddress(i);
            addressSelected.setConfirmed(true);

            interfaceAddress.setAddressFull(addressSelected);
            dismiss();
        }

        @Override
        public void onClick(View view) {
            CustomApplication.hideSystemUI(getActivity());
            dismiss();
        }
    }
}
