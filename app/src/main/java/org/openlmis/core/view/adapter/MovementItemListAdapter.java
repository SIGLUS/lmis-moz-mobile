package org.openlmis.core.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import org.openlmis.core.R;
import org.openlmis.core.view.holder.StockHistoryMovementItemViewHolder;
import org.openlmis.core.view.viewmodel.StockHistoryMovementItemViewModel;

public class MovementItemListAdapter extends
    RecyclerView.Adapter<StockHistoryMovementItemViewHolder> {

  private final List<StockHistoryMovementItemViewModel> viewModels;

  public MovementItemListAdapter(List<StockHistoryMovementItemViewModel> viewModels) {
    this.viewModels = viewModels;
  }

  @Override
  public StockHistoryMovementItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new StockHistoryMovementItemViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_movement_history, parent, false));
  }

  @Override
  public void onBindViewHolder(StockHistoryMovementItemViewHolder holder, int position) {
    holder.populate(viewModels.get(position));
  }

  @Override
  public int getItemCount() {
    return viewModels.size();
  }
}
