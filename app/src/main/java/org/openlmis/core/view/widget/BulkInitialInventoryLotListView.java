package org.openlmis.core.view.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.openlmis.core.R;
import org.openlmis.core.view.adapter.BulkInitialInventoryAdapter;
import org.openlmis.core.view.adapter.BulkInitialInventoryLotMovementAdapter;
import org.openlmis.core.view.adapter.BulkLotInfoReviewListAdapter;
import org.openlmis.core.view.adapter.LotMovementAdapter;
import org.openlmis.core.view.holder.BulkInitialInventoryWithLotViewHolder;
import org.openlmis.core.view.viewmodel.BulkInitialInventoryViewModel;
import org.roboguice.shaded.goole.common.collect.FluentIterable;

import roboguice.inject.InjectView;

public class BulkInitialInventoryLotListView extends BaseLotListView {
    private static final String TAG = BulkInitialInventoryLotListView.class.getSimpleName();
    @InjectView(R.id.btn_no_stock_done)
    ViewGroup btnNoStockDone;

    @InjectView(R.id.btn_edit)
    TextView btnEdit;

    @InjectView(R.id.vg_edit_lot_area)
    ViewGroup vgEditLotArea;

    @InjectView(R.id.vg_lot_info_review)
    ViewGroup vgLotInfoReview;

    @InjectView(R.id.rv_lot_info_review)
    RecyclerView rvLotInfoReview;

    @InjectView(R.id.ll_btn_verify)
    ViewGroup btnVerify;
    @InjectView(R.id.ll_btn_remove_product)
    ViewGroup btnRemoveProduct;

    private BulkInitialInventoryWithLotViewHolder.InventoryItemStatusChangeListener statusChangeListener;

    public BulkInitialInventoryLotListView(Context context) {
        super(context);
    }

    public BulkInitialInventoryLotListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        btnNoStockDone.setOnClickListener(v -> {
            if (validateLotList()) {
                markDone(true);
            }
        });
        btnEdit.setOnClickListener(v -> markDone(false));
    }

    public void initLotListView(BulkInitialInventoryViewModel viewModel,
                                BulkInitialInventoryWithLotViewHolder.InventoryItemStatusChangeListener statusChangeListener,
                                View.OnClickListener removeProductListener) {
        this.statusChangeListener = statusChangeListener;
        super.initLotListView(viewModel);
        if (BulkInitialInventoryAdapter.ITEM_BASIC == viewModel.getViewType()) {
            btnVerify.setVisibility(getLotNumbers().size() == 0 ? View.GONE : View.VISIBLE);
            btnVerify.setOnClickListener(v -> {
                if (validateLotList()) {
                    markDone(true);
                }
            });
            btnRemoveProduct.setVisibility(View.GONE);
            btnNoStockDone.setVisibility(getLotNumbers().size() == 0 ? View.VISIBLE : View.GONE);
        } else if (BulkInitialInventoryAdapter.ITEM_NO_BASIC == viewModel.getViewType()) {
            btnRemoveProduct.setOnClickListener(removeProductListener);
            btnVerify.setVisibility(getLotNumbers().size() == 0 ? View.GONE : View.VISIBLE);
            btnVerify.setOnClickListener(v -> {
                if (validateLotList()) {
                    markDone(true);
                }
            });
            btnRemoveProduct.setVisibility(getLotNumbers().size() == 0 ? View.VISIBLE : View.GONE);
            btnNoStockDone.setVisibility(View.GONE);
        }
        markDone(viewModel.isDone());
    }

    @Override
    public void initExistingLotListView() {
        existingLotListView.setLayoutManager(new LinearLayoutManager(getContext()));
        FluentIterable.from(viewModel.getExistingLotMovementViewModelList()).transform(lotMovementViewModel -> {
            lotMovementViewModel.setFrom(((BulkInitialInventoryViewModel) viewModel).getFrom());
            return lotMovementViewModel;
        }).toList();
        existingLotMovementAdapter = new BulkInitialInventoryLotMovementAdapter(viewModel.getExistingLotMovementViewModelList());
        existingLotMovementAdapter.setMovementChangedListenerWithStatus(movementChangedListenerWithStatus);
        existingLotListView.setAdapter(existingLotMovementAdapter);
    }

    @Override
    public void initNewLotListView() {
        newLotListView.setLayoutManager(new LinearLayoutManager(getContext()));
        FluentIterable.from(viewModel.getNewLotMovementViewModelList()).transform(lotMovementViewModel -> {
            lotMovementViewModel.setFrom(((BulkInitialInventoryViewModel) viewModel).getFrom());
            return lotMovementViewModel;
        }).toList();
        newLotMovementAdapter = new BulkInitialInventoryLotMovementAdapter(viewModel.getNewLotMovementViewModelList(), viewModel.getProduct().getProductNameWithCodeAndStrength());
        newLotMovementAdapter.setMovementChangedListenerWithStatus(movementChangedListenerWithStatus);
        newLotListView.setAdapter(newLotMovementAdapter);
    }

    LotMovementAdapter.MovementChangedListenerWithStatus movementChangedListenerWithStatus = (amount) -> {
        if (BulkInitialInventoryAdapter.ITEM_BASIC == ((BulkInitialInventoryViewModel) viewModel).getViewType()) {
            btnVerify.setVisibility(TextUtils.isEmpty(amount) ? GONE : VISIBLE);
            btnNoStockDone.setVisibility(TextUtils.isEmpty(amount) ? VISIBLE : GONE);
        } else if (BulkInitialInventoryAdapter.ITEM_NO_BASIC == ((BulkInitialInventoryViewModel) viewModel).getViewType()) {
            btnVerify.setVisibility(TextUtils.isEmpty(amount) ? GONE : VISIBLE);
            btnNoStockDone.setVisibility(GONE);
            btnRemoveProduct.setVisibility(TextUtils.isEmpty(amount) ? VISIBLE : GONE);
        }

    };

    private void initLotInfoReviewList() {
        BulkLotInfoReviewListAdapter adapter = new BulkLotInfoReviewListAdapter(viewModel);
        rvLotInfoReview.setLayoutManager(new LinearLayoutManager(context));
        rvLotInfoReview.setAdapter(adapter);
    }


    public void markDone(boolean done) {
        ((BulkInitialInventoryViewModel) viewModel).setDone(done);
        vgEditLotArea.setVisibility(done ? GONE : VISIBLE);
        vgLotInfoReview.setVisibility(done ? VISIBLE : GONE);
        statusChangeListener.onStatusChange(done);
        if (done) {
            initLotInfoReviewList();
        }
    }

    public boolean validateLotList() {
        int position1 = ((BulkInitialInventoryLotMovementAdapter) existingLotMovementAdapter).validateLotNonEmptyQuantity();
        if (position1 >= 0) {
            existingLotListView.scrollToPosition(position1);
            return false;
        }
        int position2 = ((BulkInitialInventoryLotMovementAdapter) newLotMovementAdapter).validateLotPositiveQuantity();
        if (position2 >= 0) {
            newLotListView.scrollToPosition(position2);
            return false;
        }
        return true;
    }

    @Override
    protected void inflateLayout(Context context) {
        inflate(context, R.layout.view_lot_list_bulkinitial_inventory, this);
    }
}