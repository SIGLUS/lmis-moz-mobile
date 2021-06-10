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
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "reports_type")
public class ReportTypeForm extends BaseModel {

  @Expose
  @SerializedName("code")
  @DatabaseField
  private String code;

  @Expose
  @SerializedName("name")
  @DatabaseField
  private String name;

  @Expose
  @SerializedName("description")
  @DatabaseField
  private String description;

  @Expose
  @SerializedName("active")
  @DatabaseField
  public boolean active;

  @Expose
  @SerializedName("startTime")
  @DatabaseField
  public Date startTime;

  @Expose
  @SerializedName("lastReportEndTime")
  @DatabaseField
  public String lastReportEndTime;

}
