package br.com.hotbmanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.hotbmanager.R;
import br.com.hotbmanager.dialogs.DialogInformative;
import br.com.hotbmanager.itens.HotProduct;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.Holder> {

    private Context context ;
    private ArrayList<HotProduct> itens ;
    private int sizeHeight ;

    public AdapterProducts(Context context) {
        this.context = context;
        this.itens = new ArrayList<>();
    }

    public int getSizeHeight() {
        return sizeHeight;
    }

    public void addAllProduct(ArrayList<HotProduct> products) {
        itens.addAll(products);
        notifyDataSetChanged();
    }

    public void addProduct(HotProduct product) {
        itens.add(product);
        notifyItemInserted(itens.size()-1);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.it_product_adm, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindValues(itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView icImg ;
        private TextView tvName, tvCount ;
        private View root ;
        private HotProduct product ;

        public Holder(View view) {
            super(view);

            this.root = view ;
            this.icImg = view.findViewById(R.id.iv_img_it_product);
            this.tvName = view.findViewById(R.id.tv_name_it_product);
            this.tvCount = view.findViewById(R.id.tv_count_it_product);
        }

        public void bindValues(HotProduct product){
            this.product = product ;
            tvName.setText(product.getTitle());
            tvCount.setText(product.getCount()+"x");

            sizeHeight = root.getHeight();

            if(product.getRecipe() != null)
                root.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            DialogInformative dialog = new DialogInformative(context);
            dialog.setTitle("Receita");
            dialog.setMessage(product.getRecipe());
            dialog.show();
        }
    }
}
