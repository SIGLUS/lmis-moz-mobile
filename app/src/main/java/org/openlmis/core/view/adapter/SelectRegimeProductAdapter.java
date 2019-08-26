package org.openlmis.core.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.openlmis.core.R;
import org.openlmis.core.view.holder.SelectRegimeProductsViewHolder;
import org.openlmis.core.view.viewmodel.RegimeProductViewModel;

import java.util.List;

public class SelectRegimeProductAdapter extends RecyclerView.Adapter<SelectRegimeProductsViewHolder> {

    private List<RegimeProductViewModel> products;

    public SelectRegimeProductAdapter(List<RegimeProductViewModel> products) {
        this.products = products;
    }

    @Override
    public SelectRegimeProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectRegimeProductsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_product_regime, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectRegimeProductsViewHolder holder, int position) {
        RegimeProductViewModel product = products.get(position);
        holder.populate(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
