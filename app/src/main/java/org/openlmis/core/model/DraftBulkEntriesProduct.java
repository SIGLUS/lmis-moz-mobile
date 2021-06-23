package org.openlmis.core.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.utils.ListUtil;
import org.openlmis.core.view.viewmodel.BulkEntriesViewModel;
import org.openlmis.core.view.viewmodel.LotMovementViewModel;
import org.roboguice.shaded.goole.common.collect.FluentIterable;

@Setter
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "draft_bulk_entries_product")
public class DraftBulkEntriesProduct extends BaseModel {

  @DatabaseField(foreign = true, foreignAutoRefresh = true)
  Product product;

  @DatabaseField(defaultValue = "false")
  boolean done;

  @DatabaseField
  Long quantity;

  @ForeignCollectionField(eager = true)
  private ForeignCollection<DraftBulkEntriesProductLotItem> foreignDraftLotItems;

  private List<DraftBulkEntriesProductLotItem> draftLotItemListWrapper;

  public DraftBulkEntriesProduct(BulkEntriesViewModel bulkEntriesViewModel) {
    this.product = bulkEntriesViewModel.getProduct();
    this.done = bulkEntriesViewModel.isDone();
    this.quantity = bulkEntriesViewModel.getLotListQuantityTotalAmount();
    setupDraftLotList(bulkEntriesViewModel.getExistingLotMovementViewModelList(),
        bulkEntriesViewModel.getNewLotMovementViewModelList());
  }

  public List<DraftBulkEntriesProductLotItem> getDraftLotItemListWrapper() {
    draftLotItemListWrapper = ListUtil.wrapOrEmpty(foreignDraftLotItems, draftLotItemListWrapper);
    return draftLotItemListWrapper;
  }

  private void setupDraftLotList(List<LotMovementViewModel> existingLotMovementViewModelList,
      List<LotMovementViewModel> lotMovementViewModelList) {
    getDraftLotItemListWrapper().addAll(
        FluentIterable.from(existingLotMovementViewModelList).transform(lotMovementViewModel -> {
          DraftBulkEntriesProductLotItem draftLotItem = new DraftBulkEntriesProductLotItem(
              lotMovementViewModel, product,
              false);
          draftLotItem.setDraftBulkEntriesProduct(DraftBulkEntriesProduct.this);
          return draftLotItem;
        }).toList());
    getDraftLotItemListWrapper()
        .addAll(FluentIterable.from(lotMovementViewModelList).transform(lotMovementViewModel -> {
          DraftBulkEntriesProductLotItem draftLotItem = new DraftBulkEntriesProductLotItem(
              lotMovementViewModel, product,
              true);
          draftLotItem.setDraftBulkEntriesProduct(DraftBulkEntriesProduct.this);
          return draftLotItem;
        }).toList());
  }

}
