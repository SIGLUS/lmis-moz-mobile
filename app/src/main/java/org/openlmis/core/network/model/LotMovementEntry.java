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

package org.openlmis.core.network.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.openlmis.core.model.LotMovementItem;
import org.openlmis.core.utils.DateUtil;

@Data
@NoArgsConstructor
public class LotMovementEntry {

  private String lotNumber;
  private String expirationDate;
  private String reasonName;
  String documentationNo;
  private long quantity;
  private long stockOnHand;

  public LotMovementEntry(LotMovementItem lotMovementItem) {
    this.lotNumber = lotMovementItem.getLot().getLotNumber();
    this.expirationDate = DateUtil
        .formatDate(lotMovementItem.getLot().getExpirationDate(), DateUtil.DB_DATE_FORMAT);
    this.reasonName = lotMovementItem.getReason();
    this.documentationNo = lotMovementItem.getDocumentNumber();
    this.quantity = lotMovementItem.getMovementQuantity();
    this.stockOnHand = lotMovementItem.getStockOnHand();
  }
}
