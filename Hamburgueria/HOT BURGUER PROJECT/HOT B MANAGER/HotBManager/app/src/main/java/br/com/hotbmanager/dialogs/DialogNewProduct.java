package br.com.hotbmanager.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import apis.finance.FinanceAPI;
import apis.location.LAddress;
import br.com.hotbmanager.R;
import br.com.hotbmanager.utils.Utils;

public class DialogNewProduct extends DialogFragment {

    private Controller controller;

    public static String DIALOG_TAG = "dialog_address";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.it_product_adm, null);
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
        super.onDestroy();
    }

    public class Controller implements View.OnClickListener, Dialog.OnDismissListener {

        private TextView tvTitle, tvCategorie, tvCoastPrice, tvFinalPrice;
        private EditText etTitle, etDescription, etRecipe, etFoodPornUrl, etCoastPrice, etFeeDeliveryman, etQtStock, etComission;
        private CheckBox cbCombo, cbTangible, cbActiveProduct;
        private Button btnSave, btnCancel;

        public Controller(View v) {
            this.tvTitle = v.findViewById(R.id.tv_title_it_product_adm);
            this.tvCategorie = v.findViewById(R.id.tv_categorie_it_product_adm);
            this.tvCoastPrice = v.findViewById(R.id.tv_coast_price_it_product_adm);
            this.tvFinalPrice = v.findViewById(R.id.tv_final_price_it_product_adm);

            this.etTitle = v.findViewById(R.id.et_title_it_product_adm);
            this.etTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    tvTitle.setText(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            this.etDescription = v.findViewById(R.id.et_description_it_product_adm);
            this.etRecipe = v.findViewById(R.id.et_recipe_it_product_adm);
            this.etFoodPornUrl = v.findViewById(R.id.et_url_it_product_adm);
            this.etCoastPrice = v.findViewById(R.id.et_coast_price_it_product_adm);
            this.etCoastPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    tvCoastPrice.setText("coast: "+charSequence.toString());
                    String value = String.valueOf(FinanceAPI.format(calculateValueFinal(), 2));
                    tvFinalPrice.setText("R$ "+value);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            this.etFeeDeliveryman = v.findViewById(R.id.et_fee_deliveryman_it_product_adm);
            this.etQtStock = v.findViewById(R.id.et_qt_stock_it_product_adm);
            this.etComission = v.findViewById(R.id.et_comission_it_product_adm);

            this.cbCombo = v.findViewById(R.id.cb_combo_it_product_adm);
            this.cbTangible = v.findViewById(R.id.cb_tangible_it_product_adm);
            this.cbActiveProduct = v.findViewById(R.id.cb_active_it_product_adm);

            this.btnSave = v.findViewById(R.id.cb_active_it_product_adm);
            this.btnSave.setOnClickListener(this);

            this.btnCancel = v.findViewById(R.id.cb_active_it_product_adm);
            this.btnCancel.setOnClickListener(this);

        }

        private double calculateValueFinal(){
            double coast = 0.0 ;
            if(etCoastPrice.getText() != null && etCoastPrice.getText().toString().isEmpty())
                coast = Double.valueOf(etCoastPrice.getText().toString());
            double feeCredCard = 3.79;
            double feeAntifraud = 0.7;
            double feeProcessing = 0.5;

            double feeComission =0.0;
            if(etComission.getText() != null && etComission.getText().toString().isEmpty())
                feeComission = Double.valueOf(etComission.getText().toString());

            return (coast+feeAntifraud+feeProcessing)/(1-((feeComission+feeCredCard)/100));
        }

        private boolean validateDatas() {
            String title = etTitle.getText().toString();
            String description = etDescription.getText().toString();
            String recipe = etRecipe.getText().toString();
            String foodPorn = etFoodPornUrl.getText().toString();
            String coastPrice = etCoastPrice.getText().toString();
            String feeDeliveryman = etFeeDeliveryman.getText().toString();
            String qtStock = etQtStock.getText().toString();
            String comission = etComission.getText().toString();

            if (title != null && !title.isEmpty()) {
                if (description != null && !description.isEmpty()) {
                    if (recipe != null && !recipe.isEmpty()) {
                        if (foodPorn != null && !foodPorn.isEmpty()) {
                            if (coastPrice != null && !coastPrice.isEmpty()) {
                                if (feeDeliveryman != null && !feeDeliveryman.isEmpty()) {
                                    if (qtStock != null && !qtStock.isEmpty()) {
                                        if (comission != null && !comission.isEmpty()) {
                                            return true ;
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Comissão Inválida", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Quantidade em Stock Inválida", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Taxa de Entrega inválido", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Custo inválida", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Video url inválido", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Receita inválida", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Descrição inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Titulo inválido", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        @Override
        public void onClick(View view) {


        }

        @Override
        public void onDismiss(DialogInterface dialogInterface) {

        }
    }
}