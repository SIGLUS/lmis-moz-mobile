package org.openlmis.core.view.viewmodel;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.openlmis.core.model.RnrFormItem;

@Data
public class ViaKitsViewModel {

  private final String DEFAULT_VALUE = "0";

  private String kitsReceivedHF = DEFAULT_VALUE;

  private String kitsReceivedCHW = DEFAULT_VALUE;

  private String kitsOpenedHF = DEFAULT_VALUE;

  private String kitsOpenedCHW = DEFAULT_VALUE;

  private List<RnrFormItem> kitItems = new ArrayList<>();

  public static final String US_KIT = "26A01";

  public static final String APE_KIT = "26A02";

  public void convertRnrKitItemsToViaKit(List<RnrFormItem> rnrKitItems) {
    kitItems = rnrKitItems;

    for (RnrFormItem rnrKitItem : rnrKitItems) {

      if (US_KIT.equals(rnrKitItem.getProduct().getCode())) {
        if (rnrKitItem.getReceived() > Long.MIN_VALUE) {
          kitsReceivedHF = String.valueOf(rnrKitItem.getReceived());
        }

        if (rnrKitItem.getIssued() > Long.MIN_VALUE) {
          kitsOpenedHF = String.valueOf(rnrKitItem.getIssued());
        }
      } else if (APE_KIT.equals(rnrKitItem.getProduct().getCode())) {
        if (rnrKitItem.getReceived() > Long.MIN_VALUE) {
          kitsReceivedCHW = String.valueOf(rnrKitItem.getReceived());
        }

        if (rnrKitItem.getIssued() > Long.MIN_VALUE) {
          kitsOpenedCHW = String.valueOf(rnrKitItem.getIssued());
        }
      }
    }
  }
}