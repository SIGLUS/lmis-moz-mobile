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

package org.openlmis.core.view.activity;

import static org.openlmis.core.view.activity.AddProductsToBulkEntriesActivity.SELECTED_PRODUCTS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.Serializable;
import java.util.List;
import org.openlmis.core.R;
import org.openlmis.core.googleanalytics.ScreenName;
import org.openlmis.core.model.Product;
import org.openlmis.core.presenter.BulkEntriesPresenter;
import org.openlmis.core.utils.InjectPresenter;
import org.openlmis.core.utils.ToastUtil;
import org.openlmis.core.view.adapter.BulkEntriesAdapter;
import org.openlmis.core.view.viewmodel.BulkEntriesViewModel;
import org.openlmis.core.view.widget.SingleClickButtonListener;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import rx.Subscriber;
import rx.Subscription;

@ContentView(R.layout.activity_bulk_entries)
public class BulkEntriesActivity extends BaseActivity {

  @InjectPresenter(BulkEntriesPresenter.class)
  BulkEntriesPresenter bulkEntriesPresenter;

  @InjectView(R.id.rv_bulk_entries_product)
  RecyclerView rvBulkEntriesProducts;

  @InjectView(R.id.tv_total)
  TextView tvTotal;

  @InjectView(R.id.btn_save)
  View btnSave;

  @InjectView(R.id.action_panel)
  View actionPanel;

  @InjectView(R.id.msg_no_product)
  TextView msgNoProduct;

  @InjectView(R.id.iv_no_product)
  ImageView ivNoProduct;

  BulkEntriesAdapter adapter;

  List<Product> addedProducts;


  @Override
  protected ScreenName getScreenName() {
    return ScreenName.BULK_ENTRIES_SCREEN;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initRecyclerView();
    Subscription subscription = bulkEntriesPresenter.getAllAddedBulkEntriesViewModels(addedProducts)
        .subscribe(getOnViewModelsLoadedSubscriber());
    subscriptions.add(subscription);
    btnSave.setOnClickListener(getSaveListener());
    setViewGoneWhenNoProduct(bulkEntriesPresenter.getBulkEntriesViewModels());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.menu_bulk_entries, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_add_product) {
      openAddProductsActivityForResult();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  public void openAddProductsActivityForResult() {
    Intent intent = new Intent(getApplicationContext(),AddProductsToBulkEntriesActivity.class);
    intent.putExtra(SELECTED_PRODUCTS,
        (Serializable)bulkEntriesPresenter.getAddedProductCodes());
    addProductsActivityResultLauncher.launch(intent);
  }

  private void setViewGoneWhenNoProduct(List<BulkEntriesViewModel> bulkEntriesViewModels) {
    if (bulkEntriesViewModels.isEmpty()) {
      actionPanel.setVisibility(View.GONE);
      ivNoProduct.setVisibility(View.VISIBLE);
      msgNoProduct.setVisibility(View.VISIBLE);
    }

  }

  private final ActivityResultLauncher<Intent> addProductsActivityResultLauncher = registerForActivityResult(
      new StartActivityForResult(),
      result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
          addedProducts = (List<Product>) result.getData().getSerializableExtra(SELECTED_PRODUCTS);
          bulkEntriesPresenter.addNewProductsToBulkEntriesViewModels(addedProducts);
          adapter.refresh();
          adapter.notifyDataSetChanged();
        }
      });

  protected void setTotal(int total) {
    tvTotal.setText(getString(R.string.label_total, total));
  }

  private void initRecyclerView() {
    adapter = new BulkEntriesAdapter(bulkEntriesPresenter.getBulkEntriesViewModels());
    rvBulkEntriesProducts.setLayoutManager(new LinearLayoutManager(this));
    rvBulkEntriesProducts.setAdapter(adapter);
  }

  @NonNull
  protected Subscriber<List<BulkEntriesViewModel>> getOnViewModelsLoadedSubscriber() {
    return new Subscriber<List<BulkEntriesViewModel>>() {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        ToastUtil.show(e.getMessage());
        loaded();
      }

      @Override
      public void onNext(List<BulkEntriesViewModel> productsToBulkEntriesViewModels) {
        loaded();
        setTotal(adapter.getItemCount());
        adapter.notifyDataSetChanged();
      }
    };
  }

  @NonNull
  private SingleClickButtonListener getSaveListener() {
    return new SingleClickButtonListener() {
      @Override
      public void onSingleClick(View v) {
        btnSave.setEnabled(false);
        loading();
        Subscription subscription = bulkEntriesPresenter.saveDraftBulkEntriesObservable()
            .subscribe(getReloadSubscriber());
        subscriptions.add(subscription);
      }
    };
  }

  private Subscriber getReloadSubscriber() {
    return new Subscriber() {
      @Override
      public void onCompleted() {
        Toast.makeText(getApplicationContext(), R.string.succesfully_saved, Toast.LENGTH_LONG).show();
        loaded();
        btnSave.setEnabled(true);
      }

      @Override
      public void onError(Throwable e) {
        // do nothing
      }

      @Override
      public void onNext(Object o) {
        // do nothing
      }
    };
  }

}
