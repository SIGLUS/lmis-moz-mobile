package org.openlmis.core.view.holder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import android.view.LayoutInflater;
import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlmis.core.LMISTestRunner;
import org.openlmis.core.R;
import org.openlmis.core.manager.MovementReasonManager;
import org.openlmis.core.manager.MovementReasonManager.MovementReason;
import org.openlmis.core.model.builder.StockMovementViewModelBuilder;
import org.openlmis.core.view.viewmodel.StockMovementViewModel;
import org.robolectric.RuntimeEnvironment;

@RunWith(LMISTestRunner.class)
public class StockMovementItemHistoryViewHolderTest {

  private StockMovementItemHistoryViewHolder viewHolder;
  private StockMovementViewModel viewModel;

  @Before
  public void setUp() {
    View itemView = LayoutInflater.from(RuntimeEnvironment.application)
        .inflate(R.layout.item_stock_movement, null, false);
    viewHolder = new StockMovementItemHistoryViewHolder(itemView);

    viewModel = new StockMovementViewModelBuilder().withIssued("100")
        .withMovementDate("2011-11-11")
        .withDocumentNo("12345")
        .withNegativeAdjustment(null)
        .withPositiveAdjustment(null)
        .withIssued("100")
        .withReceived(null)
        .withStockExistence("200")
        .withMovementReason(new MovementReason(MovementReasonManager.MovementType.ISSUE, "ISSUE_1",
            "issue description")).build();
  }

  @Test
  public void shouldPopulateTextDataWhenPopulatingData() {
    viewModel.setRequested("999");
    viewHolder.populate(viewModel);

    assertEquals("12345", viewHolder.etDocumentNo.getText().toString());
    assertEquals("2011-11-11", viewHolder.txMovementDate.getText().toString());
    assertEquals("", viewHolder.etReceived.getText().toString());
    assertEquals("", viewHolder.etNegativeAdjustment.getText().toString());
    assertEquals("", viewHolder.etPositiveAdjustment.getText().toString());
    assertEquals("100", viewHolder.etIssued.getText().toString());
    assertEquals("999", viewHolder.etRequested.getText().toString());
    assertEquals("200", viewHolder.txStockExistence.getText().toString());
    assertEquals("issue description", viewHolder.txReason.getText().toString());
  }

  @Test
  public void shouldDisableLineWhenPopulatingData() {
    viewHolder.populate(viewModel);

    assertFalse(viewHolder.etDocumentNo.isEnabled());
    assertFalse(viewHolder.etReceived.isEnabled());
    assertFalse(viewHolder.etNegativeAdjustment.isEnabled());
    assertFalse(viewHolder.etPositiveAdjustment.isEnabled());
    assertFalse(viewHolder.etIssued.isEnabled());
  }

  @Test
  public void shouldHideUnderlineAndDisableLine() {
    viewHolder.populate(viewModel);

    assertNull(viewHolder.etIssued.getBackground());
    assertNull(viewHolder.etRequested.getBackground());
    assertNull(viewHolder.etPositiveAdjustment.getBackground());
    assertNull(viewHolder.etNegativeAdjustment.getBackground());
    assertNull(viewHolder.etReceived.getBackground());
    assertNull(viewHolder.etDocumentNo.getBackground());

    assertFalse(viewHolder.etIssued.isEnabled());
    assertFalse(viewHolder.etRequested.isEnabled());
    assertFalse(viewHolder.etPositiveAdjustment.isEnabled());
    assertFalse(viewHolder.etNegativeAdjustment.isEnabled());
    assertFalse(viewHolder.etReceived.isEnabled());
    assertFalse(viewHolder.etDocumentNo.isEnabled());
  }

  @Test
  public void shouldSetPhysicalInventoryFontColorToRed() {
    viewModel.setReason(
        new MovementReason(MovementReasonManager.MovementType.PHYSICAL_INVENTORY, "INVENTORY", ""));
    viewHolder.populate(viewModel);

    int redColor = RuntimeEnvironment.application.getResources().getColor(R.color.color_red);

    assertEquals(redColor, viewHolder.txMovementDate.getCurrentTextColor());
    assertEquals(redColor, viewHolder.txReason.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etDocumentNo.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etReceived.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etPositiveAdjustment.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etNegativeAdjustment.getCurrentTextColor());
    assertEquals(redColor, viewHolder.txStockExistence.getCurrentTextColor());
  }

