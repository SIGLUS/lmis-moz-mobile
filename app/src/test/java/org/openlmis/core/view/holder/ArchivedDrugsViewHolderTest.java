package org.openlmis.core.view.holder;

import android.view.LayoutInflater;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlmis.core.LMISTestApp;
import org.openlmis.core.LMISTestRunner;
import org.openlmis.core.R;
import org.openlmis.core.model.Product;
import org.openlmis.core.model.builder.ProductBuilder;
import org.openlmis.core.view.viewmodel.StockCardViewModel;
import org.openlmis.core.view.viewmodel.StockCardViewModelBuilder;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(LMISTestRunner.class)
public class ArchivedDrugsViewHolderTest {

    private ArchivedDrugsViewHolder viewHolder;
    private String queryKeyWord = null;
    private StockCardViewModel viewModel;
    private ArchivedDrugsViewHolder.ArchiveStockCardListener mockedListener;

    @Before
    public void setUp() {
        ((LMISTestApp) RuntimeEnvironment.application).setFeatureToggle(true);

        View itemView = LayoutInflater.from(RuntimeEnvironment.application).inflate(R.layout.item_archived_drug, null, false);
        viewHolder = new ArchivedDrugsViewHolder(itemView);

        Product product = new ProductBuilder().setPrimaryName("Lamivudina 150mg").setCode("08S40").setStrength("10mg").setType("VIA").build();
        viewModel = new StockCardViewModelBuilder(product)
                .setQuantity("10")
                .setChecked(false)
                .setType("Embalagem")
                .setSOH(123L)
                .build();

        mockedListener = mock(ArchivedDrugsViewHolder.ArchiveStockCardListener.class);

        viewHolder.populate(viewModel, queryKeyWord, mockedListener);
    }

    @Test
    public void shouldShowProductNameAndStyledUnit() {
        assertThat(viewHolder.tvProductName.getText().toString()).isEqualTo("Lamivudina 150mg [08S40]");
        assertThat(viewHolder.tvProductUnit.getText().toString()).isEqualTo("10mg VIA");
    }

    @Test
    public void shouldViewMovementHistoryWhenHistoryViewClicked() {
        viewHolder.tvViewHistory.performClick();

        verify(mockedListener).viewMovementHistory(viewModel.getStockCard());
    }

    @Test
    public void shouldArchiveDrugBackWhenArchiveBackViewClicked() {
        viewHolder.tvArchiveBack.performClick();

        verify(mockedListener).archiveStockCardBack(viewModel.getStockCard());
    }
}