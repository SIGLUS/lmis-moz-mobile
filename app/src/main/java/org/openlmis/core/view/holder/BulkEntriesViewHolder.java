/*
 * This program is part of the OpenLMIS logistics management information
 * system platform software.
 *
 * Copyright © 2015 ThoughtWorks, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. This program is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should
 * have received a copy of the GNU Affero General Public License along with
 * this program. If not, see http://www.gnu.org/licenses. For additional
 * information contact info@OpenLMIS.org
 */

package org.openlmis.core.view.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.openlmis.core.R;
import org.openlmis.core.utils.Constants;
import org.openlmis.core.utils.TextStyleUtil;
import org.openlmis.core.view.adapter.BulkEntriesAdapter;
import org.openlmis.core.view.viewmodel.BulkEntriesViewModel;
import org.openlmis.core.view.viewmodel.BulkEntriesViewModel.InvalidType;
import org.openlmis.core.view.widget.BulkEntriesLotListView;
import roboguice.inject.InjectView;

public class BulkEntriesViewHolder extends BaseViewHolder {

  @InjectView(R.id.bulk_entries_header)
  private ViewGroup itemHeader;

  @InjectView(R.id.ic_done)
  private ImageView icDone;

  @InjectView(R.id.tv_product_name)
  private TextView productName;

  @InjectView(R.id.ic_trashcan)
  private ImageView icTrashcan;

  @InjectView(R.id.view_lot_list)
  private BulkEntriesLotListView bulkEntriesLotListView;

  private BulkEntriesViewModel bulkEntriesViewModel;

  public BulkEntriesViewHolder(View itemView) {
    super(itemView);
  }

  public void populate(BulkEntriesViewModel bulkEntriesViewModel,
      final BulkEntriesAdapter bulkEntriesAdapter) {
    this.bulkEntriesViewModel = bulkEntriesViewModel;
    icTrashcan
        .setOnClickListener(getRemoveProductListener(bulkEntriesViewModel, bulkEntriesAdapter));
    setMovementDone();
    setInvalidStatus();
    bulkEntriesLotListView
        .initLotListView(bulkEntriesViewModel, bulkEntriesAdapter, getAmountChangeListener(),
            getMovementStatusListener());
  }

  private void setInvalidStatus() {
    if (bulkEntriesViewModel.getInvalidType() == InvalidType.EXISTING_LOT_ALL_BLANK
        || bulkEntriesViewModel.getInvalidType() == InvalidType.NO_LOT) {
      icTrashcan.setImageResource(R.drawable.ic_trashcan_red);
    } else {
      icTrashcan.setImageResource(R.drawable.ic_trashcan);
    }
  }

  private void setMovementDone() {
    if (bulkEntriesViewModel.isDone()) {
      icDone.setVisibility(View.VISIBLE);
      productName.setText(bulkEntriesViewModel.getGreenName());
      itemHeader.setBackgroundColor(context.getResources().getColor(R.color.color_white));
    } else {
      icDone.setVisibility(View.GONE);
      itemHeader.setBackgroundColor(context.getResources().getColor(R.color.color_D8D8D8));
      productName.setText(TextStyleUtil.formatStyledProductName(bulkEntriesViewModel.getProduct()));
    }
  }

  private View.OnClickListener getRemoveProductListener(BulkEntriesViewModel bulkEntriesViewModel,
      BulkEntriesAdapter bulkEntriesAdapter) {
    return v -> {
      bulkEntriesAdapter.remove(bulkEntriesViewModel);
      EventBus.getDefault().post(Constants.REFRESH_BACKGROUND_EVENT);
    };
  }

  private BulkEntriesLotListView.AmountChangeListener getAmountChangeListener() {
    return this::setInvalidStatus;
  }

  private BulkEntriesLotListView.MovementStatusListener getMovementStatusListener() {
    return this::setMovementDone;
  }
}