  @Test
  public void shouldSetNegativeInventoryAdjustmentFontColorToRed() {
    viewModel.setReason(new MovementReason(MovementReasonManager.MovementType.NEGATIVE_ADJUST,
        MovementReasonManager.INVENTORY_NEGATIVE, ""));
    viewHolder.populate(viewModel);

    int redColor = RuntimeEnvironment.application.getResources().getColor(R.color.color_red);

    assertEquals(redColor, viewHolder.txMovementDate.getCurrentTextColor());
    assertEquals(redColor, viewHolder.txReason.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etDocumentNo.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etReceived.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etPositiveAdjustment.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etNegativeAdjustment.getCurrentTextColor());
    assertEquals(redColor, viewHolder.txStockExistence.getCurrentTextColor());
  }

  @Test
  public void shouldSetPositiveInventoryAdjustmentFontColorToRed() {
    viewModel.setReason(new MovementReason(MovementReasonManager.MovementType.POSITIVE_ADJUST,
        MovementReasonManager.INVENTORY_POSITIVE, ""));
    viewHolder.populate(viewModel);

    int redColor = RuntimeEnvironment.application.getResources().getColor(R.color.color_red);

    assertEquals(redColor, viewHolder.txMovementDate.getCurrentTextColor());
    assertEquals(redColor, viewHolder.txReason.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etDocumentNo.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etReceived.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etPositiveAdjustment.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etNegativeAdjustment.getCurrentTextColor());
    assertEquals(redColor, viewHolder.txStockExistence.getCurrentTextColor());
  }

  @Test
  public void shouldSetReceiveFontColorToRed() {
    viewModel
        .setReason(new MovementReason(MovementReasonManager.MovementType.RECEIVE, "RECEIVE", ""));
    viewModel.setReceived("123");
    viewModel.setIssued(null);
    viewHolder.populate(viewModel);

    int redColor = RuntimeEnvironment.application.getResources().getColor(R.color.color_red);

    assertEquals(redColor, viewHolder.txMovementDate.getCurrentTextColor());
    assertEquals(redColor, viewHolder.txReason.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etDocumentNo.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etReceived.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etPositiveAdjustment.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etNegativeAdjustment.getCurrentTextColor());
    assertEquals(redColor, viewHolder.txStockExistence.getCurrentTextColor());
  }

  @Test
  public void shouldSetFontColorToRedIfReasonIsNotIssue() {
    viewModel.setReason(
        new MovementReason(MovementReasonManager.MovementType.POSITIVE_ADJUST, "positive_adjust",
            ""));
    viewModel.setIssued(null);
    viewModel.setPositiveAdjustment("123");
    viewHolder.populate(viewModel);

    int redColor = RuntimeEnvironment.application.getResources().getColor(R.color.color_red);

    assertEquals(redColor, viewHolder.txMovementDate.getCurrentTextColor());
    assertEquals(redColor, viewHolder.txReason.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etDocumentNo.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etReceived.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etPositiveAdjustment.getCurrentTextColor());
    assertEquals(redColor, viewHolder.etNegativeAdjustment.getCurrentTextColor());
    assertEquals(redColor, viewHolder.txStockExistence.getCurrentTextColor());
  }

  @Test
  public void shouldSetFontColorToBlackIfReasonIsIssue() {
    viewModel.setReason(new MovementReason(MovementReasonManager.MovementType.ISSUE, "Issued", ""));
    viewModel.setIssued("123");
    viewHolder.populate(viewModel);

    int blackColor = RuntimeEnvironment.application.getResources().getColor(R.color.color_black);

    assertEquals(blackColor, viewHolder.txMovementDate.getCurrentTextColor());
    assertEquals(blackColor, viewHolder.txReason.getCurrentTextColor());
    assertEquals(blackColor, viewHolder.etDocumentNo.getCurrentTextColor());
    assertEquals(blackColor, viewHolder.etReceived.getCurrentTextColor());
    assertEquals(blackColor, viewHolder.etPositiveAdjustment.getCurrentTextColor());
    assertEquals(blackColor, viewHolder.etNegativeAdjustment.getCurrentTextColor());
    assertEquals(blackColor, viewHolder.txStockExistence.getCurrentTextColor());
  }
}