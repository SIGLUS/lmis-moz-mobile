package org.openlmis.core.view.activity;

import android.content.Intent;

import com.google.inject.AbstractModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlmis.core.LMISTestRunner;
import org.openlmis.core.R;
import org.openlmis.core.model.Product;
import org.openlmis.core.model.builder.ProductBuilder;
import org.openlmis.core.presenter.UnpackKitPresenter;
import org.openlmis.core.utils.Constants;
import org.openlmis.core.view.viewmodel.InventoryViewModel;
import org.openlmis.core.view.viewmodel.StockCardViewModelBuilder;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;

import roboguice.RoboGuice;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(LMISTestRunner.class)
public class UnpackKitActivityTest {

    private UnpackKitPresenter mockedPresenter;
    private UnpackKitActivity stockMovementActivity;
    private Product product;
    private InventoryViewModel viewModel;

    @Before
    public void setUp() throws Exception {

        product = new ProductBuilder().setIsKit(false).setCode("productCode1").setPrimaryName("name1").build();
        viewModel = new StockCardViewModelBuilder(product).setChecked(true).setKitExpectQuantity(300).setQuantity("200").build();

        mockedPresenter = mock(UnpackKitPresenter.class);

        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new AbstractModule() {
            @Override
            protected void configure() {
                bind(UnpackKitPresenter.class).toInstance(mockedPresenter);
            }
        });

        Intent intent = new Intent()
                .putExtra(Constants.PARAM_KIT_CODE, "SD0001")
                .putExtra(Constants.PARAM_KIT_NUM, 1);
        stockMovementActivity = Robolectric.buildActivity(UnpackKitActivity.class).withIntent(intent).create().visible().get();
    }

    @Test
    public void shouldLoadKitProductsWithKitCode() throws Exception {
        verify(mockedPresenter).loadKitProducts("SD0001", 1);
    }

    @Test
    public void shouldSaveUnpackMovementsWhenQuantityIsValid() throws Exception {
        stockMovementActivity.refreshList(Arrays.asList(viewModel));

        stockMovementActivity.mAdapter.onCreateViewHolder(stockMovementActivity.productListRecycleView, 1).itemView.findViewById(R.id.btn_complete).performClick();

        verify(mockedPresenter).saveUnpackProducts(1);
    }

    @Test
    public void shouldNotSaveUnpackMovementsWhenQuantityIsNotValid() throws Exception {
        viewModel.setQuantity("");
        stockMovementActivity.refreshList(Arrays.asList(viewModel));

        stockMovementActivity.mAdapter.onCreateViewHolder(stockMovementActivity.productListRecycleView, 1).itemView.findViewById(R.id.btn_complete).performClick();

        verify(mockedPresenter, never()).saveUnpackProducts(1);
    }

    @After
    public void tearDown() throws Exception {
        RoboGuice.Util.reset();
    }
}