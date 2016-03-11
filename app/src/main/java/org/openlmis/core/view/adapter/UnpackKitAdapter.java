package org.openlmis.core.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openlmis.core.R;
import org.openlmis.core.view.holder.UnpackKitViewHolder;
import org.openlmis.core.view.viewmodel.InventoryViewModel;

import java.util.List;

public class UnpackKitAdapter extends InventoryListAdapterWithBottomBtn implements FilterableAdapter {

    private final View.OnClickListener onClickListener;

    public UnpackKitAdapter(List<InventoryViewModel> data, View.OnClickListener onClickListener) {
        super(data);
        this.onClickListener = onClickListener;
    }

    @Override
    protected void populate(RecyclerView.ViewHolder viewHolder, int position) {
        final InventoryViewModel viewModel = currentList.get(position);
        ((UnpackKitViewHolder) viewHolder).populate(viewModel);
    }

    @Override
    protected VHFooter onCreateFooterView(ViewGroup parent) {
        VHFooter vhFooter = new VHFooter(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_complete_btn, parent, false));
        vhFooter.itemView.findViewById(R.id.btn_complete).setOnClickListener(onClickListener);
        return vhFooter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_physical_inventory, parent, false);
        return new UnpackKitViewHolder(view);
    }

}
