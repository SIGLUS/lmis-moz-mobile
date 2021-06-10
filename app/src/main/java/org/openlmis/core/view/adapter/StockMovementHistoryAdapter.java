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

package org.openlmis.core.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;
import org.openlmis.core.R;
import org.openlmis.core.view.holder.StockMovementItemHistoryViewHolder;
import org.openlmis.core.view.viewmodel.StockMovementViewModel;


public class StockMovementHistoryAdapter extends BaseAdapter {

  List<StockMovementViewModel> stockMovementViewModels;

  public StockMovementHistoryAdapter(List<StockMovementViewModel> list) {
    stockMovementViewModels = list;
  }

  @Override
  public int getCount() {
    return stockMovementViewModels.size();
  }

  @Override
  public StockMovementViewModel getItem(int position) {
    return stockMovementViewModels.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    StockMovementItemHistoryViewHolder holder;

    if (convertView == null) {
      convertView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_stock_movement, parent, false);
      holder = new StockMovementItemHistoryViewHolder(convertView);

      convertView.setTag(holder);
    } else {
      holder = (StockMovementItemHistoryViewHolder) convertView.getTag();
    }
    final StockMovementViewModel model = getItem(position);

    holder.populate(model);

    return convertView;
  }
}
