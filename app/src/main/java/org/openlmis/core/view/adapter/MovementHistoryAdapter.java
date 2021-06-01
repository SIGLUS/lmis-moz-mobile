package org.openlmis.core.view.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.openlmis.core.R;
import org.openlmis.core.view.holder.StockHistoryViewHolder;
import org.openlmis.core.view.viewmodel.StockHistoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovementHistoryAdapter extends RecyclerView.Adapter<StockHistoryViewHolder> {

    private List<StockHistoryViewModel> viewModels;

    public MovementHistoryAdapter() {
        viewModels = new ArrayList<>();
    }

    public void refresh(List<StockHistoryViewModel> stockHistoryViewModels) {
        this.viewModels = stockHistoryViewModels;
        notifyDataSetChanged();
    }

    @Override
    public StockHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StockHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_history, parent, false));
    }

    @Override
    public void onBindViewHolder(StockHistoryViewHolder holder, int position) {
        holder.populate(viewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }
}
