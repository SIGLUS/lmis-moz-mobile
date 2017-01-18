package org.openlmis.core.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.openlmis.core.R;
import org.openlmis.core.view.holder.StockMovementHistoryViewHolder;
import org.openlmis.core.view.viewmodel.StockMovementHistoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovementHistoryAdapter extends RecyclerView.Adapter<StockMovementHistoryViewHolder> {

    private List<StockMovementHistoryViewModel> viewModels;

    public MovementHistoryAdapter() {
        viewModels = new ArrayList<>();
    }

    public void refresh(List<StockMovementHistoryViewModel> stockMovementHistoryViewModels) {
        this.viewModels = stockMovementHistoryViewModels;
        notifyDataSetChanged();
    }

    @Override
    public StockMovementHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StockMovementHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_movement_history, parent, false));
    }

    @Override
    public void onBindViewHolder(StockMovementHistoryViewHolder holder, int position) {
        holder.populate(viewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }
}