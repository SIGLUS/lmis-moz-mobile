package org.openlmis.core.view.activity;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlmis.core.LMISTestRunner;
import org.openlmis.core.utils.Constants;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(LMISTestRunner.class)
public class StockCardListActivityTest {

    private StockCardListActivity stockCardListActivity;

    @Before
    public void setUp() {
        stockCardListActivity = Robolectric.buildActivity(StockCardListActivity.class).create().get();
    }

    @Test
    public void shouldGetValidStockCardFragment() {
        assertThat(stockCardListActivity.stockCardFragment).isNotNull();
    }

    @Test
    public void shouldNavigateToAddNewDrugPageWhenMenuClicked() {
        shadowOf(stockCardListActivity).clickMenuItem(StockCardListActivity.MENU_ID_ADD_NEW_DRUG);

        Intent nextIntent = ShadowApplication.getInstance().getNextStartedActivity();

        assertThat(nextIntent.getComponent().getClassName()).isEqualTo(InitialInventoryActivity.class.getName());
        assertThat(nextIntent.getBooleanExtra(Constants.PARAM_IS_ADD_NEW_DRUG, false)).isTrue();
    }

    @Test
    public void shouldNavigateToStockCardArchiveListWhenMenuClicked() {
        shadowOf(stockCardListActivity).clickMenuItem(StockCardListActivity.MENU_ID_ARCHIVE_LIST);

        Intent nextIntent = ShadowApplication.getInstance().getNextStartedActivity();

        assertThat(nextIntent.getComponent().getClassName()).isEqualTo(ArchivedDrugsListActivity.class.getName());
    }

    @Test
    public void shouldGetIntentToMe() {
        Intent intent = StockCardListActivity.getIntentToMe(RuntimeEnvironment.application);

        assertThat(intent.getComponent().getClassName()).isEqualTo(StockCardListActivity.class.getName());
        assertThat(intent.getFlags()).isEqualTo(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}