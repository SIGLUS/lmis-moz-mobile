package org.openlmis.core.view.holder;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;
import org.openlmis.core.R;
import org.openlmis.core.utils.TextStyleUtil;
import org.openlmis.core.view.viewmodel.InventoryViewModel;
import roboguice.inject.InjectView;

public class KitStockCardViewHolder extends StockCardViewHolder {

  @InjectView(R.id.tv_product_name)
  TextView tvProductName;
  @InjectView(R.id.tv_product_unit)
  TextView tvProductUnit;
  @InjectView(R.id.tv_stock_on_hand)
  TextView tvStockOnHand;
  //above field are present in base class, but injection does not penetrate sub class

  public KitStockCardViewHolder(View itemView, OnItemViewClickListener listener) {
    super(itemView, listener);
  }

  @Override
  protected void inflateData(InventoryViewModel inventoryViewModel, String queryKeyWord) {
    tvStockOnHand.setText(String.valueOf(inventoryViewModel.getStockOnHand()));
    tvProductName.setText(TextStyleUtil.getHighlightQueryKeyWord(queryKeyWord,
        new SpannableStringBuilder(inventoryViewModel.getProduct().getPrimaryName())));
    tvProductUnit.setVisibility(View.INVISIBLE);
  }
}
