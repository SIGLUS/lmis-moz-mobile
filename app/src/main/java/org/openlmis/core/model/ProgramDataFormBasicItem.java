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

package org.openlmis.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DatabaseTable(tableName = "program_data_Basic_items")
public class ProgramDataFormBasicItem extends BaseModel {

  @Expose
  @SerializedName("productCode")
  @DatabaseField(foreign = true, foreignAutoRefresh = true)
  private Product product;

  @Expose
  @SerializedName("beginningBalance")
  @DatabaseField
  private Long initialAmount;

  @DatabaseField(defaultValue = "false")
  private Boolean isCustomAmount;

  @Expose
  @SerializedName("quantityReceived")
  @DatabaseField
  private long received;

  @Expose
  @SerializedName("quantityDispensed")
  @DatabaseField
  private long issued;

  @Expose
  @SerializedName("totalLossesAndAdjustments")
  @DatabaseField
  private long adjustment;

  @Expose
  @SerializedName("expirationDate")
  @DatabaseField
  private String validate;

  @Expose
  @SerializedName("stockInHand")
  @DatabaseField
  private Long inventory;

  @DatabaseField(foreign = true, foreignAutoRefresh = true)
  private ProgramDataForm form;

}
