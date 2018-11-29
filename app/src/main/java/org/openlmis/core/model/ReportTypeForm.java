package org.openlmis.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.openlmis.core.utils.ListUtil;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